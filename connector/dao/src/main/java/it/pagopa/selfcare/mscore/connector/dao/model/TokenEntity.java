package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document("Token")
@FieldNameConstants(asEnum = true)
public class TokenEntity {

    @Id
    private String id;
    private RelationshipState status;
    private String institutionId;
    private String productId;
    private String expiringDate;
    private String checksum;
    private String contract;
    private List<String> users;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

