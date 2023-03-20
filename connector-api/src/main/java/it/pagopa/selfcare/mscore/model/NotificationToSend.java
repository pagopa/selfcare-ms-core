package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class NotificationToSend {
    private String id;
    private String internalInstitutionID;
    private String product;
    private RelationshipState state;
    private String filePath;
    private String fileName;
    private String contentType;
    private String onboardingTokenId;
    private String pricingPlan;
    private OffsetDateTime updatedAt;
    private Institution institution;
    private Billing billing;

}
