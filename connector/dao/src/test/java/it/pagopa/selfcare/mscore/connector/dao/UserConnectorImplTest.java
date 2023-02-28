package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
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


    /**
     * Method under test: {@link UserConnectorImpl#deleteById}
     */
    @Test
    void testDeleteById() {
        doNothing().when(userRepository).deleteById("any");
        userConnectorImpl.deleteById("42");
        verify(userRepository).deleteById( any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#getById}
     */
    @Test
    void testGetById() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        when(userRepository.findById( any())).thenReturn(ofResult);
        OnboardedUser actualById = userConnectorImpl.getById("42");
        assertTrue(actualById.getBindings().isEmpty());
        assertNull(actualById.getUpdatedAt());
        assertEquals("42", actualById.getId());
        assertNull(actualById.getCreatedAt());
        verify(userRepository).findById( any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#getById}
     */
    @Test
    void testGetById2() {
        when(userRepository.findById( any())).thenReturn(Optional.empty());
        assertNull(userConnectorImpl.getById("42"));
        verify(userRepository).findById( any());
    }

    @Test
    void testFindAndUpdate() {
        UserEntity userEntity = new UserEntity();
        userEntity.setBindings(new ArrayList<>());
        userEntity.setCreatedAt(null);
        userEntity.setId("42");
        userEntity.setUpdatedAt(null);
        when(userRepository.findAndModify(any(),  any(),  any(), any())).thenReturn(userEntity);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(EnvEnum.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRoles(new ArrayList<>());
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        UserBinding userBinding = new UserBinding();
        userBinding.setCreatedAt(null);
        userBinding.setInstitutionId("42");
        userBinding.setProducts(new ArrayList<>());

        OnboardedUser user = new OnboardedUser();
        List<UserBinding> bindings = new ArrayList<>();
        UserBinding binding = new UserBinding();
        binding.setInstitutionId("42");
        bindings.add(binding);
        user.setBindings(bindings);
        userConnectorImpl.findAndUpdate(user, "42", "42", onboardedProduct, userBinding);
        verify(userRepository).findAndModify(any(),  any(),  any(), any());
    }

    /**
     * Method under test: {@link UserConnectorImpl#findOnboardedManager(String, String, List)}
     */
    @Test
    void testFindOnboardedManager() {
        when(userRepository.find(any(),any())).thenReturn(new ArrayList<>());
        List<RelationshipState> states = new ArrayList<>();
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userConnectorImpl.findOnboardedManager("42", "42", states));
    }

}

