package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OnboardingResponse {

    private String productId;
    private String tokenId;
    private RelationshipResponse status;
    private String contract;
    private String pricingPlan;
    private BillingResponse billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
}
