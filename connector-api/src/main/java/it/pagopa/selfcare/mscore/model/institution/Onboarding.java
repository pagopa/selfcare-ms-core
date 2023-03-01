package it.pagopa.selfcare.mscore.model.institution;

import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

@Data
@FieldNameConstants(asEnum = true)
public class Onboarding {

    private String productId;
    private RelationshipState status;
    private String contract;
    private String pricingPlan;
    private Premium premium;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
