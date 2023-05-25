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
        ConfigEntity configEntityMock = new ConfigEntity();
        configEntityMock.setId("KafkaScheduler");
        configEntityMock.setProductFilter("");
        configEntityMock.setEnableKafkaScheduler(true);
        return configEntityMock;
    }

    public static TokenEntity createTokenEntityMock(Integer bias, RelationshipState status) {
        TokenEntity tokenEntityMock = new TokenEntity();
        tokenEntityMock.setId("TokenId" + (bias == null ? "" : bias));
        tokenEntityMock.setType(TokenType.INSTITUTION);
        tokenEntityMock.setStatus(status);
        tokenEntityMock.setInstitutionId("InstitutionId" + (bias == null ? "" : bias));
        tokenEntityMock.setProductId("ProductId" + (bias == null ? "" : bias));
        tokenEntityMock.setExpiringDate(OffsetDateTime.now().plusDays(60));
        tokenEntityMock.setChecksum("Checksum");
        tokenEntityMock.setContractVersion("ContractVersion");
        tokenEntityMock.setContractTemplate("ContractTemplate");
        tokenEntityMock.setContractSigned("ContractPath/" + tokenEntityMock.getId() + "/FileName");
        tokenEntityMock.setCreatedAt(OffsetDateTime.now().minusDays(2));
        tokenEntityMock.setUpdatedAt(OffsetDateTime.now().minusDays(1));
        tokenEntityMock.setClosedAt((status.equals(RelationshipState.DELETED) ? OffsetDateTime.now() : null));
        return tokenEntityMock;
    }
}
