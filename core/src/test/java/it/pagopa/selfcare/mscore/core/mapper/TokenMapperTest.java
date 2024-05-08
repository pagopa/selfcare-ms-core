package it.pagopa.selfcare.mscore.core.mapper;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenMapperTest {
    @Test
    void testToToken() {
        String institutionId = "institutionId";
        String productId = "productId";

        Onboarding onboarding = new Onboarding();
        onboarding.setTokenId("id");
        onboarding.setCreatedAt(OffsetDateTime.now());
        onboarding.setUpdatedAt(OffsetDateTime.now().plusDays(1));
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setContract("contract");

        Token expectedToken = new Token();
        expectedToken.setId(onboarding.getTokenId());
        expectedToken.setInstitutionId(institutionId);
        expectedToken.setProductId(productId);
        expectedToken.setCreatedAt(onboarding.getCreatedAt());
        expectedToken.setUpdatedAt(onboarding.getUpdatedAt());
        expectedToken.setStatus(onboarding.getStatus());
        expectedToken.setContractSigned(onboarding.getContract());

        Token actualTokenResult = TokenMapper.toToken(onboarding, institutionId, productId);
        assertEquals(expectedToken, actualTokenResult);
    }
}
