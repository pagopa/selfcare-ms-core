package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;

@Data
@NoArgsConstructor
@Document("User")
public class UserEntity {

    @MongoId
    private ObjectId id;
    private Map<String,Map<String, Product>> bindings;
}
