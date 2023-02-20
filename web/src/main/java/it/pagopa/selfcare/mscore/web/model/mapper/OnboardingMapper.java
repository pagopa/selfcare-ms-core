package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.AttributesResponse;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.GeoTaxonomies;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingRequest toOnboardingRequest(OnboardingInstitutionRequest onboardingInstitutionRequest) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setProductId(onboardingInstitutionRequest.getProductId());
        onboardingRequest.setProductName(onboardingInstitutionRequest.getProductName());
        onboardingRequest.setInstitutionExternalId(onboardingInstitutionRequest.getInstitutionExternalId());
        onboardingRequest.setPricingPlan(onboardingInstitutionRequest.getPricingPlan());

        if (onboardingInstitutionRequest.getBillingRequest() != null)
            onboardingRequest.setBillingRequest(convertToBilling(onboardingInstitutionRequest.getBillingRequest()));
        if (onboardingInstitutionRequest.getContract() != null)
            onboardingRequest.setContract(convertToContract(onboardingInstitutionRequest.getContract()));
        if (onboardingInstitutionRequest.getUsers() != null)
            onboardingRequest.setUsers(convertToOnboarderUser(onboardingInstitutionRequest.getUsers()));
        if (onboardingInstitutionRequest.getInstitutionUpdate() != null)
            onboardingRequest.setInstitutionUpdate(onboardingInstitutionRequest.getInstitutionUpdate());

        return onboardingRequest;
    }

    private static Contract convertToContract(ContractRequest request) {
        Contract contract = new Contract();
        contract.setPath(request.getPath());
        contract.setVersion(request.getVersion());
        return contract;
    }

    private static List<UserToOnboard> convertToOnboarderUser(List<Person> personList) {
        List<UserToOnboard> users = new ArrayList<>();
        if (!personList.isEmpty()) {
            for (Person p : personList) {
                UserToOnboard userToOnboard = new UserToOnboard();
                userToOnboard.setId(p.getId());
                userToOnboard.setName(p.getName());
                userToOnboard.setSurname(p.getSurname());
                userToOnboard.setTaxCode(p.getTaxCode());
                userToOnboard.setEmail(p.getEmail());
                userToOnboard.setRole(p.getRole());
                userToOnboard.setProductRole(p.getProductRole());
                userToOnboard.setEnv(p.getEnv());
                users.add(userToOnboard);
            }
        }
        return users;
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
        for (OnboardingInfo onboardingInfo : onboardingInfos) {
            Institution institution = onboardingInfo.getInstitution();
            List<Onboarding> onboardingList = institution.getOnboarding();
            for (Onboarding onboarding : onboardingList) {
                OnboardedProduct product = onboardingInfo.getOnboardedProducts().get(onboarding.getProductId());
                if (product != null) {
                    institutionResponseList.add(constructOnboardedInstitutionResponse(institution, product, onboarding));
                }
            }
        }
        response.setInstitutions(institutionResponseList);
        return response;
    }

    private static OnboardedInstitutionResponse constructOnboardedInstitutionResponse(Institution institution, OnboardedProduct product, Onboarding onboarding) {
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
        institutionResponse.setGeographicTaxonomies(convertToGeoTaxonomies(institution.getGeographicTaxonomies()));
        institutionResponse.setAttributes(convertToAttributesResponse(institution.getAttributes()));
        institutionResponse.setPricingPlan(onboarding.getPricingPlan());
        Billing billing = getBillingFromOnboarding(onboarding, institution);
        institutionResponse.setBilling(billing);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setRole(product.getProductRoles());
        productInfo.setId(product.getProductId());
        productInfo.setCreatedAt(product.getCreatedAt());
        institutionResponse.setState(product.getStatus().name());
        institutionResponse.setRole(product.getRole());
        institutionResponse.setProductInfo(productInfo);
        return institutionResponse;
    }

    private static Billing getBillingFromOnboarding(Onboarding onboarding, Institution institution) {
        return onboarding.getBilling() != null ? onboarding.getBilling() : institution.getBilling();
    }

    private static List<AttributesResponse> convertToAttributesResponse(List<Attributes> attributesList) {
        if (attributesList == null) {
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
        if (geographicTaxonomies == null) {
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
