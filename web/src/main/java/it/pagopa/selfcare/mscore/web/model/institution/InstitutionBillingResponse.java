package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.utils.OriginEnum;
import lombok.Data;

@Data
public class InstitutionBillingResponse {
    private String institutionId;
    private String externalId;
    private OriginEnum origin;
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
