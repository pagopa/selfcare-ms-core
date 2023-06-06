package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.*;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.strategy.factory.CreateInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ADMIN_PARTY_ROLE;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final UserService userService;
    private final CoreConfig coreConfig;
    private final ContractService contractService;

    private final InstitutionMapper institutionMapper;

    private final CreateInstitutionStrategyFactory createInstitutionStrategyFactory;

    public InstitutionServiceImpl(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                  InstitutionConnector institutionConnector,
                                  UserService userService, CoreConfig coreConfig,
                                  TokenConnector tokenConnector,
                                  UserConnector userConnector,
                                  ContractService contractService,
                                  InstitutionMapper institutionMapper,
                                  CreateInstitutionStrategyFactory createInstitutionStrategyFactory) {
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.userService = userService;
        this.coreConfig = coreConfig;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.contractService = contractService;
        this.institutionMapper = institutionMapper;
        this.createInstitutionStrategyFactory = createInstitutionStrategyFactory;
    }

    @Override
    public List<Onboarding> getOnboardingInstitutionByProductId(String institutionId, String productId) {
        return institutionConnector.findOnboardingByIdAndProductId(institutionId, productId);
    }

    @Override
    public Institution retrieveInstitutionById(String id) {
        return institutionConnector.findById(id);
    }

    @Override
    public Institution retrieveInstitutionByExternalId(String institutionExternalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(institutionExternalId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_FOUND.getMessage(), "UNDEFINED", institutionExternalId), CustomError.INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("founded institution having externalId: {}", institutionExternalId);
        return opt.get();
    }

    @Override
    public void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    CustomError.INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<Institution> getInstitutions(String taxCode, String subunitCode) {
        return institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
    }


    @Override
    public Institution createInstitutionFromIpa(String taxCode, InstitutionPaSubunitType subunitType, String subunitCode) {
        return createInstitutionStrategyFactory.createInstitutionStrategy(subunitType)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(taxCode)
                        .subunitCode(subunitCode)
                        .subunitType(subunitType)
                        .build());
    }
    @Override
    public Institution createInstitutionByExternalId(String externalId) {
        checkIfAlreadyExists(externalId);

        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);

        log.debug("institution from proxy: {}", institutionProxyInfo);
        log.info("getInstitution {}", institutionProxyInfo.getId());
        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
        log.info("category from proxy: {}", categoryProxyInfo);

        Institution newInstitution = institutionMapper.fromInstitutionProxyInfo(institutionProxyInfo);

        newInstitution.setExternalId(externalId);
        newInstitution.setOrigin(Origin.IPA.getValue());
        newInstitution.setCreatedAt(OffsetDateTime.now());

        Attributes attributes = new Attributes();
        attributes.setOrigin(categoryProxyInfo.getOrigin());
        attributes.setCode(categoryProxyInfo.getCode());
        attributes.setDescription(categoryProxyInfo.getName());
        newInstitution.setAttributes(List.of(attributes));
        try {
            return institutionConnector.save(newInstitution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    @Override
    public Institution createPnPgInstitution(String taxId, String description) {
        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxId);
        newInstitution.setDescription(description);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());
        newInstitution.setOrigin(Origin.INFOCAMERE.getValue());
        newInstitution.setOriginId(taxId); //TODO: CHE CAMPO USARE
        return institutionConnector.saveOrRetrievePnPg(newInstitution);
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
        newInstitution.setOriginId(taxId); //TODO: CHE CAMPO USARE

        //TODO: QUANDO SARA' DISPONIBILE IL SERVIZIO PUNTUALE PER CONOSCERE LA RAGIONE SOCIALE DATA LA PIVA SOSTITUIRE LA CHIAMATA
        if (existsInRegistry) {
            if (coreConfig.isInfoCamereEnable()) {
                List<InstitutionByLegal> institutionByLegal = partyRegistryProxyConnector.getInstitutionsByLegal(selfCareUser.getFiscalCode());
                institutionByLegal.stream()
                        .filter(i -> taxId.equalsIgnoreCase(i.getBusinessTaxId()))
                        .findFirst()
                        .ifPresentOrElse(institution -> newInstitution.setDescription(institution.getBusinessName()),
                                () -> {
                                    throw new InvalidRequestException(String.format(CustomError.INSTITUTION_LEGAL_NOT_FOUND.getMessage(), taxId), CustomError.INSTITUTION_LEGAL_NOT_FOUND.getCode());
                                });

                NationalRegistriesProfessionalAddress professionalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);
                if (professionalAddress != null) {
                    newInstitution.setAddress(professionalAddress.getAddress());
                    newInstitution.setZipCode(professionalAddress.getZip());
                }
            }
            newInstitution.setOrigin(Origin.INFOCAMERE.getValue());
        } else {
            newInstitution.setOrigin(Origin.ADE.getValue());
        }
        return institutionConnector.save(newInstitution);
    }

    @Override
    public Institution createInstitutionRaw(Institution institution, String externalId) {
        checkIfAlreadyExists(externalId);
        institution.setOrigin(Origin.SELC.getValue());
        institution.setOriginId("SELC_" + institution.getExternalId());
        institution.setCreatedAt(OffsetDateTime.now());
        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(Institution institution, List<RelationshipState> states) {
        List<Onboarding> onboardingList;
        if (institution.getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                onboardingList = institution.getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus()))
                        .collect(Collectors.toList());
            } else {
                onboardingList = institution.getOnboarding();
            }
            if (!onboardingList.isEmpty()) {
                return onboardingList;
            }
        }
        throw new ResourceNotFoundException(String.format(CustomError.PRODUCTS_NOT_FOUND_ERROR.getMessage(), institution.getId()), CustomError.PRODUCTS_NOT_FOUND_ERROR.getCode());
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionConnector.findByExternalIdAndProductId(externalId, productId);
    }

    @Override
    public List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(Institution institution) {
        log.info("Retrieving geographic taxonomies for institution {}", institution.getId());
        if (institution.getGeographicTaxonomies() != null) {
            List<GeographicTaxonomies> geographicTaxonomies = institution.getGeographicTaxonomies().stream()
                    .map(InstitutionGeographicTaxonomies::getCode)
                    .map(this::retrieveGeoTaxonomies)
                    .collect(Collectors.toList());
            if (!geographicTaxonomies.isEmpty()) {
                return geographicTaxonomies;
            }
        }
        throw new MsCoreException(String.format("GeographicTaonomies for institution %s not found", institution.getId()), "0000");
    }

    @Override
    public GeographicTaxonomies retrieveGeoTaxonomies(String code) {
        return partyRegistryProxyConnector.getExtByCode(code);
    }

    @Override
    public Institution updateInstitution(String institutionId, InstitutionUpdate institutionUpdate) {
            List<InstitutionGeographicTaxonomies> geographicTaxonomies = retrieveGeographicTaxonomies(institutionUpdate);
            return institutionConnector.findAndUpdate(institutionId, null, geographicTaxonomies, institutionUpdate);
    }

    private List<InstitutionGeographicTaxonomies> retrieveGeographicTaxonomies(InstitutionUpdate institutionUpdate) {
        if (institutionUpdate.getGeographicTaxonomies() != null) {
            return institutionUpdate.getGeographicTaxonomies()
                    .stream()
                    .map(geoTaxonomy -> retrieveGeoTaxonomies(geoTaxonomy.getCode()))
                    .map(geo -> new InstitutionGeographicTaxonomies(geo.getGeotaxId(), geo.getDescription())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ValidInstitution> retrieveInstitutionByExternalIds(List<ValidInstitution> validInstitutionList, String productId) {
        List<String> institutionsExternalId = institutionConnector.findByExternalIdsAndProductId(validInstitutionList, productId);
        validInstitutionList.removeIf(validInstitution -> institutionsExternalId.contains(validInstitution.getId()));
        return validInstitutionList;
    }

    @Override
    public List<Institution> findInstitutionsByGeoTaxonomies(String geoTaxonomies, SearchMode searchMode) {
        List<String> geo = Arrays.stream(geoTaxonomies.split(","))
                .filter(StringUtils::hasText).collect(Collectors.toList());
        validateGeoTaxonomies(geo, searchMode);
        return institutionConnector.findByGeotaxonomies(geo, searchMode);
    }

    private void validateGeoTaxonomies(List<String> geoTaxonomies, SearchMode searchMode) {
        if (geoTaxonomies.isEmpty() && searchMode != SearchMode.EXACT) {
            throw new InvalidRequestException("Empty geographic taxonomies filter is valid only when searchMode is exact", "0000");
        }
    }

    @Override
    public List<Institution> findInstitutionsByProductId(String productId) {
        List<Institution> institutions = institutionConnector.findByProductId(productId);
        if (institutions.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Institutions with productId %s not found", productId), "0000");
        }
        return institutions;
    }

    @Override
    public List<Institution> retrieveInstitutionByIds(List<String> ids) {
        return institutionConnector.findAllByIds(ids);
    }

    @Override
    public List<RelationshipInfo> retrieveUserInstitutionRelationships(Institution institution, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<OnboardedUser> adminRelationships = userService.retrieveUsers(institution.getId(), userId, ADMIN_PARTY_ROLE, ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES, null, null);
        String personToFilter = personId;
        if (adminRelationships.isEmpty()) {
            personToFilter = userId;
        }
        List<OnboardedUser> institutionRelationships = userService.retrieveUsers(institution.getId(), personToFilter, roles, states, products, productRoles);
        return toRelationshipInfo(institutionRelationships, institution, roles, states, products, productRoles);
    }

    @Override
    public List<RelationshipInfo> retrieveUserRelationships(String userId, String institutionId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        if (userId != null) {
            return toRelationshipInfo(userService.retrieveUsers(null, userId, roles, states, products, productRoles), null, roles, states, products, productRoles);
        } else if (institutionId != null) {
            Institution institution = retrieveInstitutionById(institutionId);
            return toRelationshipInfo(userService.retrieveUsers(institutionId, null, roles, states, products, productRoles), institution, roles, states, products, productRoles);
        }
        throw new InvalidRequestException(CustomError.MISSING_QUERY_PARAMETER.getMessage(), CustomError.MISSING_QUERY_PARAMETER.getCode());
    }

    @Override
    public void updateCreatedAt(String institutionId, String productId, OffsetDateTime createdAt) {
        log.trace("updateCreatedAt start");
        log.debug("updateCreatedAt institutionId = {}, productId = {}, createdAt = {}", institutionId, productId, createdAt);
        Assert.hasText(institutionId, "An institution ID is required.");
        Assert.hasText(productId, "A product ID is required.");
        Assert.notNull(createdAt, "A createdAt date is required.");

        Institution updatedInstitution = institutionConnector.updateOnboardedProductCreatedAt(institutionId, productId, createdAt);
        String tokenId = updatedInstitution.getOnboarding().stream()
                .filter(onboarding -> onboarding.getProductId().equals(productId))
                .findFirst().get().getTokenId();
        Token updatedToken = tokenConnector.updateTokenCreatedAt(tokenId, createdAt);
        List<String> usersId = updatedToken.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList());
        userConnector.updateUserBindingCreatedAt(institutionId, productId, usersId, createdAt);

        contractService.sendDataLakeNotification(updatedInstitution, updatedToken, QueueEvent.UPDATE);

        log.trace("updateCreatedAt end");
    }

    public void checkIfAlreadyExists(String externalId) {
        log.info("START - check institution {} already exists", externalId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_INSTITUTION_CONFLICT.getMessage(), externalId), CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
        }
    }


    private void checkIfAlreadyExists(String taxCode, String subunitCode) {
        /* check if institution exists */
        List<Institution> institutions = institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
        if(!institutions.isEmpty())
            throw new ResourceConflictException(String
                    .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), taxCode, subunitCode),
                    CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
    }
    private List<RelationshipInfo> toRelationshipInfo(List<OnboardedUser> institutionRelationships, Institution institution, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<RelationshipInfo> list = new ArrayList<>();
        for (OnboardedUser onboardedUser : institutionRelationships) {
            for (UserBinding binding : onboardedUser.getBindings()) {
                list.addAll(retrieveAllProduct(onboardedUser.getId(), binding, institution, roles, states, products, productRoles));
            }
        }
        return list;
    }

    @Override
    public List<RelationshipInfo> retrieveAllProduct(String userId, UserBinding binding, Institution institution, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        List<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        if (institution != null) {
            if (institution.getId().equalsIgnoreCase(binding.getInstitutionId())) {
                relationshipInfoList = binding.getProducts().stream()
                        .filter(product -> filterProduct(product, roles, states, products, productRoles))
                        .map(product -> {
                            RelationshipInfo relationshipInfo = new RelationshipInfo();
                            relationshipInfo.setInstitution(institution);
                            relationshipInfo.setUserId(userId);
                            relationshipInfo.setOnboardedProduct(product);
                            return relationshipInfo;
                        })
                        .collect(Collectors.toList());
            }
        } else {
            for (OnboardedProduct product : binding.getProducts()) {
                if (Boolean.TRUE.equals(filterProduct(product, roles, states, products, productRoles))) {
                    Institution retrievedInstitution = retrieveInstitutionById(binding.getInstitutionId());
                    RelationshipInfo relationshipInfo = new RelationshipInfo();
                    relationshipInfo.setInstitution(retrievedInstitution);
                    relationshipInfo.setUserId(userId);
                    relationshipInfo.setOnboardedProduct(product);
                    relationshipInfoList.add(relationshipInfo);
                }
            }
        }
        return relationshipInfoList;
    }

    protected Boolean filterProduct(OnboardedProduct product, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {

        if (roles != null && !roles.isEmpty() && !roles.contains(product.getRole())) {
            return false;
        }

        if (states != null && !states.isEmpty() && !states.contains(product.getStatus())) {
            return false;
        }

        if (products != null && !products.isEmpty() && !products.contains(product.getProductId())) {
            return false;
        }

        return !(productRoles != null && !productRoles.isEmpty() && !productRoles.contains(product.getProductRole()));

    }

}
