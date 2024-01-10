package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.QueueNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/scheduler")
@Api(tags = "scheduler")
public class QueueNotificationController {

    private final QueueNotificationService queueNotificationService;

    public QueueNotificationController(QueueNotificationService queueNotificationService) {
        this.queueNotificationService = queueNotificationService;
    }
    @ApiOperation(value = "", notes = "${swagger.ms-core.scheduler.api.start}")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public void resendContracts(@RequestParam(name = "size", required = false) Optional<Integer> size,
                                @RequestParam(name = "productsFilter") List<String> productsFilter){

        log.trace("Resend contracts events started");
        queueNotificationService.startScheduler(size, productsFilter);
    }

    @ApiOperation(value = "", notes = "${swagger.ms-core.scheduler.api.start.users}")
    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public void resendUsers(@RequestParam(name = "size", required = false)Optional<Integer> size,
                            @RequestParam(name = "page", required = false)Optional<Integer> page,
                            @RequestParam(name = "productsFilter")List<String> productsFilter,
                            @RequestParam(name = "userId", required = false)Optional<String> userId){
        log.trace("Resend users events started");
        queueNotificationService.startUsersScheduler(size, page, productsFilter, userId);
    }
}