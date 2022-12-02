package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Institution")
@Data
public class InstitutionEntity {
    private String id;
    private String externalId;
}


