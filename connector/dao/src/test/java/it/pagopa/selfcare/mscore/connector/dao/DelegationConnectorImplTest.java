package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapperImpl;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {DelegationConnectorImpl.class})
@ExtendWith(MockitoExtension.class)
class DelegationConnectorImplTest {

    @InjectMocks
    private DelegationConnectorImpl delegationConnectorImpl;

    @Mock
    private DelegationRepository delegationRepository;

    @Spy
    private DelegationEntityMapper delegationMapper = new DelegationEntityMapperImpl();

    @Test
    void testSaveDelegation() {
        DelegationEntity delegationEntity = new DelegationEntity();
        delegationEntity.setId("id");
        delegationEntity.setType(DelegationType.PT);
        when(delegationRepository.save(Mockito.any())).thenReturn(delegationEntity);
        Delegation response = delegationConnectorImpl.save(new Delegation());
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(response.getId(), delegationEntity.getId());
        assertEquals(response.getType(), delegationEntity.getType());
    }

    @Test
    void testSaveDelegationWithError() {
        when(delegationRepository.save(any())).thenThrow(new MsCoreException(CREATE_DELEGATION_ERROR.getMessage(), CREATE_DELEGATION_ERROR.getCode()));
        assertThrows(MsCoreException.class, () -> delegationConnectorImpl.save(new Delegation()));
        verify(delegationRepository).save(any());
    }

}