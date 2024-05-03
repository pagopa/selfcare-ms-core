package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;

import java.util.List;
import java.util.Optional;

public interface UserService {

    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);

    List<OnboardedUser> findAllByIds(List<String> users);

    List<UserNotificationToSend>  findAll(Optional<Integer> size, Optional<Integer> page, String productId);

    List<OnboardedUser> findAllExistingByIds(List<String> users);

    List<UserBinding> retrieveBindings(String institutionId, String userId, String[] states, List<String> products);

    boolean checkIfInstitutionUser(String userId, String institutionId);

    User retrieveUserFromUserRegistry(String userId);

    User retrieveUserFromUserRegistryByFiscalCode(String fiscalCode);

    User persistUserRegistry(String name, String familyName, String fiscalCode, String email, String institutionId);

    User persistWorksContractToUserRegistry(String fiscalCode, String email, String institutionId);

    List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter);

    User retrievePerson(String userId, String productId, String institutionId);

    List<OnboardingInfo> getUserInfo(String userId, String institutionId, String[] states);

    void updateUserStatus(String userId, String institutionId, String productId, PartyRole role, String productRole, RelationshipState status);
}
