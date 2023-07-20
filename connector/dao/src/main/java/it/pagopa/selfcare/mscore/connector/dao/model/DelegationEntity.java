package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

@Data
@Document("Delegations")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class DelegationEntity {

    @Id
    private String id;
    private String from;
    private String institutionFromName;
    private String to;
    private String productId;
    private DelegationType type;

}
