package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("Config")
@FieldNameConstants(asEnum = true)
public class ConfigEntity {

    @Id
    private String id;

    private String productFilter;

    private boolean enableKafkaScheduler;

}
