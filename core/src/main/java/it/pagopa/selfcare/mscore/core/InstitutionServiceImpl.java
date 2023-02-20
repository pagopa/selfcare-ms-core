package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {
    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;

    public InstitutionServiceImpl(PartyRegistryProxyConnector partyRegistryProxyConnector, InstitutionConnector institutionConnector, GeoTaxonomiesConnector geoTaxonomiesConnector) {
         this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
    }
    @Override
    public Institution retrieveInstitutionById(String id){
        return institutionConnector.findById(id);
    }

    @Override
    public Institution retrieveInstitutionByExternalId(String institutionExternalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(institutionExternalId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), null,institutionExternalId), INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("founded institution having externalId: {}", institutionExternalId);
        return opt.get();
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
        newInstitution.setTaxCode(institutionProxyInfo.getTaxCode());
        newInstitution.setAddress(institutionProxyInfo.getAddress());
        newInstitution.setZipCode(institutionProxyInfo.getZipCode());

        newInstitution.setIpaCode(institutionProxyInfo.getOriginId());
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
    public Institution createPgInstitution(String taxId, boolean existsInRegistry, SelfCareUser selfCareUser) {
        checkIfAlreadyExists(taxId);
        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxId);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());

        //TODO: QUANDO SARA' DISPONIBILE IL SERVIZIO PUNTUALE PER CONOSCERE LA RAGIONE SOCIALE DATA LA PIVA SOSTITUIRE LA CHIAMATA
        if (existsInRegistry) {
            List<InstitutionByLegal> institutionByLegal = partyRegistryProxyConnector.getInstitutionsByLegal(selfCareUser.getFiscalCode());
            institutionByLegal.stream().filter(i -> taxId.equalsIgnoreCase(i.getBusinessTaxId()))
                    .findFirst().ifPresentOrElse(institution -> newInstitution.setDescription(institution.getBusinessName()),
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
        institution.setCreatedAt(OffsetDateTime.now());
        return institutionConnector.save(institution);
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(String institutionId, List<String> states) {
        Institution institution = retrieveInstitutionById(institutionId);
        if (institution.getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                return institution.getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus().name()))
                        .collect(Collectors.toList());
            } else {
                return institution.getOnboarding();
            }
        } else {
            throw new ResourceNotFoundException(String.format(PRODUCTS_NOT_FOUND_ERROR.getMessage(), institutionId), PRODUCTS_NOT_FOUND_ERROR.getCode());
        }
    }
    @Override
    public Institution getInstitutionProduct(String externalId, String productId) {
        return institutionConnector.findInstitutionProduct(externalId, productId);
    }

    public void checkIfAlreadyExists(String externalId) {
        log.info("START - check institution {} already exists", externalId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            throw new ResourceConflictException(String.format(CREATE_INSTITUTION_CONFLICT.getMessage(), externalId), CREATE_INSTITUTION_CONFLICT.getCode());
        }
    }

    public GeographicTaxonomies getGeoTaxonomies(String code) {
        return geoTaxonomiesConnector.getExtByCode(code);
    }

    @Override
    public void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }
}
