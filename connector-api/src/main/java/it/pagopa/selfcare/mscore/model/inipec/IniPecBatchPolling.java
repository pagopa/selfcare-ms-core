package it.pagopa.selfcare.mscore.model.inipec;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IniPecBatchPolling {

    private String id;
    private String batchId;
    private String pollingId;
    private String status;
    private LocalDateTime timeStamp;

}
