package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.utils.MaskDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@Component
public class CreateInstitutionStrategyInfocamere extends CreateInstitutionStrategyCommon implements CreateInstitutionStrategy {

    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private Institution institution;

    public CreateInstitutionStrategyInfocamere(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                               InstitutionConnector institutionConnector) {
        super(institutionConnector);
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }


    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {
        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        try {
            NationalRegistriesProfessionalAddress professionalAddress = partyRegistryProxyConnector.getLegalAddress(strategyInput.getTaxCode());
            getInstitutionFromInfocamere(strategyInput.getTaxCode(), strategyInput.getDescription(), professionalAddress);
        } catch(MsCoreException ex) {
            if(ex.getCode().equalsIgnoreCase(String.valueOf(HttpStatus.NOT_FOUND.value()))) {
                log.warn(String.format(INSTITUTION_INFOCAMERE_NOTFOUND.getMessage(), MaskDataUtils.maskString(strategyInput.getTaxCode())));
                getInstitutionRaw(strategyInput);
            } else {
                throw ex;
            }
        } catch (ResourceNotFoundException ex) {
            log.warn(String.format(INSTITUTION_INFOCAMERE_NOTFOUND.getMessage(), MaskDataUtils.maskString(strategyInput.getTaxCode())));
            getInstitutionRaw(strategyInput);
        }

        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private void getInstitutionRaw(CreateInstitutionStrategyInput strategyInput) {
        institution.setExternalId(getExternalId(strategyInput));
        institution.setOrigin(Origin.ADE.getValue());
        institution.setOriginId("SELC_" + institution.getExternalId());
        institution.setCreatedAt(OffsetDateTime.now());
    }

    private void getInstitutionFromInfocamere(String taxCode, String description, NationalRegistriesProfessionalAddress professionalAddress) {
        institution.setAddress(professionalAddress.getAddress());
        institution.setZipCode(professionalAddress.getZipCode());
        institution.setTaxCode(taxCode);
        institution.setDescription(description);
        institution.setExternalId(taxCode);
        institution.setOrigin(Origin.INFOCAMERE.getValue());
        institution.setOriginId(taxCode);
        institution.setCreatedAt(OffsetDateTime.now());
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
