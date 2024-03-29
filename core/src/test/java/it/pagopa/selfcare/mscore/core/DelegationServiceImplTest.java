package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;
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

    private static final Delegation dummyDelegationProdIo;
    private static final Delegation dummyDelegationProdPagopa;

    static {
        dummyDelegationProdIo = new Delegation();
        dummyDelegationProdIo.setId("id");
        dummyDelegationProdIo.setProductId("prod-io");

        dummyDelegationProdPagopa = new Delegation();
        dummyDelegationProdPagopa.setId("id");
        dummyDelegationProdPagopa.setProductId("prod-pagopa");
        dummyDelegationProdPagopa.setTo("to");
        dummyDelegationProdPagopa.setFrom("from");
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithProductProdIo() {
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(dummyDelegationProdIo);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(dummyDelegationProdIo);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdIo.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationForPTWithProductPagopa() {
        Institution institution = new Institution();
        institution.setId("id");
        institution.setInstitutionType(InstitutionType.PT);
        when(delegationConnector.save(any())).thenReturn(dummyDelegationProdPagopa);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(dummyDelegationProdPagopa);
        verify(institutionService).getInstitutions(any(), any());
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdPagopa.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationForEcWithProductPagopa() {
        Institution institution = new Institution();
        institution.setId("id");
        institution.setInstitutionType(InstitutionType.PT);
        when(delegationConnector.save(any())).thenReturn(dummyDelegationProdPagopa);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(dummyDelegationProdPagopa);
        verify(delegationConnector).save(any());
        verify(institutionService).getInstitutions(any(), any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdPagopa.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithSendMailError() {
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of(institution));
        doThrow(new MsCoreException(SEND_MAIL_FOR_DELEGATION_ERROR.getMessage(), SEND_MAIL_FOR_DELEGATION_ERROR.getCode()))
                .when(mailNotificationService)
                .sendMailForDelegation(any(), any(), any());
        assertDoesNotThrow(() -> delegationServiceImpl.createDelegation(dummyDelegationProdPagopa));
        verify(mailNotificationService).sendMailForDelegation(any(), any(), any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithError() {
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegation(dummyDelegationProdIo));
        verify(delegationConnector).save(any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithResourceNotFoundException() {
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> delegationServiceImpl.createDelegation(dummyDelegationProdPagopa));
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithConflict() {
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationServiceImpl.checkIfExists(any())).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegation(dummyDelegationProdIo));
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
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", "to", "productId", null, null,
                GetDelegationsMode.NORMAL, Optional.of(0), Optional.of(100));
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(), any(), any());

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
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", null, "productId", null, null,
                GetDelegationsMode.FULL, Optional.of(0), Optional.of(0));
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(),m any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void find_shouldGetData_fullMode_defaultPage() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", null, "productId", null, null,
                GetDelegationsMode.FULL, Optional.empty(), Optional.empty());
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    @Test
    void testCreateDelegationFromTaxCode() {
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(dummyDelegationProdPagopa);
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), dummyDelegationProdPagopa.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getFrom(), dummyDelegationProdPagopa.getFromSubunitCode())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationProdPagopa);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdPagopa.getId(), response.getId());
    }

    @Test
    void testCreateDelegationFromTaxCodeWithSubunitCode() {
        Institution institution = new Institution();
        institution.setId("id");
        when(delegationConnector.save(any())).thenReturn(dummyDelegationProdPagopa);
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), dummyDelegationProdPagopa.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getFrom(), dummyDelegationProdPagopa.getFromSubunitCode())).thenReturn(List.of(institution));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationProdPagopa);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdPagopa.getId(), response.getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeWithError() {
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), null)).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getFrom(), null)).thenReturn(List.of(institution));
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationProdPagopa));
        verify(delegationConnector).save(any());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeWithResourceNotFoundException() {
        when(institutionService.getInstitutions(any(), any())).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationProdPagopa));
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeConflict() {
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), dummyDelegationProdPagopa.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getFrom(), dummyDelegationProdPagopa.getFromSubunitCode())).thenReturn(List.of(institution));
        when(delegationConnector.checkIfExists(any())).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationProdPagopa));
    }

    @Test
    void testDeleteDelegationByDelegationId_whenDelegationisNotActive() {
        Delegation delegation = new Delegation();
        delegation.setTo("id");
        delegation.setStatus(DelegationState.DELETED);
        when(delegationConnector.findByIdAndModifyStatus("id", DelegationState.DELETED)).thenReturn(delegation);
        when(delegationConnector.checkIfDelegationsAreActive("id")).thenReturn(false);
        Executable executable = () -> delegationServiceImpl.deleteDelegationByDelegationId("id");
        assertDoesNotThrow(executable);
        verify(institutionService).updateInstitutionDelegation("id", false);
    }

    @Test
    void testDeleteDelegationByDelegationId_whenDelegationisActive() {
        Delegation delegation = new Delegation();
        delegation.setTo("id");
        delegation.setStatus(DelegationState.DELETED);
        when(delegationConnector.findByIdAndModifyStatus("id", DelegationState.DELETED)).thenReturn(delegation);
        when(delegationConnector.checkIfDelegationsAreActive("id")).thenReturn(true);
        Executable executable = () -> delegationServiceImpl.deleteDelegationByDelegationId("id");
        assertDoesNotThrow(executable);
        verify(institutionService, times(0)).updateInstitutionDelegation("id", false);
    }

    @Test
    void testDeleteDelegationByDelegationId_whenFindAndModifyStatusThrowsException() {
        Delegation delegation = new Delegation();
        delegation.setTo("id");
        delegation.setStatus(DelegationState.DELETED);
        when(delegationConnector.findByIdAndModifyStatus("id", DelegationState.DELETED))
                .thenThrow(new MsCoreException(DELETE_DELEGATION_ERROR.getMessage(), DELETE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.deleteDelegationByDelegationId("id"));
        verify(delegationConnector, times(0)).checkIfDelegationsAreActive(any());
    }

    @Test
    void testDeleteDelegationByDelegationId_whenUpdateInstitutionDelegationThrowsException() {
        Delegation delegation = new Delegation();
        delegation.setTo("id");
        delegation.setStatus(DelegationState.DELETED);
        when(delegationConnector.findByIdAndModifyStatus("id", DelegationState.DELETED)).thenReturn(delegation);
        when(delegationConnector.checkIfDelegationsAreActive("id")).thenReturn(false);
        doThrow(new MsCoreException(DELETE_DELEGATION_ERROR.getMessage(), DELETE_DELEGATION_ERROR.getCode()))
                .when(institutionService).updateInstitutionDelegation("id", false);
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.deleteDelegationByDelegationId("id"));
        verify(delegationConnector, times(1)).findByIdAndModifyStatus("id", DelegationState.ACTIVE);
    }

}