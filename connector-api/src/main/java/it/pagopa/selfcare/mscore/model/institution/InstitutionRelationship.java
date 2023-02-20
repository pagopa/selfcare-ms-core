package it.pagopa.selfcare.mscore.model.institution;

import it.pagopa.selfcare.mscore.model.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class InstitutionRelationship {
    private String id;
    private String from;
    private String to;
    private String role;
    private OnboardedProduct product;
    private RelationshipState state;
    private String pricingPlan;
    private InstitutionUpdate institutionUpdate;
    private Billing billing;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
