package it.pagopa.selfcare.mscore.model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Onboarding {

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("status")
    private RelationshipState status;

    @JsonProperty("contract")
    private String contract;

    @JsonProperty("pricingPlan")
    private String pricingPlan;

    @JsonProperty("premium")
    private Premium premium;

    private Billing billing; //optional
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
