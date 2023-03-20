package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.ContractRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionLegalsRequest;
import it.pagopa.selfcare.mscore.web.model.user.Person;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;

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
        assertEquals(personList, actualToOnboardingLegalsRequestResult.getUsers());
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

    /**
     * Method under test: {@link OnboardingMapper#toOnboardingLegalsRequest(OnboardingInstitutionLegalsRequest)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testToOnboardingLegalsRequest3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.web.model.mapper.UserMapper.toUserToOnboard(UserMapper.java:23)
        //       at it.pagopa.selfcare.mscore.web.model.mapper.UserMapper.toUserToOnboard(UserMapper.java:16)
        //       at it.pagopa.selfcare.mscore.web.model.mapper.OnboardingMapper.toOnboardingLegalsRequest(OnboardingMapper.java:139)
        //   See https://diff.blue/R013 to resolve this issue.

        ArrayList<Person> personList = new ArrayList<>();
        personList.add(null);

        OnboardingInstitutionLegalsRequest onboardingInstitutionLegalsRequest = new OnboardingInstitutionLegalsRequest();
        onboardingInstitutionLegalsRequest.setInstitutionExternalId("42");
        onboardingInstitutionLegalsRequest.setInstitutionId("42");
        onboardingInstitutionLegalsRequest.setProductId("42");
        onboardingInstitutionLegalsRequest.setProductName("Product Name");
        onboardingInstitutionLegalsRequest.setSignContract(true);
        onboardingInstitutionLegalsRequest.setContract(null);
        onboardingInstitutionLegalsRequest.setUsers(personList);
        OnboardingMapper.toOnboardingLegalsRequest(onboardingInstitutionLegalsRequest);
    }
}

