package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<UserBinding> retrieveBindings(String institutionId, String userId, String[] states, List<String> products) {


        List<RelationshipState> relationshipStates = Optional.ofNullable(states)
                .map(OnboardingInfoUtils::convertStatesToRelationshipsState)
                .orElse(null);

        List<OnboardedUser> onboardingInfoList = userConnector.findWithFilter(institutionId, userId, null, relationshipStates, null, null);
        if(Objects.isNull(onboardingInfoList) || onboardingInfoList.isEmpty()) return List.of();

        OnboardedUser onboardedUser = onboardingInfoList.get(0);
        return onboardedUser.getBindings().stream()
                .peek(userBinding -> userBinding.setProducts(userBinding.getProducts().stream()
                                .filter(product -> Objects.isNull(relationshipStates) || relationshipStates.isEmpty()
                                        || relationshipStates.contains(product.getStatus()))
                        .collect(Collectors.toList())))
                .filter(userBinding -> !userBinding.getProducts().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public boolean checkIfInstitutionUser(String userId, String institutionId) {
        return !userConnector.findActiveInstitutionUser(userId, institutionId).isEmpty();
    }

    @Override
    public void verifyUser(String userId) {
        findByUserId(userId);
    }

    @Override
    public User retrieveUserFromUserRegistry(String userId, EnumSet<User.Fields> fields) {
        return userRegistryConnector.getUserByInternalId(userId, fields);
    }

    @Override
    public List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter) {
        return userConnector.findUserInstitutionAggregation(filter);
    }
}
