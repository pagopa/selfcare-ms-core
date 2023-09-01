package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapperImpl;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapperImpl;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationInstitution;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_DELEGATION_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DelegationConnectorImpl.class})
@ExtendWith(MockitoExtension.class)
class DelegationConnectorImplTest {

    @InjectMocks
    private DelegationConnectorImpl delegationConnectorImpl;

    @Mock
    private DelegationRepository delegationRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Spy
    private DelegationEntityMapper delegationMapper = new DelegationEntityMapperImpl();

    @Spy
    private DelegationInstitutionMapper delegationInstitutionMapper = new DelegationInstitutionMapperImpl();

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

    @Test
    void testCheckIfExists() {
        Delegation delegation = new Delegation();
        delegation.setTo("to");
        delegation.setFrom("from");
        delegation.setType(DelegationType.PT);
        delegation.setProductId("prod");
        when(delegationRepository.findByFromAndToAndProductIdAndType(any(), any(), any(), any())).thenReturn(Optional.of(new DelegationEntity()));
        boolean response = delegationConnectorImpl.checkIfExists(delegation);
        assertTrue(response);

    }

    @Test
    void find_shouldGetData() {
        //Given
        DelegationEntity delegationEntity = new DelegationEntity();
        delegationEntity.setId("id");
        delegationEntity.setProductId("productId");
        delegationEntity.setType(DelegationType.PT);
        delegationEntity.setTo("To");
        delegationEntity.setFrom("From");
        delegationEntity.setInstitutionFromName("setInstitutionFromName");
        delegationEntity.setInstitutionFromRootName("setInstitutionFromRootName");

        //When
        when(delegationRepository.find(any(), any())).
                thenReturn(List.of(delegationEntity));

        List<Delegation> response = delegationConnectorImpl.find(delegationEntity.getFrom(),
                delegationEntity.getTo(), delegationEntity.getProductId(), GetDelegationsMode.NORMAL);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        Delegation actual = response.get(0);

        assertEquals(actual.getId(), delegationEntity.getId());
        assertEquals(actual.getType(), delegationEntity.getType());
        assertEquals(actual.getProductId(), delegationEntity.getProductId());
        assertEquals(actual.getTo(), delegationEntity.getTo());
        assertEquals(actual.getFrom(), delegationEntity.getFrom());
        assertEquals(actual.getInstitutionFromName(), delegationEntity.getInstitutionFromName());
        assertEquals(actual.getInstitutionFromRootName(), delegationEntity.getInstitutionFromRootName());
    }

    @Test
    void find_shouldGetData_fullMode() {
        //Given
        Institution institution = new Institution();
        institution.setTaxCode("taxCode");
        institution.setInstitutionType(InstitutionType.PT);
        DelegationInstitution delegationEntity = new DelegationInstitution();
        delegationEntity.setId("id");
        delegationEntity.setProductId("productId");
        delegationEntity.setType(DelegationType.PT);
        delegationEntity.setTo("To");
        delegationEntity.setFrom("From");
        delegationEntity.setInstitutionFromName("setInstitutionFromName");
        delegationEntity.setInstitutionFromRootName("setInstitutionFromRootName");
        delegationEntity.setInstitutions(List.of(institution));

        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(delegationEntity));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<Delegation> response = delegationConnectorImpl.find(delegationEntity.getFrom(),
                delegationEntity.getTo(), delegationEntity.getProductId(), GetDelegationsMode.FULL);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        Delegation actual = response.get(0);

        assertEquals(actual.getId(), delegationEntity.getId());
        assertEquals(actual.getType(), delegationEntity.getType());
        assertEquals(actual.getProductId(), delegationEntity.getProductId());
        assertEquals(actual.getTo(), delegationEntity.getTo());
        assertEquals(actual.getFrom(), delegationEntity.getFrom());
        assertEquals(actual.getInstitutionFromName(), delegationEntity.getInstitutionFromName());
        assertEquals(actual.getInstitutionFromRootName(), delegationEntity.getInstitutionFromRootName());
        assertEquals(actual.getTaxCode(), delegationEntity.getInstitutions().get(0).getTaxCode());
        assertEquals(actual.getInstitutionType(), delegationEntity.getInstitutions().get(0).getInstitutionType());
    }

}
