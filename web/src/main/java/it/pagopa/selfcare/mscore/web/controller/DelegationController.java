    package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.core.DelegationService;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequest;
import it.pagopa.selfcare.mscore.web.model.delegation.DelegationRequestFromTaxcode;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/delegations", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Delegation")
@Slf4j
public class DelegationController {

    public static final int MAX_PAGE_SIZE = 100;
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
     * * Code: 409, Message: Conflict, DataType: Problem
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
     * The function persist delegation
     *
     * @param delegation DelegationRequest
     * @return InstitutionResponse
     * * Code: 201, Message: successful operation, DataType: DelegationResponse
     * * Code: 400, Message: Bad Request, DataType: Problem
     * * Code: 409, Message: Conflict, DataType: Problem
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.delegation.createFromTaxCode}", notes = "${swagger.mscore.delegation.createFromTaxCode}")
    @PostMapping("/from-taxcode")
    public ResponseEntity<DelegationResponse> createDelegationFromInstitutionsTaxCode(@RequestBody @Valid DelegationRequestFromTaxcode delegation) {
        CustomExceptionMessage.setCustomMessage(GenericError.CREATE_DELEGATION_ERROR);
        Delegation saved = delegationService.createDelegationFromInstitutionsTaxCode(delegationMapper.toDelegation(delegation));
        return ResponseEntity.status(HttpStatus.CREATED).body(delegationMapper.toDelegationResponse(saved));
    }



    /**
     * The function get delegations
     *
     * @param institutionId String
     * @return InstitutionResponse
     * * Code: 200, Message: successful operation, DataType: List<DelegationResponse>
     * * Code: 404, Message: Institution data not found, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     */
    @Tags({@Tag(name = "external-v2"), @Tag(name = "support"), @Tag(name = "Delegation")})
    @ApiOperation(value = "${swagger.mscore.institutions.delegations}", notes = "${swagger.mscore.institutions.delegations}")
    @GetMapping()
    public ResponseEntity<List<DelegationResponse>> getDelegations(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                   @RequestParam(name = "institutionId", required = false) String institutionId,
                                                                   @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                   @RequestParam(name = "brokerId", required = false) String brokerId,
                                                                   @ApiParam("${swagger.mscore.product.model.id}")
                                                                   @RequestParam(name = "productId", required = false) String productId,
                                                                   @ApiParam("${swagger.mscore.institutions.delegations.mode}")
                                                                   @RequestParam(name = "mode", required = false) GetDelegationsMode mode) {

        if(Objects.isNull(institutionId) && Objects.isNull(brokerId))
            throw new InvalidRequestException("institutionId or brokerId must not be null!!", GenericError.GENERIC_ERROR.getCode());

        return ResponseEntity.status(HttpStatus.OK).body(delegationService.getDelegations(institutionId, brokerId, productId, mode).stream()
                .map(delegationMapper::toDelegationResponse)
                .collect(Collectors.toList()));
    }

    /**
     * The function get delegations
     *
     * @param from String
     * @param to String
     * @return DelegationResponse
     * * Code: 200, Message: successful operation, DataType: List<DelegationResponse>
     * * Code: 404, Message: Institution data not found, DataType: Problem
     * * Code: 400, Message: Bad Request, DataType: Problem
     */
    @ApiOperation(value = "${swagger.mscore.institutions.delegations}", notes = "${swagger.mscore.institutions.delegations}")
    @GetMapping("/filter")
    public ResponseEntity<List<DelegationResponse>> getPaginatedDelegations(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                    @RequestParam(name = "from", required = false) String from,
                                                                    @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                    @RequestParam(name = "to", required = false) String to,
                                                                    @RequestParam(name = "page", required = false) Optional<Integer> page,
                                                                    @RequestParam(name = "size", required = false) Optional<Integer> size) {
        if(Objects.isNull(from) && Objects.isNull(to))
            throw new InvalidRequestException("from or to must not be null!!", GenericError.GENERIC_ERROR.getCode());

        int pageSize = size.filter(s -> s <= MAX_PAGE_SIZE).orElse(MAX_PAGE_SIZE);

        return ResponseEntity.status(HttpStatus.OK).body(delegationService.getPaginatedDelegations(from, to, page, Optional.of(pageSize)).stream()
                .map(delegationMapper::toDelegationResponse)
                .collect(Collectors.toList()));
    }
}
