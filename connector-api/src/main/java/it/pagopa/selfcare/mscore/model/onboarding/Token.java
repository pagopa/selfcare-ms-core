package it.pagopa.selfcare.mscore.model.onboarding;

import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Token {
    private String id;
    private RelationshipState status;
    private String institutionId;
    private String productId;
    private String expiringDate;
    private String checksum;
    private String contract;
    private List<String> users;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
