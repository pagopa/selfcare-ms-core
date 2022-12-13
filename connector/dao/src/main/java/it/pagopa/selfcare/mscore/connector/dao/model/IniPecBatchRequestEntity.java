package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document("IniPecBatchRequest")
public class IniPecBatchRequestEntity {

    @MongoId
    private ObjectId id;
    private String batchId;
    private String cf;
    private Integer retry;
    private LocalDateTime ttl;
    private String status;
    private LocalDateTime lastReserved;
    private LocalDateTime timeStamp;

    public IniPecBatchRequestEntity(IniPecBatchRequest iniPecBatchRequest) {
        if (iniPecBatchRequest.getId() != null) {
            id = new ObjectId(iniPecBatchRequest.getId());
        }
        batchId = iniPecBatchRequest.getBatchId();
        cf = iniPecBatchRequest.getCf();
        retry = iniPecBatchRequest.getRetry();
        ttl = iniPecBatchRequest.getTtl();
        status = iniPecBatchRequest.getStatus();
        lastReserved = iniPecBatchRequest.getLastReserved();
        timeStamp = iniPecBatchRequest.getTimeStamp();
    }
}
