package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document("InfoCamereBatchRequest")
public class InfoCamereBatchRequestEntity {

    @MongoId
    private ObjectId id;
    private String batchId;
    private String cf;
    private Integer retry;
    private LocalDateTime ttl;
    private String status;
    private LocalDateTime lastReserved;
    private LocalDateTime timeStamp;

    public InfoCamereBatchRequestEntity(InfoCamereBatchRequest infoCamereBatchRequest) {
        if (infoCamereBatchRequest.getId() != null) {
            id = new ObjectId(infoCamereBatchRequest.getId());
        }
        batchId = infoCamereBatchRequest.getBatchId();
        cf = infoCamereBatchRequest.getCf();
        retry = infoCamereBatchRequest.getRetry();
        ttl = infoCamereBatchRequest.getTtl();
        status = infoCamereBatchRequest.getStatus();
        lastReserved = infoCamereBatchRequest.getLastReserved();
        timeStamp = infoCamereBatchRequest.getTimeStamp();
    }
}
