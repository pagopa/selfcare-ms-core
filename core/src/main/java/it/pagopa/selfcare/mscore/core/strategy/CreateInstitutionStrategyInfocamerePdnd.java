package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.institution.InfocamerePdndInstitution;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
@Component
public class CreateInstitutionStrategyInfocamerePdnd extends CreateInstitutionStrategyCommon implements CreateInstitutionStrategy {

    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private Institution institution;

    public CreateInstitutionStrategyInfocamerePdnd(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                               InstitutionConnector institutionConnector) {
        super(institutionConnector);
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }

    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {
        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        InfocamerePdndInstitution infocamerePdndInstitution = partyRegistryProxyConnector.getInfocamerePdndInstitution(strategyInput.getTaxCode());
        institution = addFieldsToInstitution(infocamerePdndInstitution);
        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution addFieldsToInstitution(InfocamerePdndInstitution infocamerePdndInstitution) {
        institution.setExternalId(institution.getTaxCode());
        institution.setOrigin(Origin.INFOCAMERE.getValue());
        institution.setOriginId(institution.getTaxCode());
        institution.setDigitalAddress(infocamerePdndInstitution.getDigitalAddress());
        institution.setDescription(infocamerePdndInstitution.getBusinessName());
        institution.setAddress(infocamerePdndInstitution.getAddress());
        institution.setCity(infocamerePdndInstitution.getCity());
        institution.setCounty(infocamerePdndInstitution.getCounty());
        institution.setZipCode(infocamerePdndInstitution.getZipCode());
        institution.setRea(infocamerePdndInstitution.getNRea());
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
