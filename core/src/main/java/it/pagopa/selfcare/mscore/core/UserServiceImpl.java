package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserConnector userConnector;

    public UserServiceImpl(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public boolean verifyPerson(String id) {
        return userConnector.existsById(id);
    }

    @Override
    public OnboardedUser retrievePerson(String id) {
        Optional<OnboardedUser> opt = userConnector.findById(id);
        if(opt.isPresent())
            return opt.get();
        else
            throw new ResourceNotFoundException("Person not found","");
    }

    @Override
    public OnboardedUser createPerson(OnboardedUser onboardedUser) {
        return userConnector.save(onboardedUser);
    }
}
