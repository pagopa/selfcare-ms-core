package it.pagopa.selfcare.mscore.model.infocamere;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InfoCamereBatchRequest {

    private String id;
    private String correlationId;
    private String batchId;
    private String cf;
    private Integer retry;
    private LocalDateTime ttl;
    private String status;
    private LocalDateTime lastReserved;
    private LocalDateTime timeStamp;

}
