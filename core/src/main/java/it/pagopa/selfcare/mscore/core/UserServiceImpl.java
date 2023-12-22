package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.USER_NOT_FOUND_ERROR;

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
    public List<OnboardedUser> findAllExistingByIds(List<String> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return userConnector.findAllByExistingIds(users);
    }

    @Override
    public List<UserBinding> retrieveBindings(String institutionId, String userId, String[] states, List<String> products) {


        List<RelationshipState> relationshipStates = Optional.ofNullable(states)
                .map(OnboardingInfoUtils::convertStatesToRelationshipsState)
                .orElse(null);

        List<OnboardedUser> onboardingInfoList = userConnector.findWithFilter(institutionId, userId, null, relationshipStates, null, null);
        if (Objects.isNull(onboardingInfoList) || onboardingInfoList.isEmpty()) return List.of();

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
    public User retrieveUserFromUserRegistry(String userId) {
        return userRegistryConnector.getUserByInternalIdWithFiscalCode(userId);
    }

    @Override
    public User retrieveUserFromUserRegistryByFiscalCode(String fiscalCode) {
        return userRegistryConnector.getUserByFiscalCode(fiscalCode);
    }

    @Override
    public User persistUserRegistry(String name, String familyName, String fiscalCode, String email, String institutionId) {
        return userRegistryConnector.persistUserUsingPatch(name ,familyName ,fiscalCode ,email , institutionId);
    }

    @Override
    public User persistWorksContractToUserRegistry(String fiscalCode, String email, String institutionId) {
        return userRegistryConnector.persistUserWorksContractUsingPatch(fiscalCode ,email , institutionId);
    }

    @Override
    public List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter) {
        return userConnector.findUserInstitutionAggregation(filter);
    }

    @Override
    public void findAndUpdateStateByInstitutionAndProduct(String userId, String institutionId, String productId, RelationshipState state) {
        userConnector.findAndUpdateStateByInstitutionAndProduct(userId, institutionId, productId, state);
    }

    @Override
    public User retrievePerson(String userId, String productId, String institutionId) {
        OnboardedUser onboardedUser = userConnector.findById(userId);
        if (StringUtils.hasText(productId) && !verifyBindings(onboardedUser, productId)) {
            log.error(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId));
            throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
        }
        return userRegistryConnector.getUserByInternalIdWithFiscalCode(userId);
    }

    @Override
    public List<OnboardingInfo> getUserInfo(String userId, String institutionId, String[] states) {
        List<OnboardingInfo> onboardingInfos = new ArrayList<>();
        List<UserInstitutionAggregation> userInfos = userConnector.getUserInfo(userId, institutionId, states);
        userInfos.forEach(userBinding -> onboardingInfos.add(new OnboardingInfo(userId, userBinding.getInstitutions().get(0), userBinding.getBindings())));
        return onboardingInfos;
    }

    private boolean verifyBindings(OnboardedUser onboardedUser, String productId) {
        return onboardedUser.getBindings().stream()
                .anyMatch(binding -> existsProduct(binding, productId));
    }

    private boolean existsProduct(UserBinding binding, String productId) {
        return binding.getProducts().stream()
                .anyMatch(product -> productId.equalsIgnoreCase(product.getProductId()));
    }
}
