package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("User")
public class UserInstitutionEntity {

    private ProductEntity[] products;
    private String institutionId;

}
