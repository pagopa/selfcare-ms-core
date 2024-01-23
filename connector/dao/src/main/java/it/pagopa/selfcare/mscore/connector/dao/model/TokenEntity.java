package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.TokenUserEntity;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document("Token")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class TokenEntity {

    @Id
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
    private List<TokenUserEntity> users;
    private InstitutionUpdateEntity institutionUpdate;
    @Indexed
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private OffsetDateTime activatedAt;

}

