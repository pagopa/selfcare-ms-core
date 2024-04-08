package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.QueueNotificationService;
import it.pagopa.selfcare.mscore.model.aggregation.QueryCount;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.user.ProductCountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/notification-event")
@Api(tags = "kafka")
public class QueueNotificationController {

    private final QueueNotificationService queueNotificationService;

    private final UserMapper userMapper;

    public QueueNotificationController(QueueNotificationService queueNotificationService, UserMapper userMapper) {
        this.queueNotificationService = queueNotificationService;
        this.userMapper = userMapper;
    }
    @ApiOperation(value = "", notes = "${swagger.ms-core.notification-event.api.start}")
    @PostMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void resendContracts(@RequestParam(name = "size", required = false) Optional<Integer> size,
                                @RequestParam(name = "productsFilter") List<String> productsFilter){

        log.trace("Resend contracts events started");
        queueNotificationService.sendContracts(size, productsFilter);
    }
    @ApiOperation(value = "", notes = "Function to send a specific onboarding using institutionId and tokenId ")
    @PutMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void resendContractsByInstitutionIdAndTokenId(@RequestParam(name = "tokenId") String tokenId,
                                @RequestParam(name = "institutionId") String institutionId){

        log.trace("Resend contracts byInstitutionIdAndTokenId events started");
        queueNotificationService.sendContractsNotificationsByInstitutionIdAndTokenId(tokenId, institutionId);
    }

    @ApiOperation(value = "", notes = "${swagger.ms-core.notification-event.api.start.users}")
    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void resendUsers(@RequestParam(name = "size", required = false)Optional<Integer> size,
                            @RequestParam(name = "page", required = false)Optional<Integer> page,
                            @RequestParam(name = "productsFilter")List<String> productsFilter,
                            @RequestParam(name = "userId", required = false)Optional<String> userId){
        log.trace("Resend users events started");
        queueNotificationService.sendUsers(size, page, productsFilter, userId);
    }

    @ApiOperation(value = "", notes = "${swagger.ms-core.notification-event.api.start.users.count}")
    @GetMapping(value = "/users/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductCountResponse> countUsers(){
        log.trace("Count users start");
        List<QueryCount> counts = queueNotificationService.countUsers();

        ProductCountResponse productCountResponse = new ProductCountResponse(counts.stream()
                .map(userMapper::toProductCount).toList());

        log.trace("Count users end");
        return ResponseEntity.ok().body(productCountResponse);
    }
}