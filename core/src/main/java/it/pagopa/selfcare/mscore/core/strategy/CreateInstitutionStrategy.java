package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
public class CreateInstitutionStrategy {

    private final InstitutionConnector institutionConnector;

    private final Consumer<CreateInstitutionStrategyInput> checkIfAlreadyExists;
    private final Function<CreateInstitutionStrategyInput, Institution> mappingToInstitution;

    public CreateInstitutionStrategy(InstitutionConnector institutionConnector, Consumer<CreateInstitutionStrategyInput> checkIfAlreadyExists, Function<CreateInstitutionStrategyInput, Institution> mappingToInstitution) {
        this.institutionConnector = institutionConnector;
        this.checkIfAlreadyExists = checkIfAlreadyExists;
        this.mappingToInstitution = mappingToInstitution;
    }


    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        checkIfAlreadyExists.accept(strategyInput);

        Institution newInstitution = mappingToInstitution.apply(strategyInput);

        try {
            return institutionConnector.save(newInstitution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }
}
