package it.pagopa.selfcare.mscore.core;

import org.springframework.scheduling.annotation.Async;

public interface QueueNotificationService {

    @Async
    void sendContractsNotificationsByInstitutionIdAndTokenId(String tokenId, String institutionId);

}
