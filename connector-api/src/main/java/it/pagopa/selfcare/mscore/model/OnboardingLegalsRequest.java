package it.pagopa.selfcare.mscore.model;

import lombok.Data;
import java.util.List;

@Data
public class OnboardingLegalsRequest {

    private String productId;
    private String productName;
    private List<UserToOnboard> users;
    private String institutionExternalId;
    private String institutionId;
    private Contract contract;
    private boolean signContract;
}
