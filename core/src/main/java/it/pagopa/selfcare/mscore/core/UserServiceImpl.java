package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public List<OnboardedUser> retrieveAdminUsers(String institutionId, String userId, EnvEnum env) {
        return userConnector.findAdminWithFilter(env, userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, EnvEnum env, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return  userConnector.findWithFilter(env, institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public boolean checkIfAdmin(EnvEnum env, String userId, String institutionId) {
        return !userConnector.findAdminWithFilter(env, userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE)).isEmpty();
    }
}
