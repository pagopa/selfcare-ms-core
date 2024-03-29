package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenUtils {

    public static Token toToken(OnboardingRequest request, Institution institution, String digest, OffsetDateTime expiringDate) {
        log.info("START - convertToToken for institution having externalId: {} and digest: {}", institution.getExternalId(), digest);
        OffsetDateTime activatedAt = Objects.nonNull(request.getContractActivatedAt()) ? request.getContractActivatedAt() : null;
        Token token = new Token();
        if (request.getContract() != null) {
            token.setContractTemplate(request.getContract().getPath());
        }
        token.setCreatedAt(OffsetDateTime.now());
        token.setUpdatedAt(OffsetDateTime.now());
        token.setActivatedAt(activatedAt);
        token.setInstitutionId(institution.getId());
        token.setProductId(request.getProductId());
        token.setChecksum(digest);
        token.setStatus(OnboardingInstitutionUtils.getStatus(request.getInstitutionUpdate(),
                institution.getInstitutionType(), institution.getOrigin(), request.getProductId()));
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

    public static TokenRelationships toTokenRelationships(Token token, List<OnboardedUser> users) {
        TokenRelationships tokenRelationships = new TokenRelationships();
        tokenRelationships.setChecksum(token.getChecksum());
        tokenRelationships.setTokenId(token.getId());
        tokenRelationships.setInstitutionId(token.getInstitutionId());
        tokenRelationships.setProductId(token.getProductId());
        tokenRelationships.setUsers(users);
        tokenRelationships.setType(token.getType());
        tokenRelationships.setStatus(token.getStatus());
        tokenRelationships.setClosedAt(token.getDeletedAt());
        tokenRelationships.setCreatedAt(token.getCreatedAt());
        tokenRelationships.setActivatedAt(token.getActivatedAt());
        tokenRelationships.setUpdatedAt(token.getUpdatedAt());
        tokenRelationships.setContentType(token.getContentType());
        tokenRelationships.setContractSigned(token.getContractSigned());
        tokenRelationships.setInstitutionUpdate(token.getInstitutionUpdate());
        return tokenRelationships;
    }
}
