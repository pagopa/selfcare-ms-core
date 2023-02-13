package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class UserInstitution {
    @JsonProperty("institutionId")
    private String institutionId;

    @JsonProperty("products")
    private List<OnboardedProduct> onboardedProducts;

    private OffsetDateTime createdAt;
    private PartyRole role;
}
