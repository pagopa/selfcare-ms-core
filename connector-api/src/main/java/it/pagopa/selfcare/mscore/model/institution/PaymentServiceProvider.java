package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
public class PaymentServiceProvider {

    private String abiCode;
    private String businessRegisterNumber;
    private String legalRegisterName;
    private String legalRegisterNumber;
    private boolean vatNumberGroup;

}
