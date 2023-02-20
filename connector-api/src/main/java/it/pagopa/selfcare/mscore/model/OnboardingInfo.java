package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.Data;

import java.util.Map;

@Data
public class OnboardingInfo {
    Institution institution;
    Map<String, OnboardedProduct> onboardedProducts;

    public OnboardingInfo(Institution onboardedInstitution, Map<String, OnboardedProduct> onboardedProducts) {
        this.institution = onboardedInstitution;
        this.onboardedProducts = onboardedProducts;
    }
}
