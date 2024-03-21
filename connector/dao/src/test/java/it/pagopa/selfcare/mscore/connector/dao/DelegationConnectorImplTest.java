package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapperImpl;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapperImpl;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationInstitution;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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

    public static final int PAGE_SIZE = 0;
    public static final int MAX_PAGE_SIZE = 100;
    static Institution dummyInstitution;
    static DelegationInstitution dummyDelegationEntity;

    static {
        dummyInstitution = new Institution();
        dummyInstitution.setTaxCode("taxCode");
        dummyInstitution.setInstitutionType(InstitutionType.PT);
        dummyDelegationEntity = new DelegationInstitution();
        dummyDelegationEntity.setId("id");
        dummyDelegationEntity.setProductId("productId");
        dummyDelegationEntity.setType(DelegationType.PT);
        dummyDelegationEntity.setTo("To");
        dummyDelegationEntity.setFrom("From");
        dummyDelegationEntity.setInstitutionFromName("setInstitutionFromName");
        dummyDelegationEntity.setInstitutionFromRootName("setInstitutionFromRootName");
        dummyDelegationEntity.setInstitutions(List.of(dummyInstitution));
    }

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

        DelegationEntity delegationEntity = new DelegationEntity();
        delegationEntity.setId("id");
        delegationEntity.setProductId("productId");
        delegationEntity.setType(DelegationType.PT);
        delegationEntity.setTo("To");
        delegationEntity.setFrom("From");
        delegationEntity.setInstitutionFromName("setInstitutionFromName");
        delegationEntity.setInstitutionFromRootName("setInstitutionFromRootName");

        List<DelegationEntity> delegationEntities = List.of(delegationEntity);
        Page<DelegationEntity> delegationEntityPage = new PageImpl<>(delegationEntities);
        //When

        doReturn(delegationEntityPage)
                .when(delegationRepository)
                .find(any(), any(), any());

        List<Delegation> response = delegationConnectorImpl.find(delegationEntity.getFrom(),
                delegationEntity.getTo(), delegationEntity.getProductId(), null, GetDelegationsMode.NORMAL, PAGE_SIZE, MAX_PAGE_SIZE);

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
    void getBrokersFullMode() {

        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(dummyDelegationEntity));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<Delegation> response = delegationConnectorImpl.find(dummyDelegationEntity.getFrom(), null,
                dummyDelegationEntity.getProductId(), null, GetDelegationsMode.FULL, PAGE_SIZE, MAX_PAGE_SIZE);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        Delegation actual = response.get(0);

        assertEquals(actual.getId(), dummyDelegationEntity.getId());
        assertEquals(actual.getType(), dummyDelegationEntity.getType());
        assertEquals(actual.getProductId(), dummyDelegationEntity.getProductId());
        assertEquals(actual.getTo(), dummyDelegationEntity.getTo());
        assertEquals(actual.getFrom(), dummyDelegationEntity.getFrom());
        assertEquals(actual.getInstitutionFromName(), dummyDelegationEntity.getInstitutionFromName());
        assertEquals(actual.getInstitutionFromRootName(), dummyDelegationEntity.getInstitutionFromRootName());
        assertEquals(actual.getBrokerTaxCode(), dummyDelegationEntity.getInstitutions().get(0).getTaxCode());
        assertEquals(actual.getBrokerType(), dummyDelegationEntity.getInstitutions().get(0).getInstitutionType());
    }

    @Test
    void getInstitutionsFullMode() {

        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(dummyDelegationEntity));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<Delegation> response = delegationConnectorImpl.find(null, dummyDelegationEntity.getTo(),
                dummyDelegationEntity.getProductId(), null, GetDelegationsMode.FULL, PAGE_SIZE, MAX_PAGE_SIZE);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        Delegation actual = response.get(0);

        assertEquals(actual.getId(), dummyDelegationEntity.getId());
        assertEquals(actual.getType(), dummyDelegationEntity.getType());
        assertEquals(actual.getProductId(), dummyDelegationEntity.getProductId());
        assertEquals(actual.getTo(), dummyDelegationEntity.getTo());
        assertEquals(actual.getFrom(), dummyDelegationEntity.getFrom());
        assertEquals(actual.getInstitutionFromName(), dummyDelegationEntity.getInstitutionFromName());
        assertEquals(actual.getInstitutionFromRootName(), dummyDelegationEntity.getInstitutionFromRootName());
        assertEquals(actual.getTaxCode(), dummyDelegationEntity.getInstitutions().get(0).getTaxCode());
        assertEquals(actual.getInstitutionType(), dummyDelegationEntity.getInstitutions().get(0).getInstitutionType());
    }

    @Test
    void findByIdAndModifyStatus() {
        DelegationEntity delegationEntity = new DelegationEntity();
        delegationEntity.setId("id");
        delegationEntity.setStatus(DelegationState.ACTIVE);
        when(delegationRepository.findAndModify(any(), any(), any(), any())).thenReturn(delegationEntity);
        Delegation delegation = delegationConnectorImpl.findByIdAndModifyStatus(delegationEntity.getId(), DelegationState.DELETED);
        assertNotNull(delegation);
        assertEquals(delegation.getId(), delegationEntity.getId());
    }

    @Test
    void checkIfDelegationsAreActive() {
        when(delegationRepository.findByToAndStatus(anyString(), any())).thenReturn(Optional.of(List.of(new DelegationEntity())));
        boolean response = delegationConnectorImpl.checkIfDelegationsAreActive("id");
        assertTrue(response);
    }


    @Test
    void find_shouldGetDataPaginated() {

        final String FROM1 = "from1";
        final String TO1 = "to1";

        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(createAggregation("1", FROM1, TO1)));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<Delegation> response = delegationConnectorImpl.find(null,
                TO1, "productId", null, GetDelegationsMode.FULL, 0, 1);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());

        Delegation actual = response.get(0);

        assertEquals(actual.getId(), "id_1");
        assertEquals(actual.getType(), DelegationType.PT);
        assertEquals(actual.getProductId(), "productId");
        assertEquals(actual.getTo(), TO1);
        assertEquals(actual.getFrom(), FROM1);

    }

    private DelegationInstitution createAggregation(String pattern, String from, String to) {
        Institution institution = new Institution();
        institution.setTaxCode("taxCode_" + pattern);
        institution.setInstitutionType(InstitutionType.PT);
        DelegationInstitution delegationEntity = new DelegationInstitution();
        delegationEntity.setId("id_" + pattern);
        delegationEntity.setProductId("productId");
        delegationEntity.setType(DelegationType.PT);
        delegationEntity.setTo(to);
        delegationEntity.setFrom(from);
        delegationEntity.setInstitutionFromName("name_" + from);
        delegationEntity.setInstitutions(List.of(institution));
        return delegationEntity;
    }
}
