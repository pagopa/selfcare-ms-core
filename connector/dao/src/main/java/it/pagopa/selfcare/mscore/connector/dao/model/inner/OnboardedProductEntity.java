package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import java.time.OffsetDateTime;

import static it.pagopa.selfcare.mscore.model.EnvEnum.ROOT;

@Data
@FieldNameConstants(asEnum = true)
public class OnboardedProductEntity {
    private String relationshipId;
    private String productId;
    private String tokenId;
    private RelationshipState status;
    private String contract;
    private String productRole;
    private PartyRole role;
    private EnvEnum env = ROOT;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
