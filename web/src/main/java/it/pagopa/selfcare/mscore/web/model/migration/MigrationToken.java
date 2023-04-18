package it.pagopa.selfcare.mscore.web.model.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MigrationToken {
    private String id;

    @NotNull(message = "Valid value for token type are: INSTITUTION and LEGALS")
    private TokenType type;

    @NotNull(message = "Valid value for status are: PENDING, ACTIVE, SUSPENDED, DELETED, TOBEVALIDATED or REJECTED")
    private RelationshipState status;

    private String institutionId;
    private String productId;
    private OffsetDateTime expiringDate;
    private String checksum;
    private String contractTemplate;
    private String contractVersion;
    private String contractSigned;
    private List<TokenUser> users;
    private InstitutionUpdate institutionUpdate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
}
