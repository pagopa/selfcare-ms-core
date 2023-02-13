package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.USER_ALREADY_EXIST_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.USER_NOT_FOUND_ERROR;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    private final UserConnector userConnector;

    public PersonServiceImpl(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public OnboardedUser createUser(OnboardedUser onboardedUser){
        List<OnboardedUser> onboardedUsers = userConnector.getByUser(onboardedUser.getUser());
        if(!onboardedUsers.isEmpty())
            throw new ResourceNotFoundException(String.format(USER_ALREADY_EXIST_ERROR.getMessage(),onboardedUser.getUser()), USER_ALREADY_EXIST_ERROR.getCode());
        onboardedUser.setCreatedAt(OffsetDateTime.now());
        return userConnector.save(onboardedUser);
    }

    @Override
    public OnboardedUser findByUserId(String userId){
        List<OnboardedUser> onboardedUsers = userConnector.getByUser(userId);
        if(onboardedUsers.isEmpty())
            throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(),userId), USER_NOT_FOUND_ERROR.getCode());
        return onboardedUsers.get(0);
    }


}
