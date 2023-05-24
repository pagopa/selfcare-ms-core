package it.pagopa.selfcare.mscore.web.model.token;

import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TokenResource {

    private String id;
    private TokenType type;
    private RelationshipState status;
    private String institutionId;
    private String productId;
    private OffsetDateTime expiringDate;
    private String checksum;
    private String contractVersion;
    private String contractTemplate;
    private String contractSigned;
    private List<TokenUser> users;
    private InstitutionUpdate institutionUpdate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
}
