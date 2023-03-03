package it.pagopa.selfcare.mscore.model.onboarding;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

@Data
public class TokenUser {
    private String userId;
    private PartyRole role;
}
