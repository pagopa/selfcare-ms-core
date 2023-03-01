package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.CustomErrorEnum;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ADMIN_PARTY_ROLE;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    public UserServiceImpl(UserConnector userConnector, UserRegistryConnector userRegistryConnector) {
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> active) {
        return userConnector.findOnboardedManager(institutionId, productId, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public OnboardedUser findByUserId(String userId) {
        return userConnector.getById(userId);
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public boolean checkIfAdmin(String userId, String institutionId) {
        return !userConnector.findAdminWithFilter(userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE)).isEmpty();
    }

    @Override
    public void verifyUser(String userId) {
        OnboardedUser user = findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(CustomErrorEnum.USER_NOT_FOUND_ERROR.getMessage(), userId), CustomErrorEnum.USER_NOT_FOUND_ERROR.getCode());
        }
    }

    @Override
    public User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields) {
        return userRegistryConnector.getUserByInternalId(userId, fields);
    }
}
