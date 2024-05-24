package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapperImpl;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapperImpl;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.delegation.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
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
        delegation.setStatus(DelegationState.DELETED);
        when(delegationRepository.findByFromAndToAndProductIdAndTypeAndStatus(any(), any(), any(), any(), any())).thenReturn(Optional.of(new DelegationEntity()));
        boolean response = delegationConnectorImpl.checkIfExistsWithStatus(delegation, DelegationState.DELETED);
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
                delegationEntity.getTo(), delegationEntity.getProductId(), null, null, Order.NONE, PAGE_SIZE, MAX_PAGE_SIZE);

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
        assertEquals(actual.getToTaxCode(), delegationEntity.getToTaxCode());
        assertEquals(actual.getFromTaxCode(), delegationEntity.getFromTaxCode());
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
    void checkIfDelegationsAreActive_true() {
        when(delegationRepository.findByToAndStatus(anyString(), any())).thenReturn(Optional.of(List.of(new DelegationEntity())));
        boolean response = delegationConnectorImpl.checkIfDelegationsAreActive("id");
        assertTrue(response);
    }

    @Test
    void checkIfDelegationsAreActive_false() {
        when(delegationRepository.findByToAndStatus(anyString(), any())).thenReturn(Optional.of(Collections.emptyList()));
        boolean response = delegationConnectorImpl.checkIfDelegationsAreActive("id");
        assertFalse(response);
    }


    @Test
    void find_shouldGetDataPaginated() {
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

        List<Delegation> response = delegationConnectorImpl.find(null, delegationEntity.getTo(), "productId",
                null, null, Order.NONE, 0, 1);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());

        Delegation actual = response.get(0);

        assertEquals(actual.getId(), delegationEntity.getId());
        assertEquals(actual.getType(), delegationEntity.getType());
        assertEquals(actual.getProductId(), delegationEntity.getProductId());
        assertEquals(actual.getTo(), delegationEntity.getTo());
        assertEquals(actual.getFrom(), delegationEntity.getFrom());
        assertEquals(actual.getFromTaxCode(), delegationEntity.getFromTaxCode());
        assertEquals(actual.getToTaxCode(), delegationEntity.getToTaxCode());

    }

    @Test
    void findAndActivate() {
        DelegationEntity delegationEntity = new DelegationEntity();
        delegationEntity.setId("id");
        delegationEntity.setFrom("from");
        delegationEntity.setTo("to");
        delegationEntity.setProductId("prod-io");
        delegationEntity.setStatus(DelegationState.ACTIVE);
        when(delegationRepository.findAndModify(any(), any(), any(), any())).thenReturn(delegationEntity);
        Delegation delegation = delegationConnectorImpl.findAndActivate(delegationEntity.getFrom(), delegationEntity.getTo(), delegationEntity.getProductId());
        assertNotNull(delegation);
        assertEquals(delegation.getId(), delegationEntity.getId());
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

    @Test
    void findAndCount_shouldGetData() {

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

        Pageable pageable = PageRequest.of(PAGE_SIZE, MAX_PAGE_SIZE);
        Page<Delegation> result = PageableExecutionUtils.getPage(List.of(new Delegation()), pageable, () -> 1L);
        PageInfo expectedPageInfo = new PageInfo(result.getSize(), result.getNumber(), result.getTotalElements(), result.getTotalPages());

        //When

        doReturn(delegationEntityPage)
                .when(delegationRepository)
                .find(any(), any(), any());

        doReturn(1L)
                .when(mongoTemplate)
                .count(any(), eq(DelegationEntity.class));

        DelegationWithPagination response = delegationConnectorImpl.findAndCount(createDelegationParameters(delegationEntity.getFrom(),
                delegationEntity.getTo(), delegationEntity.getProductId(), null, null, Order.NONE, PAGE_SIZE, MAX_PAGE_SIZE));

        //Then
        assertNotNull(response);
        assertNotNull(response.getDelegations());
        assertNotNull(response.getPageInfo());
        assertFalse(response.getDelegations().isEmpty());
        Delegation actualDelegation = response.getDelegations().get(0);
        PageInfo actualPageInfo = response.getPageInfo();

        assertEquals(actualDelegation.getId(), delegationEntity.getId());
        assertEquals(actualDelegation.getType(), delegationEntity.getType());
        assertEquals(actualDelegation.getProductId(), delegationEntity.getProductId());
        assertEquals(actualDelegation.getTo(), delegationEntity.getTo());
        assertEquals(actualDelegation.getFrom(), delegationEntity.getFrom());
        assertEquals(actualDelegation.getInstitutionFromName(), delegationEntity.getInstitutionFromName());
        assertEquals(actualDelegation.getInstitutionFromRootName(), delegationEntity.getInstitutionFromRootName());
        assertEquals(actualPageInfo, expectedPageInfo);
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
