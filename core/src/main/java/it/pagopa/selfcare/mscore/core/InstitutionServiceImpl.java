package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.NationalRegistriesConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressResponse;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.*;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;
    private final NationalRegistriesConnector nationalRegistriesConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private static final String INSTITUTION_CREATED_LOG = "institution created {}";

    public InstitutionServiceImpl(InstitutionConnector institutionConnector,
                                  NationalRegistriesConnector nationalRegistriesConnector, PartyRegistryProxyConnector partyRegistryProxyConnector) {
        this.institutionConnector = institutionConnector;
        this.nationalRegistriesConnector = nationalRegistriesConnector;
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

        Attributes attributes = new Attributes();
        attributes.setOrigin(categoryProxyInfo.getOrigin());
        attributes.setCode(categoryProxyInfo.getCode());
        attributes.setDescription(categoryProxyInfo.getName());
        newInstitution.setAttributes(List.of(attributes));

        return saveInstitution(newInstitution);
    }

    @Override
    public Institution createPgInstitution(String taxId) {
        checkAlreadyExists(taxId);

        Institution newInstitution = new Institution();
        NationalRegistriesAddressResponse response = nationalRegistriesConnector.getLegalAddress(taxId);

        newInstitution.setExternalId(taxId);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setAddress(response.getProfessionalAddress().getAddress());
        newInstitution.setZipCode(response.getProfessionalAddress().getZip());

        return saveInstitution(newInstitution);
    }


    @Override
    public Institution createInstitutionRaw(Institution institution, String externalId) {
        checkAlreadyExists(externalId);
        institution.setIpaCode(Optional.ofNullable(institution.getInstitutionType().toString())
                .orElse("SELC_" + externalId));

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
}
