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
@RequestMapping("/notification-event")
@Api(tags = "kafka")
public class QueueNotificationController {

    private final QueueNotificationService queueNotificationService;

    public QueueNotificationController(QueueNotificationService queueNotificationService) {
        this.queueNotificationService = queueNotificationService;
    }
    @ApiOperation(value = "", notes = "Function to send a specific onboarding using institutionId and tokenId ")
    @PutMapping(value = "/contracts")
    @ResponseStatus(HttpStatus.OK)
    public void resendContractsByInstitutionIdAndTokenId(@RequestParam(name = "tokenId") String tokenId,
                                @RequestParam(name = "institutionId") String institutionId){

        log.trace("Resend contracts byInstitutionIdAndTokenId events started");
        queueNotificationService.sendContractsNotificationsByInstitutionIdAndTokenId(tokenId, institutionId);
    }

}