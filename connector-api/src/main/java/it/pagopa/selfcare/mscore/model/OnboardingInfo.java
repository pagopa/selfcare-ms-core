package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OnboardingInfo {
    Institution institution;
    List<Onboarding>  onboardingList;
    Map<String, Product> productMap;

    public OnboardingInfo(Institution onboardedInstitution, List<Onboarding> onboardingList, Map<String, Product> productMap) {
        this.institution = onboardedInstitution;
        this.onboardingList = onboardingList;
        this.productMap = productMap;
    }
}
