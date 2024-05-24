package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
import it.pagopa.selfcare.mscore.model.delegation.PageInfo;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
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

    private static final Delegation dummyDelegationTaxCode;

    static {
        dummyDelegationProdIo = new Delegation();
        dummyDelegationProdIo.setId("id");
        dummyDelegationProdIo.setTo("to");
        dummyDelegationProdIo.setFrom("from");
        dummyDelegationProdIo.setProductId("prod-io");

        dummyDelegationProdPagopa = new Delegation();
        dummyDelegationProdPagopa.setId("id");
        dummyDelegationProdPagopa.setProductId("prod-pagopa");
        dummyDelegationProdPagopa.setTo("taxCodeTo");
        dummyDelegationProdPagopa.setFrom("from");

        dummyDelegationTaxCode = new Delegation();
        dummyDelegationTaxCode.setToTaxCode("taxCodeTo");
        dummyDelegationTaxCode.setFromTaxCode("taxCodeFrom");
        dummyDelegationTaxCode.setProductId("prod-pagopa");
        dummyDelegationTaxCode.setId("id");
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationWithProductProdIo() {
        Institution institutionTo = new Institution();
        institutionTo.setId("idTo");
        institutionTo.setTaxCode("taxCodeTo");
        Institution institutionFrom = new Institution();
        institutionTo.setId("idFrom");
        institutionTo.setTaxCode("taxCodeFrom");
        when(delegationConnector.save(dummyDelegationProdIo)).thenAnswer(arg ->arg.getArguments()[0]);
        when(institutionService.retrieveInstitutionById(dummyDelegationProdIo.getFrom())).thenReturn(institutionFrom);
        when(institutionService.retrieveInstitutionById(dummyDelegationProdIo.getTo())).thenReturn(institutionTo);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegation(dummyDelegationProdIo);
        verify(delegationConnector).save(dummyDelegationProdIo);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationProdIo.getId(), response.getId());
        assertEquals(institutionTo.getTaxCode(), response.getToTaxCode());
        assertEquals(institutionFrom.getTaxCode(), response.getFromTaxCode());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationForPTWithProductPagopa() {
        Institution institutionTo = new Institution();
        institutionTo.setId("id");
        institutionTo.setInstitutionType(InstitutionType.PT);
        institutionTo.setTaxCode("taxCodeTo");
        Institution institutionFrom = new Institution();
        institutionFrom.setId("id");
        institutionFrom.setTaxCode("taxCodeFrom");
        when(delegationConnector.save(dummyDelegationProdPagopa)).thenAnswer(arg ->arg.getArguments()[0]);
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), null)).thenReturn(List.of(institutionTo));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        when(institutionService.retrieveInstitutionById(dummyDelegationProdPagopa.getFrom())).thenReturn(institutionFrom);
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
        Institution institutionTo = new Institution();
        institutionTo.setId("to");
        institutionTo.setInstitutionType(InstitutionType.PT);
        institutionTo.setTaxCode("taxCodeTo");
        Institution institutionFrom = new Institution();
        institutionFrom.setId("from");
        institutionFrom.setTaxCode("taxCodeFrom");
        when(delegationConnector.findAndActivate(institutionFrom.getId(), institutionTo.getId(), dummyDelegationProdPagopa.getProductId())).thenReturn(dummyDelegationProdPagopa);;
        doNothing().when(mailNotificationService).sendMailForDelegation(any(), any(), any());
        when(institutionService.getInstitutions(dummyDelegationProdPagopa.getTo(), null)).thenReturn(List.of(institutionTo));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        when(delegationServiceImpl.checkIfExistsWithStatus(dummyDelegationProdPagopa, DelegationState.DELETED)).thenReturn(true);
        when(institutionService.retrieveInstitutionById(dummyDelegationProdPagopa.getFrom())).thenReturn(institutionFrom);
        Delegation response = delegationServiceImpl.createDelegation(dummyDelegationProdPagopa);
        verify(delegationConnector).findAndActivate(institutionFrom.getId(), institutionTo.getId(), dummyDelegationProdPagopa.getProductId());
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
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
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
        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);
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
        when(institutionService.retrieveInstitutionById(dummyDelegationProdIo.getTo())).thenReturn(institution);
        when(institutionService.retrieveInstitutionById(dummyDelegationProdIo.getFrom())).thenReturn(institution);
        when(delegationServiceImpl.checkIfExistsWithStatus(dummyDelegationProdIo, DelegationState.ACTIVE)).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegation(dummyDelegationProdIo));
        verifyNoMoreInteractions(delegationConnector);
    }

    /**
     * Method under test: {@link DelegationService#checkIfExistsWithStatus(Delegation, DelegationState)}
     */
    @Test
    void testCheckIfExists() {
        Delegation delegation = new Delegation();
        when(delegationConnector.checkIfExistsWithStatus(any(), any())).thenReturn(true);
        boolean check = delegationServiceImpl.checkIfExistsWithStatus(delegation, DelegationState.ACTIVE);
        assertTrue(check);
    }

    /**
     * Method under test: {@link DelegationServiceImpl#getDelegations(String, String, String, String, String, Optional, Optional, Optional)}
     */
    @Test
    void find_shouldGetData() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", "to", "productId", null,
                null, Optional.empty(), Optional.of(0), Optional.of(100));
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#getDelegations(String, String, String, String, String, Optional, Optional, Optional)}
     */
    @Test
    void find_shouldGetData_fullMode() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", null, "productId", null,
                null, Optional.of(Order.DESC), Optional.of(0), Optional.of(0));
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#getDelegations(String, String, String, String, String, Optional, Optional, Optional)}
     */
    @Test
    void find_shouldGetData_fullMode_defaultPage() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.find(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(List.of(delegation));
        //When
        List<Delegation> response = delegationServiceImpl.getDelegations("from", null, "productId", null,
                null, Optional.of(Order.DESC), Optional.empty(), Optional.empty());
        //Then
        verify(delegationConnector).find(any(), any(), any(), any(), any(), any(), any(), any());

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(delegation.getId(), response.get(0).getId());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#getDelegationsV2(GetDelegationParameters)}
     */
    @Test
    void getDelegationsV2_shouldGetData() {
        //Given
        Delegation delegation = new Delegation();
        delegation.setId("id");
        DelegationWithPagination delegationWithPagination = new DelegationWithPagination(List.of(delegation), new PageInfo(10, 0, 10, 1));
        when(delegationConnector.findAndCount(any())).thenReturn(delegationWithPagination);
        GetDelegationParameters delegationParameters = createDelegationParameters("from", "to", "productId", null,
                null, null, 0, 100);

        //When
        DelegationWithPagination response = delegationServiceImpl.getDelegationsV2(delegationParameters);
        //Then
        ArgumentCaptor<GetDelegationParameters> argumentCaptor = ArgumentCaptor.forClass(GetDelegationParameters.class);
        verify(delegationConnector).findAndCount(argumentCaptor.capture());
        assertNotNull(argumentCaptor);
        assertEquals(argumentCaptor.getValue(), delegationParameters);

        assertNotNull(response);
        assertNotNull(response.getDelegations());
        assertNotNull(response.getPageInfo());
        assertFalse(response.getDelegations().isEmpty());
        assertEquals(delegation.getId(), response.getDelegations().get(0).getId());
    }

    @Test
    void testCreateDelegationFromTaxCode() {
        Institution institutionTo = new Institution();
        institutionTo.setId("id");
        institutionTo.setTaxCode("taxCodeTo");
        when(delegationConnector.save(any())).thenReturn(dummyDelegationTaxCode);
        Institution institutionFrom = new Institution();
        institutionFrom.setId("id");
        institutionFrom.setTaxCode("taxCodeFrom");
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getToTaxCode(), dummyDelegationTaxCode.getToSubunitCode())).thenReturn(List.of(institutionTo));
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getFromTaxCode(), dummyDelegationTaxCode.getFromSubunitCode())).thenReturn(List.of(institutionFrom));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationTaxCode);
        verify(delegationConnector).save(any());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationTaxCode.getId(), response.getId());
        assertEquals(institutionTo.getTaxCode(), response.getToTaxCode());
        assertEquals(institutionFrom.getTaxCode(), response.getFromTaxCode());
    }

    @Test
    void testCreateDelegationFromTaxCodeWithSubunitCode() {
        Institution institutionTo = new Institution();
        institutionTo.setId("id");
        institutionTo.setTaxCode("taxCodeTo");
        Institution institutionFrom = new Institution();
        institutionFrom.setId("id");
        institutionFrom.setTaxCode("taxCodeFrom");
        when(delegationConnector.findAndActivate(anyString(), anyString(), anyString())).thenReturn(dummyDelegationTaxCode);
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getToTaxCode(), dummyDelegationTaxCode.getToSubunitCode())).thenReturn(List.of(institutionTo));
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getFromTaxCode(), dummyDelegationTaxCode.getFromSubunitCode())).thenReturn(List.of(institutionFrom));
        doNothing().when(institutionService).updateInstitutionDelegation(any(),anyBoolean());
        when(delegationConnector.checkIfExistsWithStatus(dummyDelegationTaxCode, DelegationState.ACTIVE)).thenReturn(false);
        when(delegationConnector.checkIfExistsWithStatus(dummyDelegationTaxCode, DelegationState.DELETED)).thenReturn(true);
        Delegation response = delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationTaxCode);
        verify(delegationConnector).findAndActivate(anyString(), anyString(), anyString());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(dummyDelegationTaxCode.getId(), response.getId());
        assertEquals(dummyDelegationTaxCode.getFromTaxCode(), response.getFromTaxCode());
        assertEquals(dummyDelegationTaxCode.getToTaxCode(), response.getToTaxCode());
    }

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegationFromTaxCodeWithError() {
        Institution institution = new Institution();
        institution.setId("id");
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getToTaxCode(), null)).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getFromTaxCode(), null)).thenReturn(List.of(institution));
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationTaxCode));
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
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getToTaxCode(), dummyDelegationProdPagopa.getToSubunitCode())).thenReturn(List.of(institution));
        when(institutionService.getInstitutions(dummyDelegationTaxCode.getFromTaxCode(), dummyDelegationProdPagopa.getFromSubunitCode())).thenReturn(List.of(institution));
        when(delegationConnector.checkIfExistsWithStatus(dummyDelegationTaxCode, DelegationState.ACTIVE)).thenReturn(true);
        assertThrows(ResourceConflictException.class, () -> delegationServiceImpl.createDelegationFromInstitutionsTaxCode(dummyDelegationTaxCode));
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


    private GetDelegationParameters createDelegationParameters(String from, String to, String productId,
                                                               String search, String taxCode, Order order,
                                                               Integer page, Integer size) {
        return GetDelegationParameters.builder()
                .from(from)
                .to(to)
                .productId(productId)
                .search(search)
                .taxCode(taxCode)
                .order(order)
                .page(page)
                .size(size)
                .build();
    }

}