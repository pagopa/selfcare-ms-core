package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInfoResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class OnboardingMapperTest {
    /**
     * Method under test: {@link OnboardingMapper#toOnboardingLegalsRequest(OnboardingInstitutionLegalsRequest)}
     */
    @Test
    void testToOnboardingLegalsRequest() {
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPath("Path");
        contractRequest.setVersion("1.0.2");

        OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest = new OnboardingInstitutionLegalsRequest();
        onboardingInstitutionLegalsRequest.setContract(contractRequest);
        onboardingInstitutionLegalsRequest.setInstitutionExternalId("42");
        onboardingInstitutionLegalsRequest.setInstitutionId("42");
        onboardingInstitutionLegalsRequest.setProductId("42");
        onboardingInstitutionLegalsRequest.setProductName("Product Name");
        onboardingInstitutionLegalsRequest.setSignContract(true);
        ArrayList<Person> personList = new ArrayList<>();
        onboardingInstitutionLegalsRequest.setUsers(personList);
        OnboardingLegalsRequest actualToOnboardingLegalsRequestResult = OnboardingMapper
                .toOnboardingLegalsRequest(onboardingInstitutionLegalsRequest);
        assertTrue(actualToOnboardingLegalsRequestResult.isSignContract());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getProductId());
        assertEquals(TokenType.LEGALS, actualToOnboardingLegalsRequestResult.getTokenType());
        assertEquals("Product Name", actualToOnboardingLegalsRequestResult.getProductName());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getInstitutionId());
        Contract contract = actualToOnboardingLegalsRequestResult.getContract();
        assertEquals("1.0.2", contract.getVersion());
        assertEquals("Path", contract.getPath());
    }

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingLegalsRequest(OnboardingInstitutionLegalsRequest)}
     */
    @Test
    void testToOnboardingLegalsRequest2() {
        ArrayList<Person> personList = new ArrayList<>();
        personList.add(new Person());

        OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest = new OnboardingInstitutionLegalsRequest();
        onboardingInstitutionLegalsRequest.setInstitutionExternalId("42");
        onboardingInstitutionLegalsRequest.setInstitutionId("42");
        onboardingInstitutionLegalsRequest.setProductId("42");
        onboardingInstitutionLegalsRequest.setProductName("Product Name");
        onboardingInstitutionLegalsRequest.setSignContract(true);
        onboardingInstitutionLegalsRequest.setContract(null);
        onboardingInstitutionLegalsRequest.setUsers(personList);
        OnboardingLegalsRequest actualToOnboardingLegalsRequestResult = OnboardingMapper
                .toOnboardingLegalsRequest(onboardingInstitutionLegalsRequest);
        assertTrue(actualToOnboardingLegalsRequestResult.isSignContract());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getInstitutionExternalId());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getProductId());
        assertEquals(1, actualToOnboardingLegalsRequestResult.getUsers().size());
        assertEquals(TokenType.LEGALS, actualToOnboardingLegalsRequestResult.getTokenType());
        assertEquals("Product Name", actualToOnboardingLegalsRequestResult.getProductName());
        assertEquals("42", actualToOnboardingLegalsRequestResult.getInstitutionId());
    }

    @Test
    void toOnboardingInfoResponse_onboardingEmpty() {
        String userId = "userId";
        List<OnboardingInfo> onboardingInfos = new ArrayList<>();
        onboardingInfos.add(new OnboardingInfo());

        OnboardingInfoResponse actual = OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfos);
        assertEquals(actual.getUserId(), userId);
    }

    @Test
    void toOnboardingInfoResponse() {
        String userId = "userId";
        List<OnboardingInfo> onboardingInfos = new ArrayList<>();
        OnboardingInfo onboardingInfo = new OnboardingInfo();
        Institution institution = new Institution();

        Onboarding onboarding = new Onboarding();
        onboarding.setProductId("prod-io");
        institution.setOnboarding(List.of(onboarding));

        onboardingInfo.setInstitution(institution);
        UserInstitutionBinding institutionBinding = new UserInstitutionBinding();
        institutionBinding.setProducts(new OnboardedProduct());
        onboardingInfo.setBinding(institutionBinding);
        onboardingInfos.add(onboardingInfo);

        OnboardingInfoResponse actual = OnboardingMapper.toOnboardingInfoResponse(userId, onboardingInfos);
        assertEquals(actual.getUserId(), userId);
        assertEquals(actual.getInstitutions().size(), 0);
    }
}

