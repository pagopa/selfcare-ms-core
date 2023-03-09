package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.CustomErrorEnum;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipPage;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        return userConnector.findById(userId);
    }

    @Override
    public List<OnboardedUser> findAllByIds(List<String> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return userConnector.findAllByIds(users);
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public RelationshipPage retrievePagedUsers(String institutionId, String personId,
                                               List<PartyRole> roles, List<RelationshipState> states,
                                               List<String> products, List<String> productRoles,
                                               Pageable pageable) {
        return userConnector.findPagedWithFilter(institutionId, personId, roles, states, products, productRoles, pageable);
    }

    @Override
    public boolean checkIfAdmin(String userId, String institutionId) {
        return !userConnector.findActiveInstitutionAdmin(userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE)).isEmpty();
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

    @Override
    public OnboardedUser createUser(String userId) {
        return userConnector.findAndCreate(userId, null);
    }
}
