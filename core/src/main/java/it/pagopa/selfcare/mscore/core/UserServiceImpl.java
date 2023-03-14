package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserConnector userConnector;

    public UserServiceImpl(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> active) {
        return userConnector.findOnboardedManager(institutionId, productId, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public OnboardedUser findByUserId(String userId) {
        return userConnector.findById(userId);
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }


    @Override
    public void verifyUser(String userId) {
        OnboardedUser user = findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(CustomError.USER_NOT_FOUND_ERROR.getMessage(), userId), CustomError.USER_NOT_FOUND_ERROR.getCode());
        }
    }
}
