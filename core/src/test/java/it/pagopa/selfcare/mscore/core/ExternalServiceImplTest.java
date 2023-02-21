package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

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
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionManager(Institution, String)}
     */
    @Test
    void testRetrieveInstitutionManager() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userService.findOnboardedManager(any(), any(), any()))
                .thenReturn(onboardedUser);
        assertSame(onboardedUser, externalServiceImpl.retrieveInstitutionManager(new Institution(), "42"));
        verify(userService).findOnboardedManager(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveRelationship(String, String, String)}
     */
    @Test
    void testRetrieveRelationship() {
        when(tokenService.findActiveContract(any(), any(), any()))
                .thenReturn("Active Contract");
        assertEquals("Active Contract", externalServiceImpl.retrieveRelationship("42", "42", "42"));
        verify(tokenService).findActiveContract(any(), any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionProduct(String, String)}
     */
    @Test
    void testRetrieveInstitutionProduct() {
        Institution institution = new Institution();
        when(institutionService.getInstitutionProduct(any(), any())).thenReturn(institution);
        assertSame(institution, externalServiceImpl.retrieveInstitutionProduct("42", "42"));
        verify(institutionService).getInstitutionProduct(any(), any());
    }

    /**
     * Method under test: {@link ExternalServiceImpl#retrieveInstitutionGeoTaxonomiesByExternalId(String)}
     */
    @Test
    void testRetrieveInstitutionGeoTaxonomiesByExternalId2() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(new ArrayList<>());
        when(institutionService.retrieveInstitutionByExternalId(any())).thenReturn(institution);
        when(geoTaxonomiesConnector.getExtByCode(any())).thenReturn(new GeographicTaxonomies());
        assertTrue(externalServiceImpl.retrieveInstitutionGeoTaxonomiesByExternalId("42").isEmpty());
        verify(institutionService).retrieveInstitutionByExternalId(any());
    }

}

