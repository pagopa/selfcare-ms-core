package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionAggregationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.*;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.aggregation.QueryCount;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.entity.ProductRole;
import it.pagopa.selfcare.product.entity.ProductRoleInfo;
import it.pagopa.selfcare.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserConnectorImplTest {
    @InjectMocks
    private UserConnectorImpl userConnectorImpl;

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserEntityMapper userMapper = new UserEntityMapperImpl();

    @Mock
    private ProductService productService;

    @Spy
    private OnboardedProductMapper productMapper = new OnboardedProductMapperImpl();

    @Spy
    private UserInstitutionAggregationMapper userInstitutionAggregationMapper = new UserInstitutionAggregationMapperImpl();

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;
    @Captor
    ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    @Mock
    private MongoTemplate mongoTemplate;

    static UserInstitutionAggregation dummyUserInstitution;

    static UserEntity dummyUserEntity;

    static {
        dummyUserInstitution = new UserInstitutionAggregation();
        dummyUserInstitution.setId("userId");
        Institution institution = new Institution();
        institution.setId("id");
        dummyUserInstitution.setInstitutions(List.of(institution));
        UserInstitutionBinding bindings = new UserInstitutionBinding();
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("prod-io");
        bindings.setProducts(onboardedProduct);
        dummyUserInstitution.setBindings(bindings);

        dummyUserEntity = new UserEntity();
        UserBindingEntity binding = new UserBindingEntity();
        OnboardedProductEntity onboardedProductEntity = new OnboardedProductEntity();
        onboardedProductEntity.setStatus(RelationshipState.ACTIVE);
        onboardedProductEntity.setProductId("product");
        binding.setProducts(List.of(onboardedProductEntity));
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        dummyUserEntity.setBindings(userBindingEntityList);
    }

    @Test
    void findAllPaged(){
        //given
        final String productId = "product";
        final Integer page = 0;
        final Integer size = 1;
        Page<UserEntity> userEntities = new PageImpl<>(List.of(dummyUserEntity));

        doReturn(userEntities).when(userRepository).find(any(), any(), any());
        //when
        List<OnboardedUser> users = userConnectorImpl.findAllValidUsers(page, size, productId);
        //then
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), eq(UserEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertTrue(capturedQuery.getQueryObject().toString().contains(productId));
    }

    @Test
    void findAllPagedWithEmptyProductId(){
        //given
        final Integer page = 0;
        final Integer size = 1;
        dummyUserEntity.getBindings().forEach(obj -> obj.getProducts().get(0).setProductId(null));
        Page<UserEntity> userEntities = new PageImpl<>(List.of(dummyUserEntity));
        doReturn(userEntities).when(userRepository).find(any(), any(), any());
        //when
        List<OnboardedUser> users = userConnectorImpl.findAllValidUsers(page, size, null);
        //then
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), eq(UserEntity.class));
    }

    private UserBindingEntity dummyUserBindingEntity() {
        UserBindingEntity dummyUserBindingEntity = new UserBindingEntity();
        dummyUserBindingEntity.setInstitutionId("42");
        dummyUserBindingEntity.setProducts(new ArrayList<>());
        return dummyUserBindingEntity;
    }

    @Test
    void findById() {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        assertNotNull(userConnectorImpl.findById("id"));
    }

    @Test
    void findById2() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findById("id"));
    }

    @Test
    void findAllByIds() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id");
        userEntities.add(userEntity);
        when(userRepository.findAllById(any())).thenReturn(userEntities);
        List<String> ids = new ArrayList<>();
        ids.add("id1");
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findAllByIds(ids));
    }

    @Test
    void findAllByIds2() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id1");
        userEntities.add(userEntity);
        when(userRepository.findAllById(any())).thenReturn(userEntities);
        List<String> users = new ArrayList<>();
        users.add("id1");
        assertNotNull(userConnectorImpl.findAllByIds(users));
    }

    @Test
    void findAllExistingByIds2() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id1");
        userEntities.add(userEntity);
        when(userRepository.findAllById(any())).thenReturn(userEntities);
        List<String> users = new ArrayList<>();
        users.add("id1");
        assertNotNull(userConnectorImpl.findAllByExistingIds(users));
    }

    @Test
    void testFindAndUpdateState() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        Token token = new Token();
        token.setContractSigned("contract signed");
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(userEntity);
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateState("42", "42", token, RelationshipState.PENDING));
    }

    @Test
    void findAndUpdateStateByInstitutionAndProduct() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(new UserEntity());
        Product product = mock(Product.class);
        EnumMap<it.pagopa.selfcare.onboarding.common.PartyRole, ProductRoleInfo> map = mock(EnumMap.class);
        ProductRoleInfo productRoleInfo = mock(ProductRoleInfo.class);
        ProductRole productRole = mock(ProductRole.class);
        when(productRole.getCode()).thenReturn("productRole");
        when(productRoleInfo.getRoles()).thenReturn(List.of(productRole));
        when(map.get(it.pagopa.selfcare.onboarding.common.PartyRole.DELEGATE)).thenReturn(productRoleInfo);
        when(product.getRoleMappings()).thenReturn(map);

        when(productService.getProduct(anyString())).thenReturn(product);
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateStateWithOptionalFilter("42", "42", "productId", PartyRole.DELEGATE, "productRole", RelationshipState.PENDING));
    }

    @Test
    void findAndUpdateStateByInstitutionId() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(new UserEntity());
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateStateWithOptionalFilter("42", "42", null, null, null, RelationshipState.PENDING));
    }

    @Test
    void findAndUpdateStateByProductId() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(new UserEntity());
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateStateWithOptionalFilter("42", null, "productId", null, null, RelationshipState.PENDING));
    }

    @Test
    void findAndUpdateStateWithoutFilter() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(new UserEntity());
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateStateWithOptionalFilter("42", null, null, null, null, RelationshipState.PENDING));
    }

    @Test
    void findAndUpdateStateByInstitutionAndProductWithoutProductRole() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(new UserEntity());
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateStateWithOptionalFilter("42", "42", "productId", PartyRole.DELEGATE, null, RelationshipState.PENDING));
    }


    @Test
    void testFindAndUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(), any(), any(), any())).thenReturn(userEntity);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("role");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("42");
        userBinding.setProducts(new ArrayList<>());

        OnboardedUser user = new OnboardedUser();
        List<UserBinding> bindings = new ArrayList<>();
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        bindings.add(binding);
        user.setBindings(bindings);
        userConnectorImpl.findAndUpdate(user, "42", "42", onboardedProduct, userBinding);
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    @Test
    void testFindAndUpdate2() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(), any(), any(), any())).thenReturn(userEntity);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("role");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("42");
        userBinding.setProducts(new ArrayList<>());

        OnboardedUser user = new OnboardedUser();
        user.setBindings(new ArrayList<>());
        userConnectorImpl.findAndUpdate(user, "42", "42", onboardedProduct, userBinding);
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    @Test
    void testFindAndCreate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(userEntity);
        userConnectorImpl.findAndCreate("42", new UserBinding());
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    @Test
    void testFindAndCreate2() {
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserBinding userBinding = new UserBinding();
        assertThrows(ResourceNotFoundException.class,
                () -> userConnectorImpl.findAndCreate("42", userBinding));
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    @Test
    void testFindAndCreate3() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(userEntity);
        userConnectorImpl.findAndCreate("42", null);
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        List<RelationshipState> states = new ArrayList<>();
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findOnboardedManager("42", "42", states));
    }

    /**
     * Method under test: {@link UserConnector#findActiveInstitutionUser(String, String)}
     */
    @Test
    void testFindAdminWithFilter() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        assertTrue(userConnectorImpl.findActiveInstitutionUser("42", "42").isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnector#findActiveInstitutionUser(String, String)}
     */
    @Test
    void testFindAdminWithFilter4() {
        when(userRepository.find(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "."));
        assertThrows(ResourceNotFoundException.class,
                () -> userConnectorImpl.findActiveInstitutionUser("42", "42"));
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(userConnectorImpl.findWithFilter("42", "42", roles, states, products, new ArrayList<>()).isEmpty());
        verify(userRepository).find(any(), any());
    }


    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter4() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());

        ArrayList<PartyRole> partyRoleList = new ArrayList<>();
        partyRoleList.add(PartyRole.MANAGER);
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        assertTrue(
                userConnectorImpl.findWithFilter("42", "42", partyRoleList, states, products, new ArrayList<>()).isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter5() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        ArrayList<PartyRole> roles = new ArrayList<>();

        ArrayList<RelationshipState> relationshipStateList = new ArrayList<>();
        relationshipStateList.add(RelationshipState.PENDING);
        ArrayList<String> products = new ArrayList<>();
        assertTrue(userConnectorImpl.findWithFilter("42", "42", roles, relationshipStateList, products, new ArrayList<>())
                .isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter6() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add(".");
        assertTrue(userConnectorImpl.findWithFilter("42", "42", roles, states, stringList, new ArrayList<>()).isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter7() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add(".");
        assertTrue(userConnectorImpl.findWithFilter("42", "42", roles, states, products, stringList).isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findWithFilter(String, String, List, List, List, List)}
     */
    @Test
    void testFindWithFilter8() {
        when(userRepository.find(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "."));
        ArrayList<PartyRole> roles = new ArrayList<>();
        ArrayList<RelationshipState> states = new ArrayList<>();
        ArrayList<String> products = new ArrayList<>();
        List<String> productRoles = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userConnectorImpl.findWithFilter("42", "42", roles, states, products, productRoles));
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findByRelationshipId("42"));
        verify(userRepository).find(any(), any());
    }


    /**
     * Method under test: {@link UserConnectorImpl#findByRelationshipId(String)}
     */
    @Test
    void testFindByRelationshipId3() {
        when(userRepository.find(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findByRelationshipId("42"));
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAndRemoveProduct(String, String, OnboardedProduct)}
     */
    @Test
    void testFindAndRemoveProduct() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(userEntity);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);
        userConnectorImpl.findAndRemoveProduct("42", "42", onboardedProduct);
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAndRemoveProduct(String, String, OnboardedProduct)}
     */
    @Test
    void testFindAndRemoveProduct2() {
        when(userRepository.findAndModify(any(), any(), any(),
                any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "$[currentUserBinding]"));

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);
        assertThrows(ResourceNotFoundException.class,
                () -> userConnectorImpl.findAndRemoveProduct("42", "42", onboardedProduct));
        verify(userRepository).findAndModify(any(), any(), any(), any());
    }

    @Test
    void updateOnboardedProductCreatedAt() {
        // Given
        String institutionIdMock = "InstitutionIdMock";
        String productIdMock = "ProductIdMock";
        List<String> usersIdMock = List.of("UserId2");
        OffsetDateTime createdAt = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");

        UserBindingEntity userBindingEntityMock1 = mockInstance(new UserBindingEntity());
        userBindingEntityMock1.setProducts(List.of(mockInstance(new OnboardedProductEntity())));
        OnboardedProductEntity onboardedProductEntity1 = mockInstance(new OnboardedProductEntity());
        OnboardedProductEntity onboardedProductEntity2 = mockInstance(new OnboardedProductEntity());
        onboardedProductEntity2.setProductId(productIdMock);
        OnboardedProductEntity onboardedProductEntity3 = mockInstance(new OnboardedProductEntity(), 3);
        UserBindingEntity userBindingEntityMock2 = mockInstance(new UserBindingEntity());
        userBindingEntityMock2.setInstitutionId(institutionIdMock);
        userBindingEntityMock2.setProducts(List.of(onboardedProductEntity1, onboardedProductEntity2, onboardedProductEntity3));
        UserEntity updatedUserEntityMock = mockInstance(new UserEntity());
        OnboardingEntity onboardingEntityMock = mockInstance(new OnboardingEntity());
        onboardingEntityMock.setProductId(productIdMock);

        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedUserEntityMock);
        // When
        List<OnboardedUser> result = userConnectorImpl.updateUserBindingCreatedAt(institutionIdMock, productIdMock, usersIdMock, createdAt);
        // Then
        assertFalse(result.isEmpty());
        assertEquals(usersIdMock.size(), result.size());
        verify(userRepository, times(2))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(UserEntity.class));
        List<Query> capturedQuery = queryArgumentCaptor.getAllValues();
        assertEquals(2, capturedQuery.size());
        assertSame(capturedQuery.get(0).getQueryObject().get(UserEntity.Fields.id.name()), usersIdMock.get(0));
        assertSame(capturedQuery.get(1).getQueryObject().get(UserEntity.Fields.id.name()), usersIdMock.get(0));
        assertEquals(2, updateArgumentCaptor.getAllValues().size());
        Update updateUserBindingCreatedAt = updateArgumentCaptor.getAllValues().get(0);
        Update updateUserEntityUpdatedAt = updateArgumentCaptor.getAllValues().get(1);
        assertEquals(2, updateUserBindingCreatedAt.getArrayFilters().size());
        assertTrue(updateUserEntityUpdatedAt.getArrayFilters().isEmpty());
        assertTrue(updateUserEntityUpdatedAt.getUpdateObject().get("$set").toString().contains(InstitutionEntity.Fields.updatedAt.name()));
        assertTrue(updateUserBindingCreatedAt.getUpdateObject().get("$set").toString().contains("bindings.$[currentUserBinding].products.$[current].createdAt") &&
                updateUserBindingCreatedAt.getUpdateObject().get("$set").toString().contains("bindings.$[currentUserBinding].products.$[current].updatedAt") &&
                updateUserBindingCreatedAt.getUpdateObject().get("$set").toString().contains(createdAt.toString()));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findUserInstitutionAggregation() {
        UserInstitutionAggregationEntity entity = new UserInstitutionAggregationEntity();
        entity.setInstitutions(List.of(mock(InstitutionEntity.class)));
        entity.setBindings(mock(UserInstitutionBindingEntity.class));
        entity.setId("UserId");
        UserInstitutionFilter filter = new UserInstitutionFilter();
        filter.setUserId("UserId");
        when(userRepository.findUserInstitutionAggregation(any(), any()))
                .thenReturn(List.of(mockInstance(new UserInstitutionAggregationEntity())));
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findUserInstitutionAggregation(filter));
    }

    @Test
    void findByInstitutionId() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id");
        when(userRepository.find(any(), any()))
                .thenReturn(List.of(userEntity));
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findByInstitutionId("institutionId"));
    }

    @Test
    void getUserInfo() {
        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(dummyUserInstitution));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<UserInstitutionAggregation> response = userConnectorImpl.getUserInfo("userId", null, null);

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        UserInstitutionAggregation actual = response.get(0);

        assertEquals(actual.getId(), dummyUserInstitution.getId());
        assertEquals(actual.getInstitutions().get(0).getId(), dummyUserInstitution.getInstitutions().get(0).getId());
        assertEquals(actual.getBindings().getProducts().getProductId(), dummyUserInstitution.getBindings().getProducts().getProductId());
    }

    @Test
    void getUserInfoWithFilters() {
        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);
        when(results.getMappedResults()).thenReturn(List.of(dummyUserInstitution));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<UserInstitutionAggregation> response = userConnectorImpl.getUserInfo("userId", "id", new String[]{"PENDING"});

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        UserInstitutionAggregation actual = response.get(0);

        assertEquals(actual.getId(), dummyUserInstitution.getId());
        assertEquals(actual.getInstitutions().get(0).getId(), dummyUserInstitution.getInstitutions().get(0).getId());
        assertEquals(actual.getBindings().getProducts().getProductId(), dummyUserInstitution.getBindings().getProducts().getProductId());
    }

    @Test
    void findUsersByInstitutionIdAndProductId(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id");
        when(userRepository.find(any(), any())).thenReturn(List.of(userEntity));
        List<String> userIds = userConnectorImpl.findUsersByInstitutionIdAndProductId("institutionId", "productId");
        assertEquals(1, userIds.size());
    }


    @Test
    void countUser() {
        //Given
        AggregationResults<Object> results = mock(AggregationResults.class);

        when(results.getMappedResults()).thenReturn(List.of(
                new QueryCount("prod1", 1),
                new QueryCount("prod2", 2),
                new QueryCount("prod3", 3)));

        //When
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(),  any())).
                thenReturn(results);

        List<QueryCount> response = userConnectorImpl.countUsers();

        //Then
        assertNotNull(response);
        assertFalse(response.isEmpty());
        QueryCount actual = response.get(0);

        assertEquals(actual.getCount(), 1);
    }

}
