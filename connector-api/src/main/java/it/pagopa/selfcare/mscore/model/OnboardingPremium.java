package it.pagopa.selfcare.mscore.model;

import lombok.Data;

@Data
public class OnboardingPremium {
    private RelationshipState status;
    private String contract;
}
