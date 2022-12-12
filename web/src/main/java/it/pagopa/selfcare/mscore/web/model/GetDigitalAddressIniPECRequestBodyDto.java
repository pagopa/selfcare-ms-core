package it.pagopa.selfcare.mscore.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetDigitalAddressIniPECRequestBodyDto {

    @JsonProperty("filter")
    private GetDigitalAddressIniPECRequestBodyFilterDto filter;

}
