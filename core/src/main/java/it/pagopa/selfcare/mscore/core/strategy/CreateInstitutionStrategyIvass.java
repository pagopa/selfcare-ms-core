package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.institution.ASResource;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
public class CreateInstitutionStrategyIvass extends CreateInstitutionStrategyCommon implements CreateInstitutionStrategy {
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private Institution institution;

    public CreateInstitutionStrategyIvass(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                          InstitutionConnector institutionConnector) {
        super(institutionConnector);
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {
        checkIfAlreadyExistByOriginAndOriginId(Origin.IVASS.name(), strategyInput.getIvassCode());

        ASResource asResource = partyRegistryProxyConnector.getASFromIvass(strategyInput.getIvassCode());

        institution = addFieldsToInstitution(asResource);
        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution addFieldsToInstitution(ASResource asResource) {
        institution.setTaxCode(asResource.getTaxCode());
        institution.setExternalId(asResource.getOriginId());
        institution.setOrigin(Origin.IVASS.getValue());
        institution.setOriginId(asResource.getOriginId());
        institution.setCreatedAt(OffsetDateTime.now());
        institution.setDigitalAddress(asResource.getDigitalAddress());
        institution.setDescription(asResource.getDescription());

        return institution;
    }

}
