package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.Instant;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@Document("PecNotification")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class PecNotificationEntity {

    @BsonId
    private ObjectId id;
    private String institutionId;
    private String productId;
    private Integer moduleDayOfTheEpoch;
    private String digitalAddress;

    private Instant createdAt;
    private Instant updatedAt;

}