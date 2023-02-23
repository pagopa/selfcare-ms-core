package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.commons.base.security.PartyRole;
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
