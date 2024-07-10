package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Document("PecNotification")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class PecNotificationEntity {

    @Id
    private Object id;
    private String institutionId;
    private String productId;
    private Integer moduleDayOfTheEpoch;
    private String digitalAddress;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}