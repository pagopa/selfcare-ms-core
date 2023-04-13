package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.web.model.onboarding.LegalsResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenMapper {

    public static TokenResponse toTokenResponse(TokenRelationships tokenRelationships){
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setId(tokenRelationships.getTokenId());
        tokenResponse.setChecksum(tokenRelationships.getChecksum());
        for(OnboardedUser user : tokenRelationships.getUsers()){
            List<LegalsResponse> list  = user.getBindings().stream()
                    .filter(userBinding -> tokenRelationships.getInstitutionId().equalsIgnoreCase(userBinding.getInstitutionId()))
                    .flatMap(userBinding -> userBinding.getProducts().stream())
                    .filter(onboardedProduct -> tokenRelationships.getProductId().equalsIgnoreCase(onboardedProduct.getProductId()))
                    .map(product -> {
                        LegalsResponse legalsResponse = new LegalsResponse();
                        legalsResponse.setPartyId(user.getId());
                        legalsResponse.setRelationshipId(product.getRelationshipId());
                        legalsResponse.setEnv(product.getEnv());
                        legalsResponse.setRole(product.getRole());
                        return legalsResponse;
                    }).collect(Collectors.toList());
            tokenResponse.getLegals().addAll(list);
        }
        return tokenResponse;
    }
}
