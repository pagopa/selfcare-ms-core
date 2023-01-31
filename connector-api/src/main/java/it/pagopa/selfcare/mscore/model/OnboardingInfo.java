package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.Data;

import java.util.Map;

@Data
public class OnboardingInfo {
    Institution institution;
    Map<String, Product> productMap;

    public OnboardingInfo(Institution onboardedInstitution, Map<String, Product> productMap) {
        this.institution = onboardedInstitution;
        this.productMap = productMap;
    }
}
