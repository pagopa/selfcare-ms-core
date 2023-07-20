package it.pagopa.selfcare.mscore.model.onboarding;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
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
    private String contentType;
    private List<TokenUser> users;
    private InstitutionUpdate institutionUpdate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return Objects.equals(getId(), token.getId()) && getType() == token.getType() && getStatus() == token.getStatus() && Objects.equals(getInstitutionId(), token.getInstitutionId()) && Objects.equals(getProductId(), token.getProductId()) && Objects.equals(getChecksum(), token.getChecksum()) && Objects.equals(getContractVersion(), token.getContractVersion()) && Objects.equals(getContractTemplate(), token.getContractTemplate()) && Objects.equals(getContractSigned(), token.getContractSigned()) && Objects.equals(getContentType(), token.getContentType()) && Objects.equals(getInstitutionUpdate(), token.getInstitutionUpdate());
    }

}
