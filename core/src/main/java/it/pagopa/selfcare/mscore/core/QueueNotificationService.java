package it.pagopa.selfcare.mscore.core;

import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;

public interface QueueNotificationService {

    @Async
    void sendContractsNotificationsByInstitutionIdAndTokenId(String tokenId, String institutionId);

    void sendContracts(Optional<Integer> size, List<String> productsFilter);

    void sendUsers(Optional<Integer> size, Optional<Integer> page, List<String> productsFilter, Optional<String> userId);
}
