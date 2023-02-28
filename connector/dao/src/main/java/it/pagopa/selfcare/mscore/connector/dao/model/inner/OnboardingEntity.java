package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

@Data
@FieldNameConstants(asEnum = true)
public class OnboardingEntity {
    private String productId;
    private RelationshipState status;
    private String contract;
    private String pricingPlan;
    private Premium premium;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
