package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnboardingRollback {
    private String tokenId;
    private Onboarding onboarding;
    private Map<String, OnboardedProduct> productMap;

}
