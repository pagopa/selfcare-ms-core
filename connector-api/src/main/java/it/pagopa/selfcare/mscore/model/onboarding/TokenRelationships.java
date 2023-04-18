package it.pagopa.selfcare.mscore.model.onboarding;

import lombok.Data;

import java.util.List;

@Data
public class TokenRelationships {
    private String tokenId;
    private String checksum;
    private String institutionId;
    private String productId;
    private List<OnboardedUser> users;
}
