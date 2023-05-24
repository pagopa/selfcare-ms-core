package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.web.model.onboarding.LegalsResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import it.pagopa.selfcare.mscore.web.model.token.TokenResource;
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

    public static TokenResource toResource(Token model){
        TokenResource resource = null;
        if(model != null) {
            resource = new TokenResource();
            resource.setId(model.getId());
            resource.setType(model.getType());
            resource.setStatus(model.getStatus());
            resource.setInstitutionId(model.getInstitutionId());
            resource.setProductId(model.getProductId());
            resource.setExpiringDate(model.getExpiringDate());
            resource.setChecksum(model.getChecksum());
            resource.setContractVersion(model.getContractVersion());
            resource.setContractTemplate(model.getContractTemplate());
            resource.setContractSigned(model.getContractSigned());
            resource.setUsers(model.getUsers());
            resource.setInstitutionUpdate(model.getInstitutionUpdate());
            resource.setCreatedAt(model.getCreatedAt());
            resource.setUpdatedAt(model.getUpdatedAt());
            resource.setClosedAt(model.getClosedAt());
        }
        return resource;
    }
}
