package it.pagopa.selfcare.mscore.core;

import java.util.List;
import java.util.Optional;

public interface SchedulerService {

    void startScheduler(Optional<Integer> size, List<String> productsFilter);

    void startUsersScheduler(Optional<Integer> size,Optional<Integer> page, List<String> productsFilter, Optional<String> userId);
}
