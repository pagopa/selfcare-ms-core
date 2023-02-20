package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.*;
import java.util.List;

public interface UserService {
    OnboardedUser findOnboardedManager(String id, String productId, List<RelationshipState> active);
    OnboardedUser findByUserId(String id);
}
