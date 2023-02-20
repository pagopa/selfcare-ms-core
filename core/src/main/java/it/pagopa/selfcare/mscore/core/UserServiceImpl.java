package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.adminPartyRole;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    public UserServiceImpl(UserConnector userConnector, UserRegistryConnector userRegistryConnector) {
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String externalId, String productId, List<RelationshipState> active) {
        List<OnboardedUser> list = userConnector.findOnboardedManager(externalId, productId, List.of(RelationshipState.ACTIVE));
        if (list != null && !list.isEmpty()) {
            log.debug("retrieve first element of tokenList --> id: {}",list.get(0).getId());
            return list.get(0);
        }
        throw new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), externalId, productId),
                GET_INSTITUTION_MANAGER_NOT_FOUND.getCode());
    }

    @Override
    public OnboardedUser createUser(UserToOnboard userToOnboard){
        OnboardedUser onboardedUser = userConnector.getById(userToOnboard.getId());
        if(onboardedUser != null) {
            throw new InvalidRequestException(String.format(USER_ALREADY_EXIST_ERROR.getMessage(), onboardedUser.getId()), USER_ALREADY_EXIST_ERROR.getCode());
        }
        OnboardedUser newOnboardedUser = new OnboardedUser();
        newOnboardedUser.setId(userToOnboard.getId());
        newOnboardedUser.setCreatedAt(OffsetDateTime.now());
        return userConnector.save(newOnboardedUser);
    }

    @Override
    public User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields) {
        return userRegistryConnector.getUserByInternalId(userId, fields);
    }

    @Override
    public boolean checkIfAdmin(EnvEnum env, String userId, String institutionId) {
        return !userConnector.findAdminWithFilter(env, userId, institutionId, adminPartyRole, List.of(RelationshipState.ACTIVE)).isEmpty();
    }

    @Override
    public List<OnboardedUser> retrieveAdminUsers(String institutionId, String userId, EnvEnum env) {
        return userConnector.findAdminWithFilter(env, userId, institutionId, adminPartyRole, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, EnvEnum env, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return  userConnector.findWithFilter(env, institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public OnboardedUser findByUserId(String userId){
        return userConnector.getById(userId);
    }
}
