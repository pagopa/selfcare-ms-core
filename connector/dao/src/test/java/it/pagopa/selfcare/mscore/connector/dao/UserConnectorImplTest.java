package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = UserConnectorImpl.class)
@ExtendWith(SpringExtension.class)
class UserConnectorImplTest {

    @Autowired
    UserConnectorImpl userConnectorImpl;

    @MockBean
    UserRepository userRepository;

    @Test
    void findOnboardedManagerTest(){
        UserEntity user = new UserEntity();
        user.setId(new ObjectId("507f1f77bcf86cd799439011"));
        user.setBindings(new HashMap<>());
        when(userRepository.find(any(),any())).thenReturn(List.of(user));
        List<OnboardedUser> response = userConnectorImpl.findOnboardedManager("42","2");
        Assertions.assertEquals(1,response.size());
        Assertions.assertEquals("507f1f77bcf86cd799439011",response.get(0).getUser());
    }
}
