package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.ProductRelationship;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingResponse;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResponse;
import it.pagopa.selfcare.mscore.web.model.institution.RelationshipResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class RelationshipMapper {
    public static RelationshipResponse toRelationshipResponse(Token token, Institution institution) {
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
                .filter(o-> token.getProductId().equalsIgnoreCase(o.getProductId()))
                .findFirst().orElseThrow(() -> new InvalidRequestException("", ""));

        response.setPricingPlan(onboarding.getPricingPlan());
        response.setBillingResponse(toBillingResponse(onboarding, institution));
        return response;
    }

    public static BillingResponse toBillingResponse(Onboarding onboarding, Institution institution) {
        BillingResponse billingResponse = new BillingResponse();
        if(onboarding.getBilling()!=null){
            billingResponse.setVatNumber(onboarding.getBilling().getVatNumber());
            billingResponse.setRecipientCode(onboarding.getBilling().getRecipientCode());
            billingResponse.setPublicServices(onboarding.getBilling().isPublicServices());
        }else if(institution.getBilling() != null ){
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

    public static RelationshipResult toRelationshipInfo(ProductRelationship relationship) {
        RelationshipResult relationshipInfo = new RelationshipResult();

        return relationshipInfo;
    }

}
