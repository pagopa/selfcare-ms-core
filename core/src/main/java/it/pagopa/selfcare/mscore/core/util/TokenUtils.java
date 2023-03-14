package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenUtils {

    public static Token toToken(OnboardingRequest request, Institution institution, String digest, OffsetDateTime expiringDate) {
        log.info("START - convertToToken for institution having externalId: {} and digest: {}", institution.getExternalId(), digest);
        Token token = new Token();
        if (request.getContract() != null) {
            token.setContractTemplate(request.getContract().getPath());
        }
        token.setCreatedAt(OffsetDateTime.now());
        token.setInstitutionId(institution.getId());
        token.setProductId(request.getProductId());
        token.setChecksum(digest);
        if (request.getInstitutionUpdate() != null) {
            token.setStatus(OnboardingInstitutionUtils.getStatus(request.getInstitutionUpdate().getInstitutionType()));
        } else if (institution.getInstitutionType() != null) {
            token.setStatus(OnboardingInstitutionUtils.getStatus(institution.getInstitutionType()));
        }
        token.setInstitutionUpdate(request.getInstitutionUpdate());
        token.setUsers(request.getUsers().stream().map(TokenUtils::toTokenUser).collect(Collectors.toList()));
        token.setExpiringDate(expiringDate);
        token.setType(request.getTokenType());
        log.info("END - convertToToken");
        return token;
    }

    public static TokenUser toTokenUser(UserToOnboard userToOnboard) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(userToOnboard.getId());
        tokenUser.setRole(userToOnboard.getRole());
        return tokenUser;
    }
}
