package it.pagopa.selfcare.mscore.core.mapper;

import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenMapper {
    public static Token toToken(Onboarding onboarding, String institutionId, String productId) {
        Token token = new Token();
        token.setId(onboarding.getTokenId());
        token.setInstitutionId(institutionId);
        token.setProductId(productId);
        token.setCreatedAt(onboarding.getCreatedAt());
        token.setUpdatedAt(onboarding.getUpdatedAt());
        token.setStatus(onboarding.getStatus());
        token.setContractSigned(onboarding.getContract());
        return token;
    }
}
