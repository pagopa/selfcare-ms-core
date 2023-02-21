package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.RelationshipState;

import java.util.List;

public class UtilEnumList {


    public static final List<RelationshipState> PRODUCT_RELATIONSHIP_STATES =
            List.of(RelationshipState.PENDING,
                    RelationshipState.REJECTED,
                    RelationshipState.TOBEVALIDATED);
    public static final List<RelationshipState> VALID_RELATIONSHIP_STATES =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.DELETED,
                    RelationshipState.SUSPENDED);
    public static final List<RelationshipState> ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.PENDING);
    private UtilEnumList() {}

}
