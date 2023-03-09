package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
public class DataProtectionOfficer {

    private String address;
    private String email;
    private String pec;

}
