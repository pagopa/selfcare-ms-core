package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Component
public class CreateInstitutionStrategyRaw implements CreateInstitutionStrategy {

    private final InstitutionConnector institutionConnector;

    private Institution institution;

    public CreateInstitutionStrategyRaw(InstitutionConnector institutionConnector) {
        this.institutionConnector = institutionConnector;
    }

    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        institution.setExternalId(getExternalId(strategyInput));
        institution.setOrigin(Origin.SELC.getValue());
        institution.setOriginId("SELC_" + institution.getExternalId());
        institution.setCreatedAt(OffsetDateTime.now());

        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }

    }

    private void checkIfAlreadyExistsByTaxCodeAndSubunitCode(String taxCode, String subunitCode) {

        List<Institution> institutions = institutionConnector.findByTaxCodeSubunitCode(taxCode, subunitCode);
        if (!institutions.isEmpty())
            throw new ResourceConflictException(String
                    .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), taxCode, subunitCode),
                    CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
}
