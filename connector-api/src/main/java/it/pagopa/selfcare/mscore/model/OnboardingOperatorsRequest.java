package it.pagopa.selfcare.mscore.model;

import lombok.Data;
import java.util.List;

@Data
public class OnboardingOperatorsRequest {

    private String productId;
    private List<UserToOnboard> users;
    private String institutionId;

}
