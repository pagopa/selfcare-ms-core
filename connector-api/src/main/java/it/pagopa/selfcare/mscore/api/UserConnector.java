package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipPage;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserConnector {

    void deleteById(String id);

    OnboardedUser findById(String userId);

    /**
     * <b>Necessario specificare almeno uno tra relationshipId e tokenId.</b>
     *
     * @param userId         id utente
     * @param relationshipId id relationship
     * @param tokenId        id token
     * @param state          nuovo stato
     */
    void findAndUpdateState(String userId, @Nullable String relationshipId, @Nullable String tokenId, RelationshipState state);

    void findAndUpdate(OnboardedUser onboardedUser, String id, String institutionId, OnboardedProduct product, UserBinding bindings);

    OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> state);

    OnboardedUser findAndCreate(String id, UserBinding binding);

    List<OnboardedUser> findActiveInstitutionAdmin(String userId, String institutionId, List<PartyRole> adminPartyRole, List<RelationshipState> active);

    List<OnboardedUser> findWithFilter(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    RelationshipPage findPagedWithFilter(String institutionId,
                                         @Nullable String personId,
                                         @Nullable List<PartyRole> roles,
                                         @Nullable List<RelationshipState> states,
                                         @Nullable List<String> products,
                                         @Nullable List<String> productRoles,
                                         Pageable pageable);

    OnboardedUser findByRelationshipId(String relationshipId);

    void findAndRemoveProduct(String userId, String institutionId, OnboardedProduct product);

    List<OnboardedUser> findAllByIds(List<String> users);
}
