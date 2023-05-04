package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private OffsetDateTime closedAt;
    private List<UserToNotify> users;
    private PaymentServiceProvider paymentServiceProvider;
    private String zipCode;
    private String istatCode;

}
