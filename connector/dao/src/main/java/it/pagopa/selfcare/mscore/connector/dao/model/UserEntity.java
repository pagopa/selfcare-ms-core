package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document("User")
public class UserEntity {

    @MongoId
    private ObjectId id;

    private String institutionId;

    private ProductEntity[] products;

    public UserEntity(OnboardedUser user) {
        if (user.getUser() != null) {
            id = new ObjectId(user.getUser());
        }
        institutionId = user.getInstitutionId();
    }
}
