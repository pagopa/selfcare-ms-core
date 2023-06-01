package it.pagopa.selfcare.mscore.connector.dao.utils;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DaoMockUtilsTest {

    @Test
    void createConfigEntityMock() {
        // When
        ConfigEntity result = DaoMockUtils.createConfigEntityMock();
        // Then
        assertNotNull(result);
    }

    @Test
    void createTokeEntitynMock() {
        // Given
        Integer biasMock = 1;
        RelationshipState statusMock = RelationshipState.ACTIVE;
        // When
        TokenEntity result = DaoMockUtils.createTokenEntityMock(biasMock, statusMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenId" + biasMock, result.getId());
        assertEquals("InstitutionId" + biasMock, result.getInstitutionId());
        assertEquals("ProductId" + biasMock, result.getProductId());
        assertEquals(statusMock, result.getStatus());
    }

    @Test
    void createTokenEntityMock_nullBias() {
        // Given
        RelationshipState statusMock = RelationshipState.ACTIVE;
        // When
        TokenEntity result = DaoMockUtils.createTokenEntityMock(null, statusMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenId", result.getId());
        assertEquals("InstitutionId", result.getInstitutionId());
        assertEquals("ProductId", result.getProductId());
        assertEquals(statusMock, result.getStatus());
    }
}
