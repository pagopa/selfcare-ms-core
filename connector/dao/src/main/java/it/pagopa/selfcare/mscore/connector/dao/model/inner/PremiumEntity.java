package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
public class PremiumEntity {
    private RelationshipState status;
    private String contract;
}
