package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.SaResource;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
public class CreateInstitutionStrategyAnac extends CreateInstitutionStrategyCommon implements CreateInstitutionStrategy {
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private Institution institution;

    public CreateInstitutionStrategyAnac(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                         InstitutionConnector institutionConnector) {
        super(institutionConnector);
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        SaResource saResource = partyRegistryProxyConnector.getSAFromAnac(strategyInput.getTaxCode());

        institution = addFieldsToInstitution(saResource);
        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution addFieldsToInstitution(SaResource saResource) {

        institution.setExternalId(institution.getTaxCode());
        institution.setOrigin(Origin.ANAC.getValue());
        institution.setOriginId(saResource.getTaxCode());
        institution.setCreatedAt(OffsetDateTime.now());
        institution.setDigitalAddress(saResource.getDigitalAddress());
        institution.setDescription(saResource.getDescription());

        return institution;
    }

}
