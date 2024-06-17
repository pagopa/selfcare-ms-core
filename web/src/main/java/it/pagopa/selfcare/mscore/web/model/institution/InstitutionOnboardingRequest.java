package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class InstitutionOnboardingRequest {

    @NotEmpty(message = "productId is required")
    private String productId;

    private String tokenId;
    private String contractPath;
    private String pricingPlan;
    private BillingRequest billing;
    private LocalDateTime activatedAt;
    private Boolean isAggregator;

}
