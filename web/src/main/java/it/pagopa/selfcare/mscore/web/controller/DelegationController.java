package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationResponse;
import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/delegations", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Delegation")
@Slf4j
public class DelegationController {

    private final DelegationService delegationService;
    private final DelegationMapper delegationMapper;

    public DelegationController(DelegationService delegationService,
                                DelegationMapper delegationMapper) {
        this.delegationService = delegationService;
        this.delegationMapper = delegationMapper;
    }

    /**
     * The function persist delegation
     *
     * @param delegation DelegationRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: DelegationResponse
     * * Code: 400, Message: Bad Request, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.delegation.create}", notes = "${swagger.mscore.delegation.create}")
    @PostMapping
    public ResponseEntity<DelegationResponse> createDelegation(@RequestBody @Valid DelegationRequest delegation) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_DELEGATION_ERROR);
        Delegation saved = delegationService.createDelegation(delegationMapper.toDelegation(delegation));
        return ResponseEntity.status(HttpStatus.CREATED).body(delegationMapper.toDelegationResponse(saved));
    }



    /**
     * The function get delegations
     *
     * @param from String
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: List<DelegationResponse>
     * * Code: 404, Message: Institution data not found, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     */
    @ApiOperation(value = "${swagger.mscore.institutions.delegations}", notes = "${swagger.mscore.institutions.delegations}")
    @GetMapping()
    public ResponseEntity<List<DelegationResponse>> getDelegations(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                   @RequestParam(name = "from", required = false) String from,
                                                                   @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                    @RequestParam(name = "to", required = false) String to,
                                                                   @ApiParam("${swagger.mscore.product.model.id}")
                                                                   @RequestParam(name = "productId", required = false) String productId) {

        return ResponseEntity.status(HttpStatus.OK).body(delegationService.getDelegations(from, to, productId).stream()
                .map(delegationMapper::toDelegationResponse)
                .collect(Collectors.toList()));
    }
}
