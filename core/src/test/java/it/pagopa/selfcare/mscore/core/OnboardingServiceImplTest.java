package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.core.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.core.mapper.TokenMapperImpl;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class OnboardingServiceImplTest {

    @Mock
    private OnboardingDao onboardingDao;

    @InjectMocks
    private OnboardingServiceImpl onboardingServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private ProductConnector productConnector;

    @Mock
    private UserNotificationService userNotificationService;

    @Spy
    private TokenMapper tokenMapper = new TokenMapperImpl();

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo() {
        doNothing().when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        onboardingServiceImpl.verifyOnboardingInfo("42", "42");
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfoSubunit(String, String, String)}
     */
    @Test
    void shouldNothingWhenVerifyOnboardingInfoSubunit() {
        when(institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any()))
                .thenReturn(true);
        onboardingServiceImpl.verifyOnboardingInfoSubunit("42", "42", "example");
        verify(institutionConnector).existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any());
    }

    @Test
    void VerifyOnboardingInfoSubunitResourceNotFound() {
        when(institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(any(), any(), any(), any()))
                .thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfoSubunit("42", "42", "example"));

    }

    @Test
    void testVerifyOnboardingInfoByFilter() {
        // Arrange
        when(institutionConnector.existsOnboardingByFilters(Mockito.any())).thenReturn(true);

        VerifyOnboardingFilters verifyOnboardingFilters = new VerifyOnboardingFilters("Product", "", "", "Origin", "OriginId", "");
        // Act
        onboardingServiceImpl.verifyOnboardingInfoByFilters(verifyOnboardingFilters);

        // Assert that nothing has changed
        verify(institutionConnector).existsOnboardingByFilters(Mockito.any());
    }

    @Test
    void testVerifyOnboardingInfoByFilterNotFound() {
        // Arrange
        when(institutionConnector.existsOnboardingByFilters(Mockito.any())).thenReturn(false);

        VerifyOnboardingFilters verifyOnboardingFilters = new VerifyOnboardingFilters("Product", "", "", "Origin", "OriginId", "");
        // Act
        Assertions.assertThrows(ResourceNotFoundException.class, () -> onboardingServiceImpl.verifyOnboardingInfoByFilters(verifyOnboardingFilters));

        // Assert that nothing has changed
        verify(institutionConnector).existsOnboardingByFilters(Mockito.any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#verifyOnboardingInfo(String, String)}
     */
    @Test
    void testVerifyOnboardingInfo5() {
        doThrow(new InvalidRequestException("An error occurred", "Code")).when(institutionService)
                .retrieveInstitutionsWithFilter(any(), any(), any());
        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.verifyOnboardingInfo("42", "42"));
        verify(institutionService).retrieveInstitutionsWithFilter(any(), any(),
                any());
    }

    @Test
    void persistOnboarding_shouldThrowIfOnboardingExists() {

        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding, dummyOnboarding()));
        when(institutionConnector.findById(institution.getId())).thenReturn(institution);

        String institutionId = institution.getId();

        String productId = onboarding.getProductId();
        Onboarding onb = new Onboarding();

        assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.persistOnboarding(institutionId,
                productId, onb));
    }



    /**
     * Method under test: {@link OnboardingServiceImpl#persistOnboarding(String, String, Onboarding)}
     */
    @Test
    void persistOnboarding_shouldRollback() {

        String pricingPlan = "pricingPlan";
        String productId = "productId";
        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Onboarding onboardingToPersist = new Onboarding();
        onboardingToPersist.setPricingPlan(pricingPlan);
        onboardingToPersist.setProductId(productId);
        onboardingToPersist.setBilling(new Billing());

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding));


        when(institutionConnector.findById(institution.getId())).thenReturn(institution);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenThrow(new RuntimeException());
        String institutionId = institution.getId();

        Assertions.assertThrows(InvalidRequestException.class, () -> onboardingServiceImpl.persistOnboarding(institutionId, productId, onboardingToPersist));

        verify(onboardingDao, times(1))
                .rollbackPersistOnboarding(any(), any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#persistOnboarding(String, String, Onboarding)}
     */
    @Test
    void persistOnboarding_whenUserNotExistsOnRegistry() {

        String pricingPlan = "pricingPlan";
        String productId = "productId";
        Billing billing = new Billing();
        billing.setVatNumber("vatNumber");
        billing.setPublicServices(false);
        billing.setRecipientCode("recipientCode");
        billing.setTaxCodeInvoicing("taxCodeInvoicing");
        Onboarding onboarding = dummyOnboarding();
        onboarding.setStatus(UtilEnumList.VALID_RELATIONSHIP_STATES.get(0));

        Onboarding onboardingToPersist = new Onboarding();
        onboardingToPersist.setPricingPlan(pricingPlan);
        onboardingToPersist.setProductId(productId);
        onboardingToPersist.setBilling(billing);
        onboardingToPersist.setIsAggregator(true);

        Institution institution = new Institution();
        institution.setId("institutionId");
        institution.setOnboarding(List.of(onboarding));


        Token token = new Token();
        token.setId(onboarding.getTokenId());
        token.setInstitutionId("institutionId");
        token.setProductId(productId);
        token.setCreatedAt(onboarding.getCreatedAt());
        token.setUpdatedAt(onboarding.getUpdatedAt());
        token.setStatus(onboarding.getStatus());
        token.setContractSigned(onboarding.getContract());

        when(institutionConnector.findById(institution.getId())).thenReturn(institution);
        when(institutionConnector.findAndUpdate(any(), any(), any(), any())).thenReturn(institution);

        onboardingServiceImpl.persistOnboarding(institution.getId(), productId, onboardingToPersist);


        ArgumentCaptor<Onboarding> captor = ArgumentCaptor.forClass(Onboarding.class);
        verify(institutionConnector, times(1))
                .findAndUpdate(any(), captor.capture(), any(), any());
        Onboarding actual = captor.getValue();
        assertEquals(billing, actual.getBilling());
        assertEquals(actual.getCreatedAt().getDayOfYear(), LocalDate.now().getDayOfYear());
    }

    private Onboarding dummyOnboarding() {
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setTokenId("42");
        onboarding.setPricingPlan("C3");
        onboarding.setProductId("42");
        return onboarding;
    }
}

