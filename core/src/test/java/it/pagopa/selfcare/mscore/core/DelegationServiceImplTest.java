package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertNotNull(delegation);
        assertNotNull(delegation.getId());
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

}