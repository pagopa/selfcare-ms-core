package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotificationToSend {
    private String id;
    private String internalIstitutionID;
    private String product;
    private RelationshipState state;
    private String filePath;
    private String onboardingTokenId;
    private String pricingPlan;
    private InstitutionToNotify institution;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
