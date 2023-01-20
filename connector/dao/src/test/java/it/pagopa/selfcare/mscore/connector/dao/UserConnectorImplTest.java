package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserConnectorImplTest {

    @InjectMocks
    UserConnectorImpl userConnectorImpl;

    @Mock
    UserRepository userRepository;

    @Test
    void findOnboardedManagerTest(){
        UserEntity user = new UserEntity();
        user.setId(new ObjectId("507f1f77bcf86cd799439011"));
        user.setBindings(new HashMap<>());
        when(userRepository.find(any(),any())).thenReturn(List.of(user));
        List<OnboardedUser> response = userConnectorImpl.findOnboardedManager("42","2");
        Assertions.assertEquals(1,response.size());
    }


    @Test
    void save(){
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new HashMap<>());
        onboardedUser.setUser("507f1f77bcf86cd799439011");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        userEntity.setBindings(new HashMap<>());
        when(userRepository.save(any())).thenReturn(userEntity);
        OnboardedUser response = userConnectorImpl.save(onboardedUser);
        Assertions.assertEquals("507f1f77bcf86cd799439011",response.getUser());
    }
}
