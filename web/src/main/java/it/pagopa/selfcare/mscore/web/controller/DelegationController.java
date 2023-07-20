package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
}
