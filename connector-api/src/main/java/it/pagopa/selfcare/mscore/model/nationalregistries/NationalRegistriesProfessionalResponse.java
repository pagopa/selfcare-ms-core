package it.pagopa.selfcare.mscore.model.nationalregistries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NationalRegistriesProfessionalResponse {

  @JsonProperty("description")
  private String description;

  @JsonProperty("municipality")
  private String municipality;

  @JsonProperty("province")
  private String province;

  @JsonProperty("address")
  private String address;

  @JsonProperty("zip")
  private String zip;

}

