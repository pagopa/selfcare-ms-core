package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ADMIN_PARTY_ROLE;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;
    private final UserService userService;
    private final CoreConfig coreConfig;

    public InstitutionServiceImpl(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                  InstitutionConnector institutionConnector,
                                  GeoTaxonomiesConnector geoTaxonomiesConnector,
                                  UserService userService, CoreConfig coreConfig) {
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
        this.userService = userService;
        this.coreConfig = coreConfig;
    }

    @Override
    public Institution retrieveInstitutionById(String id) {
        return institutionConnector.findById(id);
    }

    @Override
    public Institution retrieveInstitutionByExternalId(String institutionExternalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(institutionExternalId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), "UNDEFINED", institutionExternalId), INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("founded institution having externalId: {}", institutionExternalId);
        return opt.get();
    }

    @Override
    public void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public Institution createInstitutionByExternalId(String externalId) {
        checkIfAlreadyExists(externalId);

        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);
        log.debug("institution from proxy: {}", institutionProxyInfo);
        log.info("getInstitution {}", institutionProxyInfo.getId());
        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
        log.info("category from proxy: {}", categoryProxyInfo);

        Institution newInstitution = new Institution();
        newInstitution.setExternalId(externalId);
        newInstitution.setInstitutionType(InstitutionType.PA);
        newInstitution.setOrigin(Origin.IPA);
        newInstitution.setOriginId(institutionProxyInfo.getOriginId());
        newInstitution.setTaxCode(institutionProxyInfo.getTaxCode());
        newInstitution.setAddress(institutionProxyInfo.getAddress());
        newInstitution.setZipCode(institutionProxyInfo.getZipCode());

        newInstitution.setDescription(institutionProxyInfo.getDescription());
        newInstitution.setDigitalAddress(institutionProxyInfo.getDigitalAddress());

        newInstitution.setCreatedAt(OffsetDateTime.now());

        Attributes attributes = new Attributes();
        attributes.setOrigin(categoryProxyInfo.getOrigin());
        attributes.setCode(categoryProxyInfo.getCode());
        attributes.setDescription(categoryProxyInfo.getName());
        newInstitution.setAttributes(List.of(attributes));

        return institutionConnector.save(newInstitution);
    }

    @Override
    public Institution createPgInstitution(String taxId, String description, boolean existsInRegistry, SelfCareUser selfCareUser) {
        checkIfAlreadyExists(taxId);
        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxId);
        newInstitution.setDescription(description);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());
        newInstitution.setOrigin(Origin.INFOCAMERE);
        newInstitution.setOriginId(taxId); //TODO: CHE CAMPO USARE

        //TODO: QUANDO SARA' DISPONIBILE IL SERVIZIO PUNTUALE PER CONOSCERE LA RAGIONE SOCIALE DATA LA PIVA SOSTITUIRE LA CHIAMATA
        if (existsInRegistry && coreConfig.isInfoCamereEnable()) {
            List<InstitutionByLegal> institutionByLegal = partyRegistryProxyConnector.getInstitutionsByLegal(selfCareUser.getFiscalCode());
            institutionByLegal.stream()
                    .filter(i -> taxId.equalsIgnoreCase(i.getBusinessTaxId()))
                    .findFirst()
                    .ifPresentOrElse(institution -> newInstitution.setDescription(institution.getBusinessName()),
                            () -> {
                                throw new InvalidRequestException(String.format(INSTITUTION_LEGAL_NOT_FOUND.getMessage(), taxId), INSTITUTION_LEGAL_NOT_FOUND.getCode());
                            });

            NationalRegistriesProfessionalAddress professionalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);
            if (professionalAddress != null) {
                newInstitution.setAddress(professionalAddress.getAddress());
                newInstitution.setZipCode(professionalAddress.getZip());
            }
        }
        return institutionConnector.save(newInstitution);
    }

    @Override
    public Institution createInstitutionRaw(Institution institution, String externalId) {
        checkIfAlreadyExists(externalId);
        if (institution.getInstitutionType() == null) {
            institution.setInstitutionType(InstitutionType.UNKNOWN);
        }
        institution.setOrigin(Origin.SELC);
        institution.setOriginId("SELC_" + institution.getExternalId());
        institution.setCreatedAt(OffsetDateTime.now());
        return institutionConnector.save(institution);
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(Institution institution, List<RelationshipState> states) {
        if (institution.getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                return institution.getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus()))
                        .collect(Collectors.toList());
            } else {
                return institution.getOnboarding();
            }
        } else {
            throw new ResourceNotFoundException(String.format(PRODUCTS_NOT_FOUND_ERROR.getMessage(), institution.getId()), PRODUCTS_NOT_FOUND_ERROR.getCode());
        }
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionConnector.findInstitutionProduct(externalId, productId);
    }

    @Override
    public GeographicTaxonomies retrieveGeoTaxonomies(String code) {
        return geoTaxonomiesConnector.getExtByCode(code);
    }

    @Override
    public List<RelationshipInfo> retrieveUserInstitutionRelationships(Institution institution, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<OnboardedUser> adminRelationships = userService.retrieveUsers(institution.getId(), userId, ADMIN_PARTY_ROLE, ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES, null, null);
        String personToFilter = personId;
        if(adminRelationships.isEmpty()){
            personToFilter = userId;
        }
        List<OnboardedUser> institutionRelationships = userService.retrieveUsers(institution.getId(), personToFilter, roles, states, products, productRoles);
        return toRelationshipInfo(institutionRelationships, institution);
    }

    public void checkIfAlreadyExists(String externalId) {
        log.info("START - check institution {} already exists", externalId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            throw new ResourceConflictException(String.format(CREATE_INSTITUTION_CONFLICT.getMessage(), externalId), CREATE_INSTITUTION_CONFLICT.getCode());
        }
    }

    private List<RelationshipInfo> toRelationshipInfo(List<OnboardedUser> institutionRelationships, Institution institution) {
        List<RelationshipInfo> list = new ArrayList<>();
        for (OnboardedUser onboardedUser : institutionRelationships) {
            for (UserBinding binding : onboardedUser.getBindings()) {
                retrieveAllProduct(list, onboardedUser.getId(), binding, institution);
            }
        }
        return list;
    }

    private void retrieveAllProduct(List<RelationshipInfo> list, String userId, UserBinding binding, Institution institution) {
        if (institution.getId().equalsIgnoreCase(binding.getInstitutionId())) {
            for (OnboardedProduct product : binding.getProducts()) {
                RelationshipInfo relationshipInfo = new RelationshipInfo();
                relationshipInfo.setInstitution(institution);
                relationshipInfo.setUserId(userId);
                relationshipInfo.setOnboardedProduct(product);
                list.add(relationshipInfo);
            }
        }
    }
}
