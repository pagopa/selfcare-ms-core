package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document("PecNotification")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class PecNotificationEntity {

    @Id
    private String id;
    private String institutionId;
    private String productId;
    private Integer moduleDayOfTheEpoch;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}