package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesResponse;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingRequest toOnboardingRequest(OnboardingInstitutionRequest onboardingInstitutionRequest) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setProductId(onboardingInstitutionRequest.getProductId());
        onboardingRequest.setProductName(onboardingInstitutionRequest.getProductName());
        onboardingRequest.setInstitutionExternalId(onboardingInstitutionRequest.getInstitutionExternalId());
        onboardingRequest.setPricingPlan(onboardingInstitutionRequest.getPricingPlan());

        if(onboardingInstitutionRequest.getBillingRequest()!=null)
            onboardingRequest.setBillingRequest(convertToBilling(onboardingInstitutionRequest.getBillingRequest()));
        if(onboardingInstitutionRequest.getContract()!=null)
            onboardingRequest.setContract(convertToContract(onboardingInstitutionRequest.getContract()));
        if(onboardingInstitutionRequest.getUsers()!=null)
            onboardingRequest.setUsers(convertToOnboarderUser(onboardingInstitutionRequest));
        if(onboardingInstitutionRequest.getInstitutionUpdate()!=null)
            onboardingRequest.setInstitutionUpdate(onboardingInstitutionRequest.getInstitutionUpdate());

        return onboardingRequest;
    }

    private static Contract convertToContract(ContractRequest request) {
        Contract contract = new Contract();
        contract.setPath(request.getPath());
        contract.setVersion(request.getVersion());
        return contract;
    }

    private static List<UserToOnboard> convertToOnboarderUser(OnboardingInstitutionRequest onboardingInstitutionRequest) {
        List<UserToOnboard> onboardedUsers = new ArrayList<>();
        if(!onboardingInstitutionRequest.getUsers().isEmpty()){
            for(Person p: onboardingInstitutionRequest.getUsers()) {
                UserToOnboard onboardedUser = new UserToOnboard();
                onboardedUser.setId(p.getId());
                onboardedUser.setName(p.getName());
                onboardedUser.setSurname(p.getSurname());
                onboardedUser.setTaxCode(p.getTaxCode());
                onboardedUser.setEmail(p.getEmail());
                onboardedUser.setRole(PartyRole.valueOf(p.getRole()));
                onboardedUser.setProductRole(List.of(p.getRole(),p.getProductRole()));
                onboardedUsers.add(onboardedUser);
            }
        }
        return onboardedUsers;
    }

    private static Billing convertToBilling(BillingRequest billingRequest) {
        Billing billing = new Billing();
        billing.setRecipientCode(billingRequest.getRecipientCode());
        billing.setVatNumber(billingRequest.getVatNumber());
        billing.setPublicServices(billing.isPublicServices());
        return billing;
    }

    public static OnboardingInfoResponse toOnboardingInfoResponse(String userId, List<OnboardingInfo> onboardingInfos) {
        OnboardingInfoResponse response = new OnboardingInfoResponse();
        response.setUserId(userId);
        List<OnboardedInstitutionResponse> institutionResponseList = new ArrayList<>();
        for(OnboardingInfo onboardingInfo : onboardingInfos) {
            Institution institution = onboardingInfo.getInstitution();
            Map<String, OnboardedProduct> productMap = onboardingInfo.getProductMap();
            List<Onboarding> onboardingList = institution.getOnboarding();

            onboardingList.forEach(onboarding -> {
                String productId = onboarding.getProductId();
                OnboardedProduct onboardedProduct = productMap.get(productId);
                ProductInfo productInfo = convertToProductInfo(onboardedProduct, productId);

                Billing billing = getBillingFromOnboarding(onboarding, institution);
                OnboardedInstitutionResponse institutionResponse = new OnboardedInstitutionResponse();

                institutionResponse.setId(institution.getId());
                institutionResponse.setExternalId(institution.getExternalId());
                institutionResponse.setOriginId(institution.getIpaCode());
                institutionResponse.setDescription(institution.getDescription());
                institutionResponse.setInstitutionType(institution.getInstitutionType());
                institutionResponse.setDigitalAddress(institution.getDigitalAddress());
                institutionResponse.setAddress(institution.getAddress());
                institutionResponse.setZipCode(institution.getZipCode());
                institutionResponse.setTaxCode(institution.getTaxCode());
                institutionResponse.setPricingPlan(onboarding.getPricingPlan());
                institutionResponse.setBilling(billing);
                institutionResponse.setGeographicTaxonomies(convertToGeoTaxonomies(institution.getGeographicTaxonomies()));
                institutionResponse.setAttributes(convertToAttributesResponse(institution.getAttributes()));
                institutionResponse.setState(onboarding.getStatus().toString());
                //institutionResponse.setRole(onboarding.getRole());
                institutionResponse.setProductInfo(productInfo);
                institutionResponseList.add(institutionResponse);
            });
        }
        response.setInstitutions(institutionResponseList);
        return response;
    }

    private static Billing getBillingFromOnboarding(Onboarding onboarding, Institution institution) {
        return onboarding.getBilling() != null ? onboarding.getBilling() : institution.getBilling();
    }

    private static ProductInfo convertToProductInfo(OnboardedProduct onboardedProduct, String productId) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(productId);
        productInfo.setRole(onboardedProduct.getRoles());
        productInfo.setCreatedAt(onboardedProduct.getCreatedAt());
        return productInfo;
    }

    private static List<AttributesResponse> convertToAttributesResponse(List<Attributes> attributesList) {
        if(attributesList == null) {
            return Collections.emptyList();
        }

        return attributesList.stream()
                .map((attribute -> {
                    AttributesResponse response = new AttributesResponse();
                    response.setCode(attribute.getCode());
                    response.setOrigin(attribute.getOrigin());
                    response.setDescription(attribute.getDescription());
                    return response;
                 })
                ).collect(Collectors.toList());
    }

    private static List<GeoTaxonomies> convertToGeoTaxonomies(List<GeographicTaxonomies> geographicTaxonomies) {
        if(geographicTaxonomies == null) {
            return Collections.emptyList();
        }

        return geographicTaxonomies.stream()
                .map(geo -> {
                    GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
                    geoTaxonomies.setCode(geo.getCode());
                    geoTaxonomies.setDesc(geo.getDesc());
                    geoTaxonomies.setEnable(geo.isEnable());
                    return geoTaxonomies;
                }).collect(Collectors.toList());
    }
}
