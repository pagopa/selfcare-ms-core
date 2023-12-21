package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserInfo;
import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public interface UserConnector {

    OnboardedUser save(OnboardedUser example);

    List<OnboardedUser> findAll();
    List<OnboardedUser> findAllValidUsers(Integer page, Integer size, String productId);

    void deleteById(String id);

    OnboardedUser findById(String userId);

    /**
     * <b>Necessario specificare almeno uno tra relationshipId e tokenId.</b>
     *
     * @param userId         id utente
     * @param relationshipId id relationship
     * @param token          token
     * @param state          nuovo stato
     */
    void findAndUpdateState(String userId, @Nullable String relationshipId, @Nullable Token token, RelationshipState state);

    void findAndUpdateStateByInstitutionAndProduct(String userId, String institutionId, String productId, RelationshipState state);

    void findAndUpdate(OnboardedUser onboardedUser, String id, String institutionId, OnboardedProduct product, UserBinding bindings);

    OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);

    OnboardedUser findAndCreate(String id, UserBinding binding);

    List<OnboardedUser> findActiveInstitutionUser(String userId, String institutionId);

    List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    OnboardedUser findByRelationshipId(String relationshipId);

    void findAndRemoveProduct(String userId, String institutionId, OnboardedProduct product);

    List<OnboardedUser> findAllByIds(List<String> users);

    List<OnboardedUser> findAllByExistingIds(List<String> users);

    List<OnboardedUser> updateUserBindingCreatedAt(String institutionId, String productId, List<String> users, OffsetDateTime createdAt);

    List<UserInstitutionAggregation> findUserInstitutionAggregation(UserInstitutionFilter filter);

    List<UserInfo> findByInstitutionId(String institutionId);

    List<UserInstitutionAggregation>  getUserInfo(String userId, String institutionId, String[] states);

}
