package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;

import java.util.List;

public interface UserService {

    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);

    OnboardedUser findByUserId(String id);

    List<OnboardedUser> findAllByIds(List<String> users);

    List<OnboardedUser> findAllExistingByIds(List<String> users);
    List<UserBinding> retrieveBindings(String institutionId, String userId, String[] states, List<String> products);

    List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    boolean checkIfInstitutionUser(String userId, String institutionId);

    void verifyUser(String userId);

    User retrieveUserFromUserRegistry(String userId);

    User retrieveUserFromUserRegistryByFiscalCode(String fiscalCode);

    User persistUserRegistry(String name, String familyName, String fiscalCode, String email, String institutionId);

    List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter);

    void findAndUpdateStateByInstitutionAndProduct(String userId, String institutionId, String productId, RelationshipState state);

    User retrievePerson(String userId, String productId, String institutionId);
}
