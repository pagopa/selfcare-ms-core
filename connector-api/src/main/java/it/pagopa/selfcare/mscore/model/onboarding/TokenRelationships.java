package it.pagopa.selfcare.mscore.model.onboarding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRelationships {
    private String tokenId;
    private String checksum;
    private String institutionId;
    private String productId;
    private List<OnboardedUser> users;
}
