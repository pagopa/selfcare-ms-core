    package it.pagopa.selfcare.mscore.web.controller;

    import io.swagger.annotations.Api;
    import io.swagger.annotations.ApiOperation;
    import io.swagger.annotations.ApiParam;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import it.pagopa.selfcare.mscore.constant.GenericError;
    import it.pagopa.selfcare.mscore.constant.Order;
    import it.pagopa.selfcare.mscore.core.DelegationService;
    import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
    import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
    import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
    import it.pagopa.selfcare.mscore.web.model.delegation.DelegationWithPaginationResponse;
    import it.pagopa.selfcare.mscore.web.model.mapper.DelegationMapper;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;

    import javax.validation.constraints.Min;
    import java.util.Objects;

    @RestController
    @RequestMapping(value = "/v2/delegations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Api(tags = "Delegation")
    @Slf4j
    public class DelegationV2Controller {

        private final DelegationService delegationService;
        private final DelegationMapper delegationMapper;

        public DelegationV2Controller(DelegationService delegationService,
                                      DelegationMapper delegationMapper) {
            this.delegationService = delegationService;
            this.delegationMapper = delegationMapper;
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
        @Tag(name = "external-v2")
        @Tag(name = "support")
        @Tag(name = "Delegation")
        @ApiOperation(value = "${swagger.mscore.institutions.delegationsV2}", notes = "${swagger.mscore.institutions.delegationsv2}")
        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<DelegationWithPaginationResponse> getDelegations(@ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                       @RequestParam(name = "institutionId", required = false) String institutionId,
                                                                       @ApiParam("${swagger.mscore.institutions.model.institutionId}")
                                                                       @RequestParam(name = "brokerId", required = false) String brokerId,
                                                                       @ApiParam("${swagger.mscore.product.model.id}")
                                                                       @RequestParam(name = "productId", required = false) String productId,
                                                                       @ApiParam("${swagger.mscore.institutions.model.description}")
                                                                       @RequestParam(name = "search", required = false) String search,
                                                                       @ApiParam("${swagger.mscore.institutions.model.taxCode}")
                                                                       @RequestParam(name = "taxCode", required = false) String taxCode,
                                                                       @ApiParam("${swagger.mscore.institutions.delegations.order}")
                                                                       @RequestParam(name = "order", required = false, defaultValue = "NONE") Order order,
                                                                       @RequestParam (name = "page", required = false, defaultValue = "0") @Min(0) Integer page,
                                                                       @RequestParam(name = "size", required = false, defaultValue = "10000") @Min(1) Integer size) {

            if(Objects.isNull(institutionId) && Objects.isNull(brokerId))
                throw new InvalidRequestException("institutionId or brokerId must not be null!!", GenericError.GENERIC_ERROR.getCode());

            GetDelegationParameters delegationParameters = GetDelegationParameters.builder()
                    .from(institutionId)
                    .to(brokerId)
                    .productId(productId)
                    .search(search)
                    .taxCode(taxCode)
                    .order(order)
                    .page(page)
                    .size(size)
                    .build();

            DelegationWithPagination delegationWithPagination = delegationService.getDelegationsV2(delegationParameters);

            DelegationWithPaginationResponse response = new DelegationWithPaginationResponse(delegationWithPagination.getDelegations().stream().map(delegationMapper::toDelegationResponse).toList(), delegationWithPagination.getPageInfo());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
