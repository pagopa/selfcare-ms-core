package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaAttributes;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    private static UserMapper userMapper = new UserMapperImpl();

    private static Contract toContract(ContractRequest request) {
        Contract contract = new Contract();
        if (request != null) {
            contract.setPath(request.getPath());
            contract.setVersion(request.getVersion());
        }
        return contract;
    }

    public static OnboardingInfoResponse toOnboardingInfoResponse(String userId, List<OnboardingInfo> onboardingInfos) {
        OnboardingInfoResponse response = new OnboardingInfoResponse();
        response.setUserId(userId);
        List<OnboardedInstitutionResponse> institutionResponseList = new ArrayList<>();
        onboardingInfos.forEach(onboardingInfo ->
                onboardingInfo.getInstitution().getOnboarding().stream()
                        .filter(onboarding -> onboarding.getProductId().equalsIgnoreCase(onboardingInfo.getBinding().getProducts().getProductId())
                                && onboarding.getStatus().equals(onboardingInfo.getBinding().getProducts().getStatus()))
                        .findFirst()
                        .ifPresent(onboarding -> institutionResponseList.add(constructOnboardedInstitutionResponse(onboardingInfo.getInstitution(),
                                onboardingInfo.getBinding().getProducts(),
                                onboarding)))
        );
        response.setInstitutions(institutionResponseList);
        return response;
    }

    private static OnboardedInstitutionResponse constructOnboardedInstitutionResponse(Institution institution, OnboardedProduct product, Onboarding onboarding) {
        OnboardedInstitutionResponse institutionResponse = new OnboardedInstitutionResponse();
        institutionResponse.setId(institution.getId());
        institutionResponse.setExternalId(institution.getExternalId());
        institutionResponse.setOrigin(institution.getOrigin());
        institutionResponse.setOriginId(institution.getOriginId());
        institutionResponse.setDescription(institution.getDescription());
        institutionResponse.setInstitutionType(institution.getInstitutionType());
        institutionResponse.setDigitalAddress(institution.getDigitalAddress());
        institutionResponse.setAddress(institution.getAddress());
        institutionResponse.setZipCode(institution.getZipCode());
        institutionResponse.setTaxCode(institution.getTaxCode());
        if (institution.getGeographicTaxonomies() != null) {
            institutionResponse.setGeographicTaxonomies(InstitutionMapperCustom.toGeoTaxonomies(institution.getGeographicTaxonomies()));
        }
        if (institution.getAttributes() != null) {
            institutionResponse.setAttributes(InstitutionMapperCustom.toAttributeResponse(institution.getAttributes()));
        }
        institutionResponse.setPricingPlan(onboarding.getPricingPlan());
        Billing billing = InstitutionMapperCustom.getBillingFromOnboarding(onboarding, institution);
        institutionResponse.setBilling(billing);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setRole(product.getProductRole());
        productInfo.setId(product.getProductId());
        productInfo.setCreatedAt(product.getCreatedAt());
        institutionResponse.setState(product.getStatus().name());
        institutionResponse.setRole(product.getRole());
        institutionResponse.setProductInfo(productInfo);
        institutionResponse.setBusinessData(new BusinessData(institution.getRea(), institution.getShareCapital(), institution.getBusinessRegisterPlace()));
        institutionResponse.setSupportContact(new SupportContact(institution.getSupportEmail(), institution.getSupportPhone()));
        institutionResponse.setPaymentServiceProvider(InstitutionMapperCustom.toPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        institutionResponse.setDataProtectionOfficer(InstitutionMapperCustom.toDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        institutionResponse.setParentDescription(institution.getParentDescription());
        institutionResponse.setRootParentId(institution.getRootParentId());
        institutionResponse.setSubunitCode(institution.getSubunitCode());
        institutionResponse.setSubunitType(institution.getSubunitType());
        institutionResponse.setAooParentCode(Optional.ofNullable(institution.getPaAttributes()).map(PaAttributes::getAooParentCode).orElse(null));

        return institutionResponse;
    }

    public static OnboardingOperatorsRequest toOnboardingOperatorRequest(OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest) {
        OnboardingOperatorsRequest request = new OnboardingOperatorsRequest();
        request.setInstitutionId(onboardingInstitutionOperatorsRequest.getInstitutionId());
        request.setProductId(onboardingInstitutionOperatorsRequest.getProductId());
        request.setProductTitle(onboardingInstitutionOperatorsRequest.getProductTitle());
        request.setUsers(Optional.ofNullable(onboardingInstitutionOperatorsRequest.getUsers())
                .map(list -> list.stream().map(userMapper::toUserToOnboard).collect(Collectors.toList()))
                .orElse(List.of())
        );
        return request;
    }

    public static OnboardingLegalsRequest toOnboardingLegalsRequest(OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest) {
        OnboardingLegalsRequest request = new OnboardingLegalsRequest();
        request.setTokenType(TokenType.LEGALS);
        request.setProductId(onboardingInstitutionLegalsRequest.getProductId());
        request.setProductName(onboardingInstitutionLegalsRequest.getProductName());
        request.setUsers(Optional.ofNullable(onboardingInstitutionLegalsRequest.getUsers())
                .map(list -> list.stream().map(userMapper::toUserToOnboard).collect(Collectors.toList()))
                .orElse(List.of())
        );
        request.setInstitutionExternalId(onboardingInstitutionLegalsRequest.getInstitutionExternalId());
        request.setInstitutionId(onboardingInstitutionLegalsRequest.getInstitutionId());
        request.setContract(toContract(onboardingInstitutionLegalsRequest.getContract()));
        request.setSignContract(onboardingInstitutionLegalsRequest.isSignContract());
        return request;
    }

}
