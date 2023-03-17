package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserConnectorImplTest {
    @InjectMocks
    private UserConnectorImpl userConnectorImpl;

    @Mock
    private UserRepository userRepository;

    @Test
    void findById() {
        UserEntity userEntity = new UserEntity();
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        assertNotNull(userConnectorImpl.findById("id"));
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
        doNothing().when(userRepository).deleteById("any");
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
        when(userRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(userEntity);
        Assertions.assertDoesNotThrow(() -> userConnectorImpl.findAndUpdateState("42", "42", new Token(), RelationshipState.PENDING));
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
     * Method under test: {@link UserConnectorImpl#findActiveInstitutionAdmin(String, String, List, List)}
     */
    @Test
    void testFindAdminWithFilter() {
        when(userRepository.find(any(), any())).thenReturn(new ArrayList<>());
        ArrayList<PartyRole> roles = new ArrayList<>();
        assertTrue(userConnectorImpl.findActiveInstitutionAdmin("42", "42", roles, new ArrayList<>()).isEmpty());
        verify(userRepository).find(any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findActiveInstitutionAdmin(String, String, List, List)}
     */
    @Test
    void testFindAdminWithFilter4() {
        when(userRepository.find(any(), any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "."));
        ArrayList<PartyRole> roles = new ArrayList<>();
        List<RelationshipState> states = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class,
                () -> userConnectorImpl.findActiveInstitutionAdmin("42", "42", roles, states));
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

}
