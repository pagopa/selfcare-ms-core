package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/scheduler")
@Api(tags = "scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }
    @ApiOperation(value = "", notes = "${swagger.external-interceptor.scheduler.api.start}")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void start(@RequestParam(name = "size", required = false) Optional<Integer> size,
                      @RequestParam(name = "productsFilter") List<String> productsFilter){

        log.trace("Scheduler Started");
        schedulerService.startScheduler(size, productsFilter);
    }
}