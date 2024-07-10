package it.pagopa.selfcare.mscore.model.pecnotification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PecNotification {

    private ObjectId id;
    private String institutionId;
    private String productId;
    private Integer moduleDayOfTheEpoch;
    private String digitalAddress;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;


}
