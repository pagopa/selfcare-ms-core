package it.pagopa.selfcare.mscore.model;

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

    public Token(Token token) {
        this.id = token.getId();
        this.institutionId = token.getInstitutionId();
        this.productId = token.getProductId();
        this.expiringDate = token.getExpiringDate();
        this.checksum = token.getChecksum();
        this.contract = token.getContract();
        this.users = token.getUsers();
        this.createdAt = token.getCreatedAt();
    }
}
