package it.pagopa.selfcare.mscore.connector.dao.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("names")
public class NameEntity {

    @Id
    private String id;

}
