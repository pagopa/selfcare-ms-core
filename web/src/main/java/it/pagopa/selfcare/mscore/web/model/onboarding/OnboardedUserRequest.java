package it.pagopa.selfcare.mscore.web.model.onboarding;

import lombok.Data;

@Data
public class OnboardedUserRequest {
    private String id;
    private String taxCode;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String productRole;
}
