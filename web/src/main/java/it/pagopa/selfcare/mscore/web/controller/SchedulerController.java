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
    @ApiOperation(value = "", notes = "${swagger.ms-core.scheduler.api.start}")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public void start(@RequestParam(name = "size", required = false) Optional<Integer> size,
                      @RequestParam(name = "productsFilter") List<String> productsFilter){

        log.trace("Scheduler Started");
        schedulerService.startScheduler(size, productsFilter);
    }

    @ApiOperation(value = "", notes = "${swagger.ms-core.scheduler.api.start.users}")
    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public void startUsers(@RequestParam(name = "size", required = false)Optional<Integer> size,
                           @RequestParam(name = "productsFilter")List<String> productsFilter,
                           @PathVariable(name = "userId", required = false)Optional<String> userId){
        log.trace("Scheduler started for Users");
        schedulerService.startUsersScheduler(size, productsFilter, userId);
    }
}