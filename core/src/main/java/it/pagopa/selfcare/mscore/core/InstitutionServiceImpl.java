package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;

    //TODO: ADD private final NationalRegistriesConnector nationalRegistriesConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private static final String INSTITUTION_CREATED_LOG = "institution created {}";

    public InstitutionServiceImpl(InstitutionConnector institutionConnector, PartyRegistryProxyConnector partyRegistryProxyConnector) {
        this.institutionConnector = institutionConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }

    @Override
    public Institution createInstitutionByExternalId(String externalId) {
        log.info("Creating institution having external id {}", externalId);

        checkAlreadyExists(externalId);

        Institution newInstitution = new Institution();

        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);

        log.debug("institution from proxy: {}", institutionProxyInfo);
        log.info("getInstitution {}", institutionProxyInfo.getId());

        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
        log.info("category from proxy: {}", categoryProxyInfo);

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

        return saveInstitution(newInstitution);
    }

    @Override
    public Institution createPgInstitution(String taxId, SelfCareUser selfCareUser) {
        checkAlreadyExists(taxId);

        Institution newInstitution = new Institution();

        newInstitution.setExternalId(taxId);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());

        //TODO: QUANDO SARA' DISPONIBILE IL SERVIZIO PUNTUALE PER CONOSCERE LA RAGIONE SOCIALE DATA LA PIVA SOSTITUIRE LA SEGUENTE CHIAMATA
        List<InstitutionByLegal> institutionByLegal = partyRegistryProxyConnector.getInstitutionsByLegal(selfCareUser.getFiscalCode());
        institutionByLegal.stream().filter(i -> taxId.equalsIgnoreCase(i.getBusinessTaxId()))
                .findFirst().ifPresentOrElse(institution -> newInstitution.setDescription(institution.getBusinessName()),
                        () -> {
                            throw new InvalidRequestException(String.format(INSTITUTION_LEGAL_NOT_FOUND.getMessage(), taxId),
                                    INSTITUTION_LEGAL_NOT_FOUND.getCode());
                        });


        //TODO: ADD QUANDO NATIONAL REGISTRIES E INFO CAMERE SARANNO FUNZIONANTI
        // NationalRegistriesProfessionalAddress response = nationalRegistriesConnector.getLegalAddress(taxId);
        // newInstitution.setAddress(response.getAddress());
        // newInstitution.setZipCode(response.getZip());

        return saveInstitution(newInstitution);
    }


    @Override
    public Institution createInstitutionRaw(Institution institution, String externalId) {
        checkAlreadyExists(externalId);
        if (institution.getInstitutionType() != null) {
            institution.setIpaCode(institution.getInstitutionType().toString());
        } else {
            institution.setInstitutionType(InstitutionType.UNKNOWN);
            institution.setIpaCode("SELC_" + externalId);
        }
        institution.setCreatedAt(OffsetDateTime.now());
        return saveInstitution(institution);
    }

    private void checkAlreadyExists(String externalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent())
            throw new ResourceConflictException(String.format(CREATE_INSTITUTION_CONFLICT.getMessage(), externalId), CREATE_INSTITUTION_CONFLICT.getCode());
    }

    private Institution saveInstitution(Institution institution) {
        Institution saved = institutionConnector.save(institution);
        log.info(INSTITUTION_CREATED_LOG, saved.getExternalId());
        return saved;
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(String id, List<String> states) {
        Optional<Institution> optionalInstitution = institutionConnector.findById(id);
        if (optionalInstitution.isPresent() && optionalInstitution.get().getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                return optionalInstitution.get().getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus().name()))
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>(optionalInstitution.get().getOnboarding());
            }
        } else {
            throw new ResourceNotFoundException(String.format(PRODUCTS_NOT_FOUND_ERROR.getMessage(), id), PRODUCTS_NOT_FOUND_ERROR.getCode());
        }
    }
}
