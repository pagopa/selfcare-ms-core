package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalServiceImplTest {

    @InjectMocks
    private ExternalServiceImpl externalServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private UserService userService;

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId() {
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId2() {
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getInstitutionByExternalId(String)}
     */
    @Test
    void testGetInstitutionByExternalId3() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.getInstitutionByExternalId("42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionManager(String, String)}
     */
    @Test
    void testRetrieveInstitutionManager() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        when(userService.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        Institution institution = new Institution();
        institution.setOnboarding(new ArrayList<>());
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        Assertions.assertThrows(InvalidRequestException.class, () -> externalServiceImpl.retrieveInstitutionManager("42", "42"));
    }

    @Test
    void testRetrieveInstitutionManager2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        List<UserBinding> userBindings = new ArrayList<>();
        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("id");
        userBinding.setProducts(new ArrayList<>());
        userBindings.add(userBinding);
        onboardedUser.setBindings(userBindings);
        onboardedUser.setId("id");
        when(userService.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        Institution institution = new Institution();
        institution.setId("id");
        institution.setOnboarding(new ArrayList<>());
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        assertNotNull(externalServiceImpl.retrieveInstitutionManager("42", "42"));
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionManager(String, String)}
     */
    @Test
    void testRetrieveInstitutionManager3() {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.retrieveInstitutionManager("42", "42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionManager(String, String)}
     */
    @Test
    void testRetrieveInstitutionManager6() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution("42", "42",
                Origin.MOCK, "42", "The characteristics of someone or something", InstitutionType.PA, "42 Main St",
                "42 Main St", "21654", "Tax Code", billing, onboarding, geographicTaxonomies, attributes,
                paymentServiceProvider, new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"), "Rea",
                "Share Capital", "Business Register Place", "jane.doe@example.org", "6625550144", true, null, null));

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        when(userService.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.retrieveInstitutionManager("42", "42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(userService).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionManager(String, String)}
     */
    @Test
    void testRetrieveInstitutionManager8() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboarding = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution("42", "42",
                Origin.MOCK, "42", "The characteristics of someone or something", InstitutionType.PA, "42 Main St",
                "42 Main St", "21654", "Tax Code", billing, onboarding, geographicTaxonomies, attributes,
                paymentServiceProvider, new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"), "Rea",
                "Share Capital", "Business Register Place", "jane.doe@example.org", "6625550144", true, null, null));
        when(userService.findOnboardedManager(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.retrieveInstitutionManager("42", "42"));
        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(userService).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct() {
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionService).retrieveInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct2() {
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionService).retrieveInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct3() {
        when(institutionService.retrieveInstitutionProduct(any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionService).retrieveInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId() {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        when(institutionService.retrieveInstitutionProducts(any(), any()))
                .thenReturn(onboardingList);
        List<Onboarding> actualRetrieveInstitutionProductsByExternalIdResult = externalServiceImpl
                .retrieveInstitutionProductsByExternalId("42", new ArrayList<>());
        assertSame(onboardingList, actualRetrieveInstitutionProductsByExternalIdResult);
        assertTrue(actualRetrieveInstitutionProductsByExternalIdResult.isEmpty());
        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(institutionService).retrieveInstitutionProducts(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProductsByExternalId(String, List)}
     */
    @Test
    void testRetrieveInstitutionProductsByExternalId2() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        List<RelationshipState> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> externalServiceImpl.retrieveInstitutionProductsByExternalId("42", list));
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionByIds(List)}
     */
    @Test
    void testRetrieveInstitutionByIds() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionService.retrieveInstitutionByIds(any())).thenReturn(institutionList);
        List<Institution> actualRetrieveInstitutionByIdsResult = externalServiceImpl
                .retrieveInstitutionByIds(new ArrayList<>());
        assertSame(institutionList, actualRetrieveInstitutionByIdsResult);
        assertTrue(actualRetrieveInstitutionByIdsResult.isEmpty());
        verify(institutionService).retrieveInstitutionByIds(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionByIds(List)}
     */
    @Test
    void testRetrieveInstitutionByIds2() {
        when(institutionService.retrieveInstitutionByIds(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        List<String> ids = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> externalServiceImpl.retrieveInstitutionByIds(ids));
        verify(institutionService).retrieveInstitutionByIds(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#createPnPgInstitution(String, String)}
     */
    @Test
    void testCreatePnPgInstitution() {
        Institution institution = new Institution();
        when(institutionService.createPnPgInstitution(any(), any())).thenReturn(institution);
        assertSame(institution,
                externalServiceImpl.createPnPgInstitution("42", "The characteristics of someone or something"));
        verify(institutionService).createPnPgInstitution(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#createPnPgInstitution(String, String)}
     */
    @Test
    void testCreatePnPgInstitution2() {
        when(institutionService.createPnPgInstitution(any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class,
                () -> externalServiceImpl.createPnPgInstitution("42", "The characteristics of someone or something"));
        verify(institutionService).createPnPgInstitution(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId() {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        ArrayList<GeographicTaxonomies> geographicTaxonomiesList = new ArrayList<>();
        when(institutionService.retrieveInstitutionGeoTaxonomies(any()))
                .thenReturn(geographicTaxonomiesList);
        List<GeographicTaxonomies> actualRetrieveInstitutionGeoTaxonomiesByExternalIdResult = externalServiceImpl
                .retrieveInstitutionGeoTaxonomiesByExternalId("42");
        assertSame(geographicTaxonomiesList, actualRetrieveInstitutionGeoTaxonomiesByExternalIdResult);
        assertTrue(actualRetrieveInstitutionGeoTaxonomiesByExternalIdResult.isEmpty());
        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(institutionService).retrieveInstitutionGeoTaxonomies(any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getUserInstitutionRelationships(String, String, String, List, List, List, List)}
     */
    @Test
    void testGetUserInstitutionRelationships() {
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(new Institution());
        ArrayList<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        when(institutionService.retrieveUserInstitutionRelationships(any(), any(), any(),
                any(), any(), any(), any()))
                .thenReturn(relationshipInfoList);
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        List<RelationshipInfo> actualUserInstitutionRelationships = externalServiceImpl
                .getUserInstitutionRelationships("42", "42", "42", roles, states, products, new ArrayList<>());
        assertSame(relationshipInfoList, actualUserInstitutionRelationships);
        assertTrue(actualUserInstitutionRelationships.isEmpty());
        verify(institutionService).retrieveInstitutionByExternalId(any());
        verify(institutionService).retrieveUserInstitutionRelationships(any(), any(),
                any(), any(), any(), any(),
                any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#getUserInstitutionRelationships(String, String, String, List, List, List, List)}
     */
    @Test
    void testGetUserInstitutionRelationships2() {
        when(institutionService.retrieveInstitutionByExternalId(any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        ArrayList<String> productRoles = new ArrayList<>();
        assertThrows(InvalidRequestException.class, () -> externalServiceImpl.getUserInstitutionRelationships("42", "42",
                "42", roles, states, products, productRoles));
    }
}
