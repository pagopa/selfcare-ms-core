package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionToNotify {
    private InstitutionType institutionType;
    private String description;
    private String digitalAddress;
    private String address;
    private String taxCode;
    private Origin origin;
    private String originId;
}
