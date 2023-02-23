package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.CustomErrorEnum;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.USER_ALREADY_EXIST_ERROR;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.CREATE_PERSON_CONFLICT;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ADMIN_PARTY_ROLE;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserConnector userConnector;

    public UserServiceImpl(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> active) {
        return userConnector.findOnboardedManager(institutionId, productId, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public OnboardedUser findByUserId(String userId){
        return userConnector.getById(userId);
    }

    @Override
    public List<OnboardedUser> retrieveAdminUsers(String institutionId, String userId) {
        return userConnector.findWithFilter(institutionId, userId,ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE), null, null);
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return  userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public boolean checkIfAdmin(EnvEnum env, String userId, String institutionId) {
        return !userConnector.findAdminWithFilter(env, userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE)).isEmpty();
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public OnboardedUser createUser(UserToOnboard userToOnboard){
        OnboardedUser onboardedUser = userConnector.getById(userToOnboard.getId());
        if(onboardedUser != null) {
            throw new ResourceConflictException(CREATE_PERSON_CONFLICT.getMessage(), CREATE_PERSON_CONFLICT.getCode());
        }
        OnboardedUser newOnboardedUser = new OnboardedUser();
        newOnboardedUser.setId(userToOnboard.getId());
        newOnboardedUser.setCreatedAt(OffsetDateTime.now());
        return userConnector.save(newOnboardedUser);
    }

    @Override
    public void verifyUser(String userId) {
        OnboardedUser user = findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(CustomErrorEnum.USER_NOT_FOUND_ERROR.getMessage(), userId), CustomErrorEnum.USER_NOT_FOUND_ERROR.getCode());
        }
    }
}
