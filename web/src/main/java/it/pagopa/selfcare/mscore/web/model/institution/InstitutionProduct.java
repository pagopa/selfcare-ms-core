package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;

@Data
public class InstitutionProduct {
    private String id;
    private RelationshipState state;
}
