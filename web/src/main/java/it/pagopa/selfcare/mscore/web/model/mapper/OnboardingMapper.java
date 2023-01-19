package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.*;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingMapper {

    private static final List<RelationshipState> defaultRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.PENDING);

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

    private static List<OnboardedUser> convertToOnboarderUser(OnboardingInstitutionRequest onboardingInstitutionRequest) {
        List<OnboardedUser> onboardedUsers = new ArrayList<>();
        if(!onboardingInstitutionRequest.getUsers().isEmpty()){
            for(Person p: onboardingInstitutionRequest.getUsers()) {
                OnboardedUser onboardedUser = new OnboardedUser();
                onboardedUser.setUser(p.getId());
                onboardedUser.setRole(PartyRole.valueOf(p.getRole()));
                onboardedUser.setBindings(constructMap(p, onboardingInstitutionRequest));
                onboardedUsers.add(onboardedUser);
            }
        }
        return onboardedUsers;
    }

    private static Map<String, Map<String, Product>> constructMap(Person p, OnboardingInstitutionRequest onboardingInstitutionRequest) {
        Map<String, Map<String,Product>> map = new HashMap<>();
        map.put(onboardingInstitutionRequest.getInstitutionExternalId(),constructProductMap(onboardingInstitutionRequest,p));
        return map;
    }

    private static Map<String, Product> constructProductMap(OnboardingInstitutionRequest onboardingInstitutionRequest, Person p) {
        Map<String,Product> productMap = new HashMap<>();
        productMap.put(onboardingInstitutionRequest.getProductId(),constructProduct(p, onboardingInstitutionRequest.getInstitutionUpdate().getInstitutionType()));
        return productMap;
    }

    private static Product constructProduct(Person p, InstitutionType institutionType) {
        Product product = new Product();
        product.setRoles(List.of(p.getRole(),p.getProductRole()));
        product.setStatus(retrieveStatusFromInstitutionType(institutionType));
        product.setCreatedAt(OffsetDateTime.now());
        product.setUpdatedAt(OffsetDateTime.now());
        return product;
    }

    private static RelationshipState retrieveStatusFromInstitutionType(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
                return RelationshipState.TOBEVALIDATED;
        }
    }

    private static Billing convertToBilling(BillingRequest billingRequest) {
        Billing billing = new Billing();
        billing.setRecipientCode(billingRequest.getRecipientCode());
        billing.setVatNumber(billingRequest.getVatNumber());
        billing.setPublicServices(billing.isPublicServices());
        return billing;
    }
}
