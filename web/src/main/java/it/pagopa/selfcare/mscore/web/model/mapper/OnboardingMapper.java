package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingData;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedUserRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    public static OnboardingData fromDto(OnboardingRequest dto) {
       OnboardingData onboardingData = new OnboardingData();
       onboardingData.setBilling(mapBilling(dto.getBillingRequest()));
       onboardingData.setUsers(mapUsers(dto.getUsers()));
       onboardingData.setInstitutionUpdate(mapInstitutionUpdate(dto.getInstitutionUpdateRequest()));
       onboardingData.setContractPath(dto.getContract().getPath());
       onboardingData.setProductId(dto.getProductId());
       onboardingData.setProductName(dto.getProductName());
       onboardingData.setInstitutionExternalId(dto.getInstitutionExternalId());
       onboardingData.setPricingPlan(dto.getPricingPlan());
       return onboardingData;
    }

    private static InstitutionUpdate mapInstitutionUpdate(InstitutionUpdateRequest request) {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(InstitutionType.valueOf(String.valueOf(request.getInstitutionType())));
        institutionUpdate.setAddress(request.getAddress());
        institutionUpdate.setDescription(request.getDescription());
        institutionUpdate.setTaxCode(request.getTaxCode());
        institutionUpdate.setZipCode(request.getZipCode());
        institutionUpdate.setDataProtectionOfficer(mapDataProtectionOfficer(request.getDataProtectionOfficer()));
        institutionUpdate.setPaymentServiceProvider(mapPaymentServiceProvider(request.getPaymentServiceProvider()));
        return institutionUpdate;
    }

    private static PaymentServiceProvider mapPaymentServiceProvider(it.pagopa.selfcare.mscore.web.model.institution.PaymentServiceProvider paymentServiceProvider) {
        return new PaymentServiceProvider();
    }

    private static DataProtectionOfficer mapDataProtectionOfficer(it.pagopa.selfcare.mscore.web.model.institution.DataProtectionOfficer dataProtectionOfficer) {
        return new DataProtectionOfficer();
    }

    private static List<OnboardedUser> mapUsers(List<OnboardedUserRequest> users) {
        List<OnboardedUser> list = new ArrayList<>();
        for(OnboardedUserRequest request: users){
            OnboardedUser onboardedUser = new OnboardedUser();
            onboardedUser.setUser(request.getId());
            //COME MAPPARE L'UTENTE
            list.add(onboardedUser);
        }
        return list;
    }

    private static Billing mapBilling(BillingRequest billingRequest) {
        Billing billing = new Billing();
        billing.setPublicServer(billingRequest.getPublicServer());
        billing.setVatNumber(billingRequest.getVatNumber());
        billing.setRecipientCode(billingRequest.getRecipientCode());
        return billing;
    }

}
