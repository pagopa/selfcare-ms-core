package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomError.CREATE_INSTITUTION_ORIGIN_CONFLICT;

@Slf4j
@Component
public class CreateInstitutionStrategyCommon {
    protected final InstitutionConnector institutionConnector;

    public CreateInstitutionStrategyCommon(InstitutionConnector institutionConnector) {
        this.institutionConnector = institutionConnector;
    }

    protected void checkIfAlreadyExistsByTaxCodeAndSubunitCode(String taxCode, String subunitCode) {
        List<Institution> institutions = institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
        if (!institutions.isEmpty())
            throw new ResourceConflictException(String
                    .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), taxCode, subunitCode),
                    CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
    }

    protected void checkIfAlreadyExistByOriginAndOriginId(String origin, String originId) {
        List<Institution> institutions = institutionConnector.findByOriginAndOriginId(origin, originId);
        if (!institutions.isEmpty())
            throw new ResourceConflictException(String.format(
                    CREATE_INSTITUTION_ORIGIN_CONFLICT.getMessage(), origin, originId),
                    CREATE_INSTITUTION_ORIGIN_CONFLICT.getCode());
    }
}
