package it.pagopa.selfcare.mscore.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetDigitalAddressInfoCamereRequestBodyFilterDto {

    @JsonProperty("taxId")
    private String taxId;

    /*
    @JsonProperty("correlationId")
    private String correlationId;
    */
}
