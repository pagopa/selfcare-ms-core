package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingRequest toOnboardingRequest(OnboardingInstitutionRequest onboardingInstitutionRequest) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setProductId(onboardingInstitutionRequest.getProductId());
        onboardingRequest.setProductName(onboardingInstitutionRequest.getProductName());
        onboardingRequest.setInstitutionExternalId(onboardingInstitutionRequest.getInstitutionExternalId());
        onboardingRequest.setPricingPlan(onboardingInstitutionRequest.getPricingPlan());

        if (onboardingInstitutionRequest.getBilling() != null)
            onboardingRequest.setBillingRequest(InstitutionMapper.toBilling(onboardingInstitutionRequest.getBilling()));
        if (onboardingInstitutionRequest.getContract() != null)
            onboardingRequest.setContract(toContract(onboardingInstitutionRequest.getContract()));
        if (onboardingInstitutionRequest.getUsers() != null)
            onboardingRequest.setUsers(UserMapper.toUserToOnboard(onboardingInstitutionRequest.getUsers()));
        if (onboardingInstitutionRequest.getInstitutionUpdate() != null)
            onboardingRequest.setInstitutionUpdate(onboardingInstitutionRequest.getInstitutionUpdate());

        return onboardingRequest;
    }

    private static ContractImported toContractImported(ContractImportedRequest contractImported) {
        ContractImported response = new ContractImported();
        response.setContractType(contractImported.getContractType());
        response.setFilePath(contractImported.getFilePath());
        response.setFileName(contractImported.getFileName());
        return response;
    }

    private static Contract toContract(ContractRequest request) {
        Contract contract = new Contract();
        contract.setPath(request.getPath());
        contract.setVersion(request.getVersion());
        return contract;
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
        institutionResponse.setGeographicTaxonomies(InstitutionMapper.toGeoTaxonomies(institution.getGeographicTaxonomies()));
        institutionResponse.setAttributes(InstitutionMapper.toAttributeResponse(institution.getAttributes()));
        institutionResponse.setPricingPlan(onboarding.getPricingPlan());
        Billing billing = InstitutionMapper.getBillingFromOnboarding(onboarding, institution);
        institutionResponse.setBilling(billing);
        ProductInfo productInfo = new ProductInfo();
        productInfo.setRoles(product.getProductRoles());
        productInfo.setId(product.getProductId());
        productInfo.setCreatedAt(product.getCreatedAt());
        institutionResponse.setState(product.getStatus().name());
        institutionResponse.setRole(product.getRole());
        institutionResponse.setProductInfo(productInfo);
        institutionResponse.setRea(institution.getRea());
        institutionResponse.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionResponse.setShareCapital(institution.getShareCapital());
        institutionResponse.setSupportEmail(institution.getSupportEmail());
        institutionResponse.setSupportPhone(institution.getSupportPhone());
        return institutionResponse;
    }
    public static OnboardingOperatorsRequest toOnboardingOperatorRequest(OnboardingInstitutionOperatorsRequest onboardingInstitutionOperatorsRequest) {
        OnboardingOperatorsRequest request = new OnboardingOperatorsRequest();
        request.setInstitutionId(onboardingInstitutionOperatorsRequest.getInstitutionId());
        request.setProductId(onboardingInstitutionOperatorsRequest.getProductId());
        request.setUsers(UserMapper.toUserToOnboard(onboardingInstitutionOperatorsRequest.getUsers()));
        return request;
    }

    public static OnboardingLegalsRequest toOnboardingLegalsRequest(OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest) {
        OnboardingLegalsRequest request = new OnboardingLegalsRequest();
        request.setProductId(onboardingInstitutionLegalsRequest.getProductId());
        request.setProductName(onboardingInstitutionLegalsRequest.getProductName());
        request.setUsers(UserMapper.toUserToOnboard(onboardingInstitutionLegalsRequest.getUsers()));
        request.setInstitutionExternalId(onboardingInstitutionLegalsRequest.getInstitutionExternalId());
        request.setInstitutionId(onboardingInstitutionLegalsRequest.getInstitutionId());
        request.setContract(toContract(onboardingInstitutionLegalsRequest.getContract()));
        request.setSignContract(onboardingInstitutionLegalsRequest.isSignContract());
        return request;
    }
}
