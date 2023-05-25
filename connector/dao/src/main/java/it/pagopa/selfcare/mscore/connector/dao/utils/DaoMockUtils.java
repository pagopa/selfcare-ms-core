package it.pagopa.selfcare.mscore.connector.dao.utils;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.NONE)
public class DaoMockUtils {

    public static ConfigEntity createConfigEntityMock() {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId("KafkaScheduler");
        configEntity.setProductFilter("");
        configEntity.setEnableKafkaScheduler(true);
        return configEntity;
    }

    public static TokenEntity createTokenEntityMock(Integer bias, RelationshipState status) {
        TokenEntity tokenMock = new TokenEntity();
        tokenMock.setId("TokenId" + (bias == null ? "" : bias));
        tokenMock.setType(TokenType.INSTITUTION);
        tokenMock.setStatus(status);
        tokenMock.setInstitutionId("InstitutionId" + (bias == null ? "" : bias));
        tokenMock.setProductId("ProductId" + (bias == null ? "" : bias));
        tokenMock.setExpiringDate(OffsetDateTime.now().plusDays(60));
        tokenMock.setChecksum("Checksum");
        tokenMock.setContractVersion("ContractVersion");
        tokenMock.setContractTemplate("ContractTemplate");
        tokenMock.setContractSigned("ContractPath/" + tokenMock.getId() + "/FileName");
        tokenMock.setCreatedAt(OffsetDateTime.now().minusDays(2));
        tokenMock.setUpdatedAt(OffsetDateTime.now().minusDays(1));
        tokenMock.setClosedAt((status.equals(RelationshipState.DELETED) ? OffsetDateTime.now() : null));
        return tokenMock;
    }
}
