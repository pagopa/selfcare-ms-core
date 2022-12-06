package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;

@Data
public class OnboardingEntity {
    private String productId;
    private String status;
    private String contract;
    private String pricingPlan;
    private OnboardingPremiumEntity premium;
}
