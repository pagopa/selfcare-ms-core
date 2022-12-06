package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.core.exception.InstitutionConflictException;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.Institution;
import it.pagopa.selfcare.mscore.model.InstitutionProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    public InstitutionServiceImpl(InstitutionConnector institutionConnector,
                                  PartyRegistryProxyConnector partyRegistryProxyConnector) {
        this.institutionConnector = institutionConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }

    @Override
    public Institution createInstitutionByExternalId(String externalId) {
        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);
        log.debug("institution from proxy: {}", institutionProxyInfo);
        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
        log.info("category from proxy: {}", categoryProxyInfo);

        Institution newInstitution = new Institution();
        newInstitution.setExternalId(externalId);
        newInstitution.setIpaCode(institutionProxyInfo.getOriginId());
        newInstitution.setDescription(institutionProxyInfo.getDescription());
        newInstitution.setDigitalAddress(institutionProxyInfo.getDigitalAddress());
        newInstitution.setTaxCode(institutionProxyInfo.getTaxCode());
        newInstitution.setAddress(institutionProxyInfo.getAddress());
        newInstitution.setZipCode(institutionProxyInfo.getZipCode());

        Optional<Institution> alreadyPresent = institutionConnector.findByExternalId(externalId);
        if (alreadyPresent.isPresent()) {
            log.warn("institution with externalId {} already exist with id {}", externalId, alreadyPresent.get().getId());
            throw new InstitutionConflictException();
        }
        return institutionConnector.save(newInstitution);
    }

}
