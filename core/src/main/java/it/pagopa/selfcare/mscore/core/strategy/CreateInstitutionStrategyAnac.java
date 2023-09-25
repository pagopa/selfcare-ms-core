package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.SaResource;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
public class CreateInstitutionStrategyAnac implements CreateInstitutionStrategy {
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private final InstitutionConnector institutionConnector;

    private Institution institution;

    public CreateInstitutionStrategyAnac(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                         InstitutionConnector institutionConnector) {
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        SaResource saResource = partyRegistryProxyConnector.getSAFromAnac(strategyInput.getTaxCode());

        institution = addFieldsToInstitution(strategyInput.getTaxCode(), saResource);
        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution addFieldsToInstitution(String taxCode, SaResource saResource) {

        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxCode);
        newInstitution.setOrigin(Origin.ANAC.getValue());
        newInstitution.setOriginId(saResource.getOriginId());
        newInstitution.setCreatedAt(OffsetDateTime.now());
        newInstitution.setDigitalAddress(saResource.getDigitalAddress());
        newInstitution.setDescription(saResource.getDescription());

        return newInstitution;
    }

    private void checkIfAlreadyExistsByTaxCodeAndSubunitCode(String taxCode, String subunitCode) {

        List<Institution> institutions = institutionConnector.findByTaxCodeSubunitCode(taxCode, subunitCode);
        if (!institutions.isEmpty())
            throw new ResourceConflictException(String
                    .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), taxCode, subunitCode),
                    CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
    }

}
