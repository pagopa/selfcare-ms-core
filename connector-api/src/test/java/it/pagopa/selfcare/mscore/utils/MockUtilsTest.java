package it.pagopa.selfcare.mscore.utils;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.Config;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MockUtilsTest {

    @Test
    void createConfigMock() {
        // Given
        boolean enabledMock = true;
        String productFilterMock = "productFilterMock";
        // When
        Config result = MockUtils.createConfigMock(enabledMock, productFilterMock);
        // Then
        assertNotNull(result);
        assertEquals("KafkaScheduler", result.getId());
        assertEquals(enabledMock, result.isEnableKafkaScheduler());
        assertEquals(productFilterMock, result.getProductFilter());
    }

    @Test
    void createTokenMock() {
        // Given
        Integer biasMock = 1;
        RelationshipState statusMock = RelationshipState.ACTIVE;
        InstitutionType institutionTypeMock = InstitutionType.PA;
        // When
        Token result = MockUtils.createTokenMock(biasMock, statusMock, institutionTypeMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenId" + biasMock, result.getId());
        assertEquals("InstitutionId" + biasMock, result.getInstitutionId());
        assertEquals("ProductId" + biasMock, result.getProductId());
        assertEquals(statusMock, result.getStatus());
        assertEquals(institutionTypeMock, result.getInstitutionUpdate().getInstitutionType());
    }

    @Test
    void createTokenMock_nullBias() {
        // Given
        RelationshipState statusMock = RelationshipState.ACTIVE;
        InstitutionType institutionTypeMock = InstitutionType.PA;
        // When
        Token result = MockUtils.createTokenMock(null, statusMock, institutionTypeMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenId", result.getId());
        assertEquals("InstitutionId", result.getInstitutionId());
        assertEquals("ProductId", result.getProductId());
        assertEquals(statusMock, result.getStatus());
        assertEquals(institutionTypeMock, result.getInstitutionUpdate().getInstitutionType());
    }

    @Test
    void createTokenListMock() {
        // Given
        Integer numberOfTokensMock = 30;
        // When
        List<Token> result = MockUtils.createTokenListMock(numberOfTokensMock, 0, RelationshipState.PENDING, InstitutionType.PSP);
        // Then
        assertNotNull(result);
        assertEquals(numberOfTokensMock, result.size());
    }

    @Test
    void createTokenUserMock() {
        // Given
        Integer biasMock = 1;
        PartyRole partyRoleMock = PartyRole.MANAGER;
        // When
        TokenUser result = MockUtils.createTokenUserMock(biasMock, partyRoleMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenUserId" + biasMock, result.getUserId());
        assertEquals(partyRoleMock, result.getRole());
    }

    @Test
    void createTokenUserMock_nullBias() {
        // Given
        PartyRole partyRoleMock = PartyRole.DELEGATE;
        // When
        TokenUser result = MockUtils.createTokenUserMock(null, partyRoleMock);
        // Then
        assertNotNull(result);
        assertEquals("TokenUserId", result.getUserId());
        assertEquals(partyRoleMock, result.getRole());
    }

    @Test
    void createInstitutionGeographicTaxonomiesMock_nullBias() {
        // When
        InstitutionGeographicTaxonomies result = MockUtils.createInstitutionGeographicTaxonomiesMock(null);
        // Then
        assertNotNull(result);
        assertEquals("InstitutionGeographicTaxonomiesCode", result.getCode());
        assertEquals("InstitutionGeographicTaxonomiesDesc", result.getDesc());
    }

    @Test
    void createInstitutionMock() {
        // Given
        Integer biasMock = 1;
        RelationshipState statusMock = RelationshipState.SUSPENDED;
        InstitutionType institutionTypeMock = InstitutionType.GSP;
        // When
        Institution result = MockUtils.createInstitutionMock(biasMock, statusMock, institutionTypeMock);
        // Then
        assertNotNull(result);
        assertEquals("InstitutionId" + biasMock, result.getId());
        assertEquals("InstitutionExternalId" + biasMock, result.getExternalId());
        assertEquals("Description" + biasMock, result.getDescription());
        assertEquals("DigitalAddress" + biasMock, result.getDigitalAddress());
        assertEquals("SupportEmail" + biasMock, result.getSupportEmail());
        assertEquals(statusMock, result.getOnboarding().get(0).getStatus());
        assertEquals(institutionTypeMock, result.getInstitutionType());
    }

    @Test
    void createInstitutionMock_nullBias() {
        // Given
        RelationshipState statusMock = RelationshipState.SUSPENDED;
        InstitutionType institutionTypeMock = InstitutionType.GSP;
        // When
        Institution result = MockUtils.createInstitutionMock(null, statusMock, institutionTypeMock);
        // Then
        assertNotNull(result);
        assertEquals("InstitutionId", result.getId());
        assertEquals("InstitutionExternalId", result.getExternalId());
        assertEquals("Description", result.getDescription());
        assertEquals("DigitalAddress", result.getDigitalAddress());
        assertEquals("SupportEmail", result.getSupportEmail());
        assertEquals(statusMock, result.getOnboarding().get(0).getStatus());
        assertEquals(institutionTypeMock, result.getInstitutionType());
    }

    @Test
    void createInsituttionMock_nullBias() {
        // Given
        Integer numberOfInstitutionsMock = 30;
        // When
        List<Institution> result = MockUtils.createInstitutionListMock(numberOfInstitutionsMock, 0, RelationshipState.PENDING, InstitutionType.PSP);
        // Then
        assertNotNull(result);
        assertEquals(numberOfInstitutionsMock, result.size());
    }

}
