package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@Document("User")
public class UserEntity {

    @MongoId
    private ObjectId id;

    private List<UserInstitutionEntity> institutions;

    public UserEntity(OnboardedUser user) {
        if (user.getUser() != null) {
            id = new ObjectId(user.getUser());
        }
    }
}
