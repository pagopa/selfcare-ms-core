package it.pagopa.selfcare.mscore.model.onboarding;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.mscore.model.EnvEnum.ROOT;

@Data
@FieldNameConstants(asEnum = true)
public class OnboardedProduct {

    private String relationshipId;
    private String productId;
    private RelationshipState status;
    private String contract;
    private List<String> productRoles;
    private PartyRole role;
    private EnvEnum env = ROOT;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
