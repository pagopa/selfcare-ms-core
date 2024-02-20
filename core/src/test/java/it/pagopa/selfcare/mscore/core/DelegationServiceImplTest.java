package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericError.SEND_MAIL_FOR_DELEGATION_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DelegationServiceImplTest {
    @Mock
    private DelegationConnector delegationConnector;

    @InjectMocks
    private DelegationServiceImpl delegationServiceImpl;

    @Mock
    private MailNotificationService mailNotificationService;

    @Mock
    private InstitutionService institutionService;

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegation() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.save(any())).thenReturn(delegation);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(delegation);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(delegation.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationForProductPagopa() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setProductId("prod-pagopa");
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(delegation);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(delegation);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(delegation.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithSendMailError() {
        doThrow(new MsCoreException(SEND_MAIL_FOR_DELEGATION_ERROR.getMessage(), SEND_MAIL_FOR_DELEGATION_ERROR.getCode()))
                .when(mailNotificationService)
                .sendMailForDelegation(any(), any(), any());
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegation(new Delegation()));
        verify(mailNotificationService).sendMailForDelegation(any(), any(), any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithError() {
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegation(new Delegation()));
        verify(delegationConnector).save(any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithResourceNotFoundException() {
        Delegation delegation = new Delegation();
        delegation.setProductId("prod-pagopa");
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> delegationServiceImpl.createDelegation(delegation));
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithConflict() {
        when(delegationServiceImpl.checkIfExists(any())).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegation(new Delegation()));
        verifyNoMoreInteractions(delegationConnector);
    }

    /**
     * Method under test: {@link DelegationServiceImpl#checkIfExists(Delegation)}
     */
    @Test
    void testCheckIfExists() {
        Delegation delegation = new Delegation();
        when(delegationConnector.checkIfExists(any())).thenReturn(true);
        boolean check = delegationServiceImpl.checkIfExists(delegation);
        assertTrue(check);
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void find_shouldGetData() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", "to", "productId", GetDelegationsMode.NORMAL);
        //Then
        verify(delegationConnector).find(any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void find_shouldGetData_fullMode() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", null, "productId", GetDelegationsMode.FULL);
        //Then
        verify(delegationConnector).find(any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    @Test
    void testCreateDelegationFromTaxCode() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setProductId("prod-pagopa");
        delegation.setTo("fromToCode");
        delegation.setFrom("fromTaxCode");
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(delegation);
        when(institutionService.getInstitutions(delegation.getTo(), delegation.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(delegation.getFrom(), delegation.getFromSubunitCode())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(delegation);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(delegation.getId(), response.getId());
    }

    @Test
    void testCreateDelegationFromTaxCodeWithSubunitCode() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setProductId("prod-pagopa");
        delegation.setTo("fromToCode");
        delegation.setFromSubunitCode("fromSubunit");
        delegation.setToSubunitCode("toSubunit");
        delegation.setFrom("fromTaxCode");
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(delegation);
        when(institutionService.getInstitutions(delegation.getTo(), delegation.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(delegation.getFrom(), delegation.getFromSubunitCode())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(delegation);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(delegation.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeWithError() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        delegation.setProductId("prod-pagopa");
        delegation.setTo("fromToCode");
        delegation.setFrom("fromTaxCode");
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(delegation.getTo(), null)).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(delegation.getFrom(), null)).thenReturn(List.of(institution));
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(delegation));
        verify(delegationConnector).save(any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeWithResourceNotFoundException() {
        Delegation delegation = new Delegation();
        delegation.setProductId("prod-pagopa");
        when(institutionService.getInstitutions(delegation.getTo(), null)).thenReturn(Collections.emptyList());
        when(institutionService.getInstitutions(delegation.getFrom(), null)).thenReturn(Collections.emptyList());
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(delegation));
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeConflict() {
        Delegation delegation = new Delegation();
        delegation.setProductId("prod-pagopa");
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(delegation.getTo(), delegation.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(delegation.getFrom(), delegation.getFromSubunitCode())).thenReturn(List.of(institution));
        when(delegationConnector.checkIfExists(any())).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(delegation));
    }

}