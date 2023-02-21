package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import lombok.Data;

@Data
public class InstitutionBillingResponse {
    private String institutionId;
    private String externalId;
    private String originId;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private String pricingPlan;
    private BillingResponse billing;
}
