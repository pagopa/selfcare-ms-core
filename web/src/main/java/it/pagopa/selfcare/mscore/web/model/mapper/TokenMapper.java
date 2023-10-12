package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.web.model.onboarding.LegalsResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.TokenResponse;
import it.pagopa.selfcare.mscore.web.model.token.TokenResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = List.class)
public interface TokenMapper {
    @Mapping(target = "legals",  expression = "java(toLegalsResponse(tokenRelationships))")
    @Mapping(ignore = true, target="users")
    @Mapping(target = "id", source = "tokenId")
    TokenResponse toTokenResponse(TokenRelationships tokenRelationships);

    TokenResource toTokenResponse(Token token);
    @Named("toLegalsResponse")
    default List<LegalsResponse> toLegalsResponse( TokenRelationships tokenRelationships){
        List<LegalsResponse> legalsResponses = new ArrayList<>();
        for(OnboardedUser user: tokenRelationships.getUsers()){
            List<LegalsResponse> list = user.getBindings().stream()
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
            legalsResponses.addAll(list);
        }
        return legalsResponses;
    }

}
