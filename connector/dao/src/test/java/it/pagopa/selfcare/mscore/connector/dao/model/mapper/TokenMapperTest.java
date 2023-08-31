package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenMapperTest {
    /**
     * Method under test: {@link TokenMapper#convertToTokenEntity(Token, List)}
     */
    @Test
    void testConvertToTokenEntity() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        TokenEntity actualConvertToTokenEntityResult = TokenMapper.convertToTokenEntity(token, new ArrayList<>());
        assertEquals("Checksum", actualConvertToTokenEntityResult.getChecksum());
        assertNull(actualConvertToTokenEntityResult.getUpdatedAt());
        assertEquals(TokenType.INSTITUTION, actualConvertToTokenEntityResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenEntityResult.getStatus());
        assertEquals("42", actualConvertToTokenEntityResult.getProductId());
        assertEquals("42", actualConvertToTokenEntityResult.getInstitutionId());
        assertEquals("Contract Template", actualConvertToTokenEntityResult.getContractTemplate());
        assertNull(actualConvertToTokenEntityResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenEntityResult.getContractSigned());
        assertNull(actualConvertToTokenEntityResult.getExpiringDate());
        assertEquals("42", actualConvertToTokenEntityResult.getId());
        InstitutionUpdateEntity institutionUpdate1 = actualConvertToTokenEntityResult.getInstitutionUpdate();
        assertEquals("Rea", institutionUpdate1.getRea());
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertEquals("21654", institutionUpdate1.getZipCode());
        assertEquals("42 Main St", institutionUpdate1.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate1.getDescription());
        assertEquals("Share Capital", institutionUpdate1.getShareCapital());
        assertEquals("6625550144", institutionUpdate1.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate1.getSupportEmail());
    }

    /**
     * Method under test: {@link TokenMapper#convertToTokenEntity(Token, List)}
     */
    @Test
    void testConvertToTokenEntity2() {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setRole(PartyRole.MANAGER);
        tokenUser.setUserId("42");

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(tokenUser);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setInstitutionUpdate(null);
        token.setId(null);
        token.setUsers(tokenUserList);
        TokenEntity actualConvertToTokenEntityResult = TokenMapper.convertToTokenEntity(token, null);
        assertEquals("Checksum", actualConvertToTokenEntityResult.getChecksum());
        List<TokenUserEntity> users = actualConvertToTokenEntityResult.getUsers();
        assertEquals(1, users.size());
        assertNull(actualConvertToTokenEntityResult.getUpdatedAt());
        assertEquals(TokenType.INSTITUTION, actualConvertToTokenEntityResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenEntityResult.getStatus());
        assertEquals("42", actualConvertToTokenEntityResult.getProductId());
        assertEquals("Contract Template", actualConvertToTokenEntityResult.getContractTemplate());
        assertNull(actualConvertToTokenEntityResult.getExpiringDate());
        assertNull(actualConvertToTokenEntityResult.getCreatedAt());
        assertEquals("42", actualConvertToTokenEntityResult.getInstitutionId());
        assertEquals("Contract Signed", actualConvertToTokenEntityResult.getContractSigned());
        TokenUserEntity getResult = users.get(0);
        assertEquals(PartyRole.MANAGER, getResult.getRole());
        assertEquals("42", getResult.getUserId());
    }

    /**
     * Method under test: {@link TokenMapper#convertToTokenEntity(Token, List)}
     */
    @Test
    void testConvertToTokenEntity3() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        TokenUser tokenUser = new TokenUser();
        tokenUser.setRole(PartyRole.MANAGER);
        tokenUser.setUserId("42");

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(tokenUser);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setInstitutionUpdate(institutionUpdate);
        token.setId(null);
        token.setUsers(tokenUserList);
        TokenEntity actualConvertToTokenEntityResult = TokenMapper.convertToTokenEntity(token, null);
        assertEquals("Checksum", actualConvertToTokenEntityResult.getChecksum());
        List<TokenUserEntity> users = actualConvertToTokenEntityResult.getUsers();
        assertEquals(1, users.size());
        assertNull(actualConvertToTokenEntityResult.getUpdatedAt());
        assertEquals(TokenType.INSTITUTION, actualConvertToTokenEntityResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenEntityResult.getStatus());
        assertEquals("42", actualConvertToTokenEntityResult.getProductId());
        assertEquals("Contract Template", actualConvertToTokenEntityResult.getContractTemplate());
        assertEquals("42", actualConvertToTokenEntityResult.getInstitutionId());
        assertNull(actualConvertToTokenEntityResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenEntityResult.getContractSigned());
        assertNull(actualConvertToTokenEntityResult.getExpiringDate());
        InstitutionUpdateEntity institutionUpdate1 = actualConvertToTokenEntityResult.getInstitutionUpdate();
        assertEquals("Share Capital", institutionUpdate1.getShareCapital());
        assertEquals("Rea", institutionUpdate1.getRea());
        assertEquals("21654", institutionUpdate1.getZipCode());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("The characteristics of someone or something", institutionUpdate1.getDescription());
        assertEquals("jane.doe@example.org", institutionUpdate1.getSupportEmail());
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertEquals("42 Main St", institutionUpdate1.getDigitalAddress());
        assertEquals("6625550144", institutionUpdate1.getSupportPhone());
    }

    /**
     * Method under test: {@link TokenMapper#convertToTokenEntity(Token, List)}
     */
    @Test
    void testConvertToTokenEntity4() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        TokenUser tokenUser = new TokenUser();
        tokenUser.setRole(PartyRole.MANAGER);
        tokenUser.setUserId("42");

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(tokenUser);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setInstitutionUpdate(institutionUpdate);
        token.setId(null);
        token.setUsers(tokenUserList);

        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));
        TokenEntity actualConvertToTokenEntityResult = TokenMapper.convertToTokenEntity(token,
                institutionGeographicTaxonomiesList);
        assertEquals("Checksum", actualConvertToTokenEntityResult.getChecksum());
        List<TokenUserEntity> users = actualConvertToTokenEntityResult.getUsers();
        assertEquals(1, users.size());
        assertNull(actualConvertToTokenEntityResult.getUpdatedAt());
        assertEquals(TokenType.INSTITUTION, actualConvertToTokenEntityResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenEntityResult.getStatus());
        assertEquals("42", actualConvertToTokenEntityResult.getProductId());
        assertEquals("Contract Template", actualConvertToTokenEntityResult.getContractTemplate());
        assertEquals("42", actualConvertToTokenEntityResult.getInstitutionId());
        assertNull(actualConvertToTokenEntityResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenEntityResult.getContractSigned());
        assertNull(actualConvertToTokenEntityResult.getExpiringDate());
        InstitutionUpdateEntity institutionUpdate1 = actualConvertToTokenEntityResult.getInstitutionUpdate();
        assertEquals("Share Capital", institutionUpdate1.getShareCapital());
        assertEquals("Rea", institutionUpdate1.getRea());
        assertEquals("21654", institutionUpdate1.getZipCode());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("The characteristics of someone or something", institutionUpdate1.getDescription());
        assertEquals("jane.doe@example.org", institutionUpdate1.getSupportEmail());
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertEquals("42 Main St", institutionUpdate1.getDigitalAddress());
        List<GeoTaxonomyEntity> geographicTaxonomies = institutionUpdate1.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertEquals("6625550144", institutionUpdate1.getSupportPhone());
    }

    /**
     * Method under test: {@link TokenMapper#convertToTokenEntity(Token, List)}
     */
    @Test
    void testConvertToTokenEntity5() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        institutionUpdate.setPaymentServiceProvider(null);
        institutionUpdate.setDataProtectionOfficer(null);

        TokenUser tokenUser = new TokenUser();
        tokenUser.setRole(PartyRole.MANAGER);
        tokenUser.setUserId("42");

        ArrayList<TokenUser> tokenUserList = new ArrayList<>();
        tokenUserList.add(tokenUser);

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setInstitutionUpdate(institutionUpdate);
        token.setId(null);
        token.setUsers(tokenUserList);
        TokenEntity actualConvertToTokenEntityResult = TokenMapper.convertToTokenEntity(token, null);
        assertEquals("Checksum", actualConvertToTokenEntityResult.getChecksum());
        List<TokenUserEntity> users = actualConvertToTokenEntityResult.getUsers();
        assertEquals(1, users.size());
        assertNull(actualConvertToTokenEntityResult.getUpdatedAt());
        assertEquals(TokenType.INSTITUTION, actualConvertToTokenEntityResult.getType());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenEntityResult.getStatus());
        assertEquals("42", actualConvertToTokenEntityResult.getProductId());
        assertEquals("Contract Template", actualConvertToTokenEntityResult.getContractTemplate());
        assertEquals("42", actualConvertToTokenEntityResult.getInstitutionId());
        assertNull(actualConvertToTokenEntityResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenEntityResult.getContractSigned());
        assertNull(actualConvertToTokenEntityResult.getExpiringDate());
        InstitutionUpdateEntity institutionUpdate1 = actualConvertToTokenEntityResult.getInstitutionUpdate();
        assertEquals("Share Capital", institutionUpdate1.getShareCapital());
        assertEquals("Rea", institutionUpdate1.getRea());
        assertEquals("42 Main St", institutionUpdate1.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate1.getDescription());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("6625550144", institutionUpdate1.getSupportPhone());
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("jane.doe@example.org", institutionUpdate1.getSupportEmail());
        assertEquals("21654", institutionUpdate1.getZipCode());
        TokenUserEntity getResult = users.get(0);
        assertEquals(PartyRole.MANAGER, getResult.getRole());
        assertEquals("42", getResult.getUserId());
    }

    /**
     * Method under test: {@link TokenMapper#convertToToken(TokenEntity)}
     */
    @Test
    void testConvertToToken() {
        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode("Abi Code");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

        InstitutionUpdateEntity institutionUpdateEntity = new InstitutionUpdateEntity();
        institutionUpdateEntity.setAddress("42 Main St");
        institutionUpdateEntity.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionUpdateEntity.setDescription("The characteristics of someone or something");
        institutionUpdateEntity.setDigitalAddress("42 Main St");
        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        institutionUpdateEntity.setGeographicTaxonomies(geoTaxonomyEntityList);
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setDeletedAt(null);
        tokenEntity.setContractSigned("Contract Signed");
        tokenEntity.setContractTemplate("Contract Template");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate(null);
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setInstitutionUpdate(institutionUpdateEntity);
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setType(TokenType.INSTITUTION);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setUsers(new ArrayList<>());
        Token actualConvertToTokenResult = TokenMapper.convertToToken(tokenEntity);
        assertEquals("Checksum", actualConvertToTokenResult.getChecksum());
        assertNull(actualConvertToTokenResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("42", actualConvertToTokenResult.getInstitutionId());
        assertEquals("Contract Template", actualConvertToTokenResult.getContractTemplate());
        assertNull(actualConvertToTokenResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenResult.getContractSigned());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertEquals("42", actualConvertToTokenResult.getId());
    }

    /**
     * Method under test: {@link TokenMapper#convertToToken(TokenEntity)}
     */
    @Test
    void testConvertToToken2() {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setDeletedAt(null);
        tokenEntity.setContractSigned("Contract Signed");
        tokenEntity.setContractTemplate("Contract Template");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate(null);
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setType(TokenType.INSTITUTION);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setInstitutionUpdate(null);
        tokenEntity.setUsers(null);
        Token actualConvertToTokenResult = TokenMapper.convertToToken(tokenEntity);
        assertEquals("Checksum", actualConvertToTokenResult.getChecksum());
        assertNull(actualConvertToTokenResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("42", actualConvertToTokenResult.getInstitutionId());
        assertEquals("42", actualConvertToTokenResult.getId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertNull(actualConvertToTokenResult.getCreatedAt());
        assertEquals("Contract Template", actualConvertToTokenResult.getContractTemplate());
        assertEquals("Contract Signed", actualConvertToTokenResult.getContractSigned());
    }

    /**
     * Method under test: {@link TokenMapper#convertToToken(TokenEntity)}
     */
    @Test
    void testConvertToToken3() {
        InstitutionUpdateEntity institutionUpdateEntity = new InstitutionUpdateEntity();
        institutionUpdateEntity.setAddress("42 Main St");
        institutionUpdateEntity.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity.setDescription("The characteristics of someone or something");
        institutionUpdateEntity.setDigitalAddress("42 Main St");
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");
        institutionUpdateEntity.setGeographicTaxonomies(null);
        institutionUpdateEntity.setPaymentServiceProvider(null);
        institutionUpdateEntity.setDataProtectionOfficer(null);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setDeletedAt(null);
        tokenEntity.setContractSigned("Contract Signed");
        tokenEntity.setContractTemplate("Contract Template");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate(null);
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setType(TokenType.INSTITUTION);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setInstitutionUpdate(institutionUpdateEntity);
        tokenEntity.setUsers(null);
        Token actualConvertToTokenResult = TokenMapper.convertToToken(tokenEntity);
        assertEquals("Checksum", actualConvertToTokenResult.getChecksum());
        assertNull(actualConvertToTokenResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("42", actualConvertToTokenResult.getInstitutionId());
        assertEquals("Contract Template", actualConvertToTokenResult.getContractTemplate());
        assertNull(actualConvertToTokenResult.getCreatedAt());
        assertEquals("Contract Signed", actualConvertToTokenResult.getContractSigned());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertEquals("42", actualConvertToTokenResult.getId());
        InstitutionUpdate institutionUpdate = actualConvertToTokenResult.getInstitutionUpdate();
        assertEquals("Rea", institutionUpdate.getRea());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("6625550144", institutionUpdate.getSupportPhone());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertTrue(institutionUpdate.isImported());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
    }

    /**
     * Method under test: {@link TokenMapper#convertToToken(TokenEntity)}
     */
    @Test
    void testConvertToToken4() {
        TokenUserEntity tokenUserEntity = new TokenUserEntity();
        tokenUserEntity.setRole(PartyRole.MANAGER);
        tokenUserEntity.setUserId("42");

        ArrayList<TokenUserEntity> tokenUserEntityList = new ArrayList<>();
        tokenUserEntityList.add(tokenUserEntity);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setDeletedAt(null);
        tokenEntity.setContractSigned("Contract Signed");
        tokenEntity.setContractTemplate("Contract Template");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate(null);
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setType(TokenType.INSTITUTION);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setInstitutionUpdate(null);
        tokenEntity.setUsers(tokenUserEntityList);
        Token actualConvertToTokenResult = TokenMapper.convertToToken(tokenEntity);
        assertEquals("Checksum", actualConvertToTokenResult.getChecksum());
        List<TokenUser> users = actualConvertToTokenResult.getUsers();
        assertEquals(1, users.size());
        assertNull(actualConvertToTokenResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("Contract Template", actualConvertToTokenResult.getContractTemplate());
        assertEquals("42", actualConvertToTokenResult.getId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertNull(actualConvertToTokenResult.getCreatedAt());
        assertEquals("42", actualConvertToTokenResult.getInstitutionId());
        assertEquals("Contract Signed", actualConvertToTokenResult.getContractSigned());
        TokenUser getResult = users.get(0);
        assertEquals(PartyRole.MANAGER, getResult.getRole());
        assertEquals("42", getResult.getUserId());
    }

    /**
     * Method under test: {@link TokenMapper#convertToToken(TokenEntity)}
     */
    @Test
    void testConvertToToken5() {
        List<TokenUserEntity> tokenUserEntityList = new ArrayList<>();

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setDeletedAt(null);
        tokenEntity.setContractSigned("Contract Signed");
        tokenEntity.setContractTemplate("Contract Template");
        tokenEntity.setCreatedAt(null);
        tokenEntity.setExpiringDate(null);
        tokenEntity.setId("42");
        tokenEntity.setInstitutionId("42");
        tokenEntity.setProductId("42");
        tokenEntity.setStatus(RelationshipState.PENDING);
        tokenEntity.setType(TokenType.INSTITUTION);
        tokenEntity.setUpdatedAt(null);
        tokenEntity.setInstitutionUpdate(null);
        tokenEntity.setUsers(tokenUserEntityList);
        Token actualConvertToTokenResult = TokenMapper.convertToToken(tokenEntity);
        assertEquals("Checksum", actualConvertToTokenResult.getChecksum());
        assertTrue(actualConvertToTokenResult.getUsers().isEmpty());
        assertNull(actualConvertToTokenResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualConvertToTokenResult.getStatus());
        assertEquals("42", actualConvertToTokenResult.getProductId());
        assertEquals("42", actualConvertToTokenResult.getInstitutionId());
        assertEquals("42", actualConvertToTokenResult.getId());
        assertNull(actualConvertToTokenResult.getExpiringDate());
        assertNull(actualConvertToTokenResult.getCreatedAt());
        assertEquals("Contract Template", actualConvertToTokenResult.getContractTemplate());
        assertEquals("Contract Signed", actualConvertToTokenResult.getContractSigned());
    }
}

