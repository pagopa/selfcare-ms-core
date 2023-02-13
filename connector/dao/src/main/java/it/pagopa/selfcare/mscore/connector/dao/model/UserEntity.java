package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.OnboardedProduct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@Document("User")
public class UserEntity {

    @MongoId
    private ObjectId id;

    private String user;
    private Map<String,Map<String, OnboardedProduct>> bindings;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
