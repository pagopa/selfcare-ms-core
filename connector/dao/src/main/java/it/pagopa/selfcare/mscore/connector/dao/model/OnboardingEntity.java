package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;

@Data
public class OnboardingEntity {
    private String productId;
    private RelationshipState status;
    private String contract;
    private String pricingPlan;
    private OnboardingPremiumEntity premium;
}
