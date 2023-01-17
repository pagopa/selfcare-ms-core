package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

@Data
public class InstitutionBillingResponse {
    private String institutionId;
    private String externalId;
    private String origin;

    private String ipaCode;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private String pricingPlan;
    private BillingResponse billing;
}
