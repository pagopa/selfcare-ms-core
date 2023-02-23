package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class RelationshipMapper {
    public static RelationshipResponse toRelationshipResult(Token token, Institution institution) {
        RelationshipResponse response = new RelationshipResponse();
        response.setId(token.getId());
        response.setTo(institution.getId());
        response.setFrom(token.getUsers());
        response.setProduct(token.getProductId());
        response.setState(token.getStatus());
        response.setCreatedAt(token.getCreatedAt());
        response.setUpdatedAt(token.getUpdatedAt());
        response.setInstitutionUpdate(toInstitutionUpdate(institution));

        Onboarding onboarding = institution.getOnboarding().stream()
                .filter(o -> token.getProductId().equalsIgnoreCase(o.getProductId()))
                .findFirst().orElseThrow(() -> new InvalidRequestException("", ""));

        response.setPricingPlan(onboarding.getPricingPlan());
        response.setBillingResponse(toBillingResponse(onboarding, institution));
        return response;
    }

    public static BillingResponse toBillingResponse(Onboarding onboarding, Institution institution) {
        BillingResponse billingResponse = new BillingResponse();
        if (onboarding.getBilling() != null) {
            billingResponse.setVatNumber(onboarding.getBilling().getVatNumber());
            billingResponse.setRecipientCode(onboarding.getBilling().getRecipientCode());
            billingResponse.setPublicServices(onboarding.getBilling().isPublicServices());
        } else if (institution.getBilling() != null) {
            billingResponse.setVatNumber(institution.getBilling().getVatNumber());
            billingResponse.setRecipientCode(institution.getBilling().getRecipientCode());
            billingResponse.setPublicServices(institution.getBilling().isPublicServices());
        }
        return billingResponse;
    }

    private static InstitutionUpdateResponse toInstitutionUpdate(Institution institution) {
        InstitutionUpdateResponse institutionUpdate = new InstitutionUpdateResponse();
        institutionUpdate.setAddress(institution.getAddress());
        institutionUpdate.setInstitutionType(institution.getInstitutionType());
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setDigitalAddress(institution.getDigitalAddress());
        institutionUpdate.setTaxCode(institution.getTaxCode());
        institutionUpdate.setZipCode(institution.getZipCode());
        institutionUpdate.setPaymentServiceProvider(institution.getPaymentServiceProvider());
        institutionUpdate.setDataProtectionOfficer(institution.getDataProtectionOfficer());
        institutionUpdate.setGeographicTaxonomyCodes(institution.getGeographicTaxonomies().stream()
                .map(GeographicTaxonomies::getCode).collect(Collectors.toList()));
        institutionUpdate.setRea(institution.getRea());
        institutionUpdate.setShareCapital(institution.getShareCapital());
        institutionUpdate.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionUpdate.setSupportEmail(institution.getSupportEmail());
        institutionUpdate.setSupportPhone(institution.getSupportPhone());
        institution.setImported(institution.isImported());
        return institutionUpdate;
    }

    public static RelationshipResult toRelationshipResult(RelationshipInfo info) {

        RelationshipResult relationshipResult = new RelationshipResult();
        relationshipResult.setId(info.getOnboardedProduct().getRelationshipId());
        relationshipResult.setFrom(info.getUserId());
        relationshipResult.setTo(info.getInstitution().getId());

        relationshipResult.setState(info.getOnboardedProduct().getStatus().name());
        relationshipResult.setRole(info.getOnboardedProduct().getRole());

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(info.getOnboardedProduct().getProductId());
        productInfo.setCreatedAt(info.getOnboardedProduct().getCreatedAt());
        productInfo.setRoles(info.getOnboardedProduct().getProductRoles());

        relationshipResult.setProduct(productInfo);
        relationshipResult.setInstitutionUpdate(constructInstitutionUpdate(info.getInstitution()));

        addInstitutionOnboardingData(info, relationshipResult);
        return relationshipResult;
    }

    private static void addInstitutionOnboardingData(RelationshipInfo info, RelationshipResult relationshipResult) {
        for (Onboarding onboarding : info.getInstitution().getOnboarding()) {
            if (onboarding.getProductId().equalsIgnoreCase(info.getOnboardedProduct().getProductId())) {
                relationshipResult.setPricingPlan(onboarding.getPricingPlan());
                relationshipResult.setBilling(RelationshipMapper.toBillingResponse(onboarding, info.getInstitution()));
            }
        }
    }

    private static InstitutionUpdateResponse constructInstitutionUpdate(Institution institution) {
        InstitutionUpdateResponse institutionUpdate = new InstitutionUpdateResponse();
        institutionUpdate.setInstitutionType(institution.getInstitutionType());
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setDigitalAddress(institution.getDigitalAddress());
        institutionUpdate.setAddress(institution.getAddress());
        institutionUpdate.setTaxCode(institution.getTaxCode());
        institutionUpdate.setZipCode(institution.getZipCode());
        institutionUpdate.setPaymentServiceProvider(institution.getPaymentServiceProvider());
        institutionUpdate.setDataProtectionOfficer(institution.getDataProtectionOfficer());
        institutionUpdate.setGeographicTaxonomyCodes(institution.getGeographicTaxonomies().stream().map(GeographicTaxonomies::getCode).collect(Collectors.toList()));
        institutionUpdate.setRea(institution.getRea());
        institutionUpdate.setShareCapital(institution.getShareCapital());
        institutionUpdate.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionUpdate.setSupportEmail(institution.getSupportEmail());
        institutionUpdate.setSupportPhone(institution.getSupportPhone());
        institutionUpdate.setImported(institution.isImported());
        return institutionUpdate;
    }

    public static List<RelationshipResult> toRelationshipResultList(List<RelationshipInfo> relationshipInfoList) {
        List<RelationshipResult> relationshipResults = new ArrayList<>();
        for(RelationshipInfo info : relationshipInfoList){
            relationshipResults.add(toRelationshipResult(info));
        }
        return relationshipResults;
    }
}
