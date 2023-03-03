package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.TokenUserEntity;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.utils.TokenTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document("Token")
@FieldNameConstants(asEnum = true)
public class TokenEntity {

    @Id
    private String id;
    private TokenTypeEnum type;
    private RelationshipState status;
    private String institutionId;
    private String productId;
    private OffsetDateTime expiringDate;
    private String checksum;
    private String contractTemplate;
    private String contractSigned;
    private List<TokenUserEntity> users;
    private InstitutionUpdateEntity institutionUpdate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
}

