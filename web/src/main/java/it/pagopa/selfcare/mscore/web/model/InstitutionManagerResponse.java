package it.pagopa.selfcare.mscore.web.model;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InstitutionManagerResponse {
    private String id;
    private String from;
    private String to;
    private String role;
    private ProductInfo productInfo;
    private RelationshipState state;
    private String pricingPlan;
    private InstitutionUpdate institutionUpdate;
    private BillingResponse billingResponse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
