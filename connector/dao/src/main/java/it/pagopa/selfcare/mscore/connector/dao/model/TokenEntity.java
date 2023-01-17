package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@Document("Token")
public class TokenEntity {
    @MongoId
    private ObjectId id;
    private RelationshipState status;
    private String institutionId;
    private String productId;
    private String expiringDate;
    private String checksum;
    private String contract;
    private List<String> users;
}

