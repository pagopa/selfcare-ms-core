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
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserEntityMapperImpl;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserInstitutionAggregationMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.UserInstitutionAggregationMapperImpl;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
    @Spy
    private UserInstitutionAggregationMapper userInstitutionAggregationMapper = new UserInstitutionAggregationMapperImpl();

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(userConnectorImpl.findAll().isEmpty());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll2() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(1, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        assertEquals("42", getResult.getId());
        assertNull(getResult.getCreatedAt());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll3() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setBindings(new ArrayList<>());
        userEntity1.setCreatedAt(null);
        userEntity1.setId("Id");
        userEntity1.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity1);
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(2, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        assertEquals("Id", getResult.getId());
        OnboardedUser getResult1 = actualFindAllResult.get(1);
        assertEquals("42", getResult1.getId());
        assertNull(getResult1.getCreatedAt());
        assertNull(getResult.getCreatedAt());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll4() {
        when(userRepository.findAll()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findAll());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll5() {
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(new UserBindingEntity());

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(1, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        List<UserBinding> bindings = getResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", getResult.getId());
        assertNull(getResult.getCreatedAt());
        assertNull(bindings.get(0).getInstitutionId());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll6() {
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        userBindingEntityList.add(new UserBindingEntity("42", onboardedProductEntityList));

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(1, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        List<UserBinding> bindings = getResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", getResult.getId());
        assertNull(getResult.getCreatedAt());
        UserBinding getResult1 = bindings.get(0);
        assertEquals("42", getResult1.getInstitutionId());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll8() {
        OnboardedProductEntity onboardedProductEntity = new OnboardedProductEntity();
        onboardedProductEntity.setContract("Contract");
        onboardedProductEntity.setCreatedAt(null);
        onboardedProductEntity.setEnv(Env.ROOT);
        onboardedProductEntity.setProductId("42");
        onboardedProductEntity.setProductRole("Product Role");
        onboardedProductEntity.setRelationshipId("42");
        onboardedProductEntity.setRole(PartyRole.MANAGER);
        onboardedProductEntity.setStatus(RelationshipState.PENDING);
        onboardedProductEntity.setTokenId("42");
        onboardedProductEntity.setUpdatedAt(null);

        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        onboardedProductEntityList.add(onboardedProductEntity);
        UserBindingEntity e = new UserBindingEntity("42", onboardedProductEntityList);

        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(e);

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(1, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        List<UserBinding> bindings = getResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", getResult.getId());
        assertNull(getResult.getCreatedAt());
        UserBinding getResult1 = bindings.get(0);
        assertEquals("42", getResult1.getInstitutionId());
        assertEquals(1, getResult1.getProducts().size());
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link UserConnectorImpl#findAll()}
     */
    @Test
    void testFindAll9() {
        OnboardedProductEntity onboardedProductEntity = mock(OnboardedProductEntity.class);
        when(onboardedProductEntity.getRole()).thenReturn(PartyRole.MANAGER);
        when(onboardedProductEntity.getEnv()).thenReturn(Env.ROOT);
        when(onboardedProductEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProductEntity.getContract()).thenReturn("Contract");
        when(onboardedProductEntity.getProductId()).thenReturn("42");
        when(onboardedProductEntity.getProductRole()).thenReturn("Product Role");
        when(onboardedProductEntity.getRelationshipId()).thenReturn("42");
        when(onboardedProductEntity.getTokenId()).thenReturn("42");
        when(onboardedProductEntity.getCreatedAt()).thenReturn(null);
        when(onboardedProductEntity.getUpdatedAt()).thenReturn(null);
        doNothing().when(onboardedProductEntity).setContract(any());
        doNothing().when(onboardedProductEntity).setCreatedAt(any());
        doNothing().when(onboardedProductEntity).setEnv(any());
        doNothing().when(onboardedProductEntity).setProductId(any());
        doNothing().when(onboardedProductEntity).setProductRole(any());
        doNothing().when(onboardedProductEntity).setRelationshipId(any());
        doNothing().when(onboardedProductEntity).setRole(any());
        doNothing().when(onboardedProductEntity).setStatus(any());
        doNothing().when(onboardedProductEntity).setTokenId(any());
        doNothing().when(onboardedProductEntity).setUpdatedAt(any());
        onboardedProductEntity.setContract("Contract");
        onboardedProductEntity.setCreatedAt(null);
        onboardedProductEntity.setEnv(Env.ROOT);
        onboardedProductEntity.setProductId("42");
        onboardedProductEntity.setProductRole("Product Role");
        onboardedProductEntity.setRelationshipId("42");
        onboardedProductEntity.setRole(PartyRole.MANAGER);
        onboardedProductEntity.setStatus(RelationshipState.PENDING);
        onboardedProductEntity.setTokenId("42");
        onboardedProductEntity.setUpdatedAt(null);

        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        onboardedProductEntityList.add(onboardedProductEntity);
        UserBindingEntity e = new UserBindingEntity("42", onboardedProductEntityList);

        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(e);

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);

        ArrayList<UserEntity> userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);
        when(userRepository.findAll()).thenReturn(userEntityList);
        List<OnboardedUser> actualFindAllResult = userConnectorImpl.findAll();
        assertEquals(1, actualFindAllResult.size());
        OnboardedUser getResult = actualFindAllResult.get(0);
        List<UserBinding> bindings = getResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", getResult.getId());
        assertNull(getResult.getCreatedAt());
        UserBinding getResult1 = bindings.get(0);
        assertEquals("42", getResult1.getInstitutionId());
        assertEquals(1, getResult1.getProducts().size());
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

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser());
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave2() {
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(new UserBindingEntity());

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser());
        List<UserBinding> bindings = actualSaveResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        assertNull(bindings.get(0).getInstitutionId());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave4() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser("42", new ArrayList<>(), null));
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave5() {
        when(userRepository.save(any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        OnboardedUser onboardedUser = new OnboardedUser();
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.save(onboardedUser));
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave6() {
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        userBindingEntityList.add(new UserBindingEntity("42", onboardedProductEntityList));

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser());
        List<UserBinding> bindings = actualSaveResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        UserBinding getResult = bindings.get(0);
        assertEquals("42", getResult.getInstitutionId());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave8() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser("42", userBindingList, null));
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave9() {
        OnboardedProductEntity onboardedProductEntity = new OnboardedProductEntity();
        onboardedProductEntity.setContract("Contract");
        onboardedProductEntity.setCreatedAt(null);
        onboardedProductEntity.setEnv(Env.ROOT);
        onboardedProductEntity.setProductId("42");
        onboardedProductEntity.setProductRole("Product Role");
        onboardedProductEntity.setRelationshipId("42");
        onboardedProductEntity.setRole(PartyRole.MANAGER);
        onboardedProductEntity.setStatus(RelationshipState.PENDING);
        onboardedProductEntity.setTokenId("42");
        onboardedProductEntity.setUpdatedAt(null);

        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        onboardedProductEntityList.add(onboardedProductEntity);
        UserBindingEntity e = new UserBindingEntity("42", onboardedProductEntityList);

        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(e);

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser());
        List<UserBinding> bindings = actualSaveResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        UserBinding getResult = bindings.get(0);
        assertEquals("42", getResult.getInstitutionId());
        assertEquals(1, getResult.getProducts().size());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave10() {
        UserEntity userEntity = new UserEntity();
        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding("42", new ArrayList<>()));
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser("42", userBindingList, null));
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        verify(userRepository).save(any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#save(OnboardedUser)}
     */
    @Test
    void testSave12() {
        OnboardedProductEntity onboardedProductEntity = mock(OnboardedProductEntity.class);
        when(onboardedProductEntity.getRole()).thenReturn(PartyRole.MANAGER);
        when(onboardedProductEntity.getEnv()).thenReturn(Env.ROOT);
        when(onboardedProductEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProductEntity.getContract()).thenReturn("Contract");
        when(onboardedProductEntity.getProductId()).thenReturn("42");
        when(onboardedProductEntity.getProductRole()).thenReturn("Product Role");
        when(onboardedProductEntity.getRelationshipId()).thenReturn("42");
        when(onboardedProductEntity.getTokenId()).thenReturn("42");
        when(onboardedProductEntity.getCreatedAt()).thenReturn(null);
        when(onboardedProductEntity.getUpdatedAt()).thenReturn(null);
        onboardedProductEntity.setContract("Contract");
        onboardedProductEntity.setCreatedAt(null);
        onboardedProductEntity.setEnv(Env.ROOT);
        onboardedProductEntity.setProductId("42");
        onboardedProductEntity.setProductRole("Product Role");
        onboardedProductEntity.setRelationshipId("42");
        onboardedProductEntity.setRole(PartyRole.MANAGER);
        onboardedProductEntity.setStatus(RelationshipState.PENDING);
        onboardedProductEntity.setTokenId("42");
        onboardedProductEntity.setUpdatedAt(null);

        ArrayList<OnboardedProductEntity> onboardedProductEntityList = new ArrayList<>();
        onboardedProductEntityList.add(onboardedProductEntity);
        UserBindingEntity e = new UserBindingEntity("42", onboardedProductEntityList);

        ArrayList<UserBindingEntity> userBindingEntityList = new ArrayList<>();
        userBindingEntityList.add(e);

        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(userBindingEntityList);
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser actualSaveResult = userConnectorImpl.save(new OnboardedUser());
        List<UserBinding> bindings = actualSaveResult.getBindings();
        assertEquals(1, bindings.size());
        assertEquals("42", actualSaveResult.getId());
        assertNull(actualSaveResult.getCreatedAt());
        UserBinding getResult = bindings.get(0);
        assertEquals("42", getResult.getInstitutionId());
        assertEquals(1, getResult.getProducts().size());
        verify(userRepository).save(any());
        verify(onboardedProductEntity).getRole();
        verify(onboardedProductEntity).getEnv();
        verify(onboardedProductEntity).getStatus();
    }

    @Test
    void findAllByIds() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity());
        when(userRepository.findAllById(any())).thenReturn(userEntities);
        List<String> ids = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findAllByIds(ids));
    }

    @Test
    void findAllByIds2() {
        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setId("id");
        userEntities.add(userEntity);
        when(userRepository.findAllById(any())).thenReturn(userEntities);
        List<String> users = new ArrayList<>();
        users.add("id1");
        assertNotNull(userConnectorImpl.findAllByIds(users));
    }


    /**
     * Method under test: {@link UserConnectorImpl#deleteById}
     */
    @Test
    void testDeleteById() {
        doNothing().when(userRepository).deleteById(any());
        userConnectorImpl.deleteById("42");
        verify(userRepository).deleteById(any());
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
        ArrayList<PartyRole> roles = new ArrayList<>();
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
        ArrayList<PartyRole> roles = new ArrayList<>();
        List<RelationshipState> states = new ArrayList<>();
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
}
