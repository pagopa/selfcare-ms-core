package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipPage;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.User;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.List;

public interface UserService {

    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);

    OnboardedUser findByUserId(String id);

    List<OnboardedUser> findAllByIds(List<String> users);

    List<OnboardedUser> retrieveUsers(String externalId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles);

    RelationshipPage retrievePagedUsers(String externalId,
                                        String personId,
                                        List<PartyRole> roles,
                                        List<RelationshipState> states,
                                        List<String> products,
                                        List<String> productRoles,
                                        Pageable pageable);

    boolean checkIfAdmin(String userId, String institutionId);

    void verifyUser(String userId);

    User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields);

    OnboardedUser createUser(String userId);
}
