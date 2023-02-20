package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.model.RelationshipState;

import java.util.List;

public class UtilEnumList {

    private UtilEnumList() {}

    public static final List<RelationshipState> productRelationshipStates =
            List.of(RelationshipState.PENDING,
                    RelationshipState.REJECTED,
                    RelationshipState.TOBEVALIDATED);
    public static final List<RelationshipState> validRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.DELETED,
                    RelationshipState.SUSPENDED);
    public static final List<RelationshipState> onboardingInfoDefaultRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.PENDING);
}
