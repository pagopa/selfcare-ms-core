package it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NationalRegistriesAddressRequest {

    @JsonProperty("filter")
    private NationalRegistriesAddressFilter filter;
}
