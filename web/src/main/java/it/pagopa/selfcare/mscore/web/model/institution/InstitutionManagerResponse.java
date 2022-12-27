package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.model.Product;
import lombok.Data;


@Data
public class InstitutionManagerResponse {
    private String id;
    private String from;
    private String to;
    private Product product;
    private String state;
    private String pricingPlan;
    private BillingRequest billingRequest;
    private String role;
    private String createdAt;
    private String updatedAt;
    private InstitutionUpdateRequest institutionUpdateRequest;

}
