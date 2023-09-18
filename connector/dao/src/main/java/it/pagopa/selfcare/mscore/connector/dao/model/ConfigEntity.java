package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Data
@Document("Config")
@FieldNameConstants(asEnum = true)
public class ConfigEntity {

    @Id
    private String id;

    private String productFilter;

    private int firstPage;

    private int lastPage;

    private boolean enableKafkaScheduler;

    private OffsetDateTime lastRequestDate;

}
