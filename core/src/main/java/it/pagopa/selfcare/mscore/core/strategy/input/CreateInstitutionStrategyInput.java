package it.pagopa.selfcare.mscore.core.strategy.input;

import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateInstitutionStrategyInput {

    private String taxCode;
    private InstitutionPaSubunitType subunitType;
    private String subunitCode;
}
