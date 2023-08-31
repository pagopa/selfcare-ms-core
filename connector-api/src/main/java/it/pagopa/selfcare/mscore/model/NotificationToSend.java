package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationToSend {

    private String id;
    private String internalIstitutionID;
    private String product;
    private String state;
    private String filePath;
    private String fileName;
    private String contentType;
    private String onboardingTokenId;
    private String pricingPlan;
    private InstitutionToNotify institution;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime closedAt;
    private OffsetDateTime updatedAt;
    private QueueEvent notificationType;

}
