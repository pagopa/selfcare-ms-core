package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipInfo {

    private String id;
    private String from;
    private String to;
    private PartyRole role;
    private ProductInfo productInfo;
    private String state;
    private String pricingPlan;
    private InstitutionUpdate institutionUpdate;
    private BillingResponse billingResponse;
    private OffsetDateTime createdAt;
    private OffsetDateTime updateAt;
}
