package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ExternalServiceImplTest {

    @InjectMocks
    private ExternalServiceImpl externalServiceImpl;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private GeoTaxonomiesConnector geoTaxonomiesConnector;
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
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct() {
        Institution institution = new Institution();
        when(institutionService.retrieveInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionService).retrieveInstitutionProduct(any(), any());
    }
}
