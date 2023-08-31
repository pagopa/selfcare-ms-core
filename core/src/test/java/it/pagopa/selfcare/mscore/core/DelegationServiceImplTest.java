package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DelegationServiceImplTest {
    @Mock
    private DelegationConnector delegationConnector;

    @InjectMocks
    private DelegationServiceImpl delegationServiceImpl;

    /**
     * Method under test: {@link DelegationServiceImpl#createDelegation(Delegation)}
     */
    @Test
    void testCreateDelegation() {
        Delegation delegation = new Delegation();
        delegation.setId("id");
        when(delegationConnector.save(any())).thenReturn(delegation);
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
    void testCreateDelegationWithError() {
        when(delegationConnector.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationServiceImpl.createDelegation(new Delegation()));
        verify(delegationConnector).save(any());
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

}