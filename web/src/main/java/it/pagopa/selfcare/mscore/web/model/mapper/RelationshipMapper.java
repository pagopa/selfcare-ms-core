package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class RelationshipMapper {

    public static RelationshipResponse toRelationshipResult(Token token, Institution institution) {
        RelationshipResponse response = new RelationshipResponse();
        response.setId(token.getId());
       // response.setFrom(token.getUsers());
        response.setProduct(token.getProductId());
        response.setState(token.getStatus());
        response.setCreatedAt(token.getCreatedAt());
        response.setUpdatedAt(token.getUpdatedAt());
        if (institution != null) {
            response.setTo(institution.getId());
            response.setInstitutionUpdate(InstitutionMapper.toInstitutionUpdateResponse(institution));

            if (institution.getOnboarding() != null) {
                Onboarding onboarding = institution.getOnboarding().stream()
                        .filter(o -> token.getProductId().equalsIgnoreCase(o.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new InvalidRequestException("", ""));
                response.setPricingPlan(onboarding.getPricingPlan());
                response.setBillingResponse(InstitutionMapper.toBillingResponse(onboarding, institution));
            }
        }
        return response;
    }

    public static RelationshipResult toRelationshipResult(RelationshipInfo info) {

        RelationshipResult relationshipResult = new RelationshipResult();
        if (info.getOnboardedProduct() != null) {
            relationshipResult.setId(info.getOnboardedProduct().getRelationshipId());
            relationshipResult.setState(info.getOnboardedProduct().getStatus());
            relationshipResult.setRole(info.getOnboardedProduct().getRole());

            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(info.getOnboardedProduct().getProductId());
            productInfo.setCreatedAt(info.getOnboardedProduct().getCreatedAt());
            productInfo.setRole(info.getOnboardedProduct().getProductRole());

            relationshipResult.setProduct(productInfo);
        }
        relationshipResult.setFrom(info.getUserId());
        relationshipResult.setTo(info.getInstitution().getId());

        if (info.getInstitution() != null) {
            relationshipResult.setInstitutionUpdate(InstitutionMapper.toInstitutionUpdateResponse(info.getInstitution()));
        }

        addInstitutionOnboardingData(info, relationshipResult);
        return relationshipResult;
    }

    private static void addInstitutionOnboardingData(RelationshipInfo info, RelationshipResult relationshipResult) {
        for (Onboarding onboarding : info.getInstitution().getOnboarding()) {
            if (onboarding.getProductId().equalsIgnoreCase(info.getOnboardedProduct().getProductId())) {
                relationshipResult.setPricingPlan(onboarding.getPricingPlan());
                relationshipResult.setBilling(InstitutionMapper.toBillingResponse(onboarding, info.getInstitution()));
            }
        }
    }

    public static List<RelationshipResult> toRelationshipResultList(List<RelationshipInfo> relationshipInfoList) {
        List<RelationshipResult> relationshipResults = new ArrayList<>();
        for(RelationshipInfo info : relationshipInfoList){
            relationshipResults.add(toRelationshipResult(info));
        }
        return relationshipResults;
    }
}
