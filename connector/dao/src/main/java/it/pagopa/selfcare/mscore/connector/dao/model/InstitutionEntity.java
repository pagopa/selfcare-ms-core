package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.Institution;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@Document("Institution")
public class InstitutionEntity {

    @MongoId
    private ObjectId id;

    private String externalId;

    public InstitutionEntity(Institution institution) {
        if (institution.getId() != null) {
            id = new ObjectId(institution.getId());
        }
        externalId = institution.getExternalId();
    }
}
