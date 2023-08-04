package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity;
import it.pagopa.selfcare.mscore.connector.dao.utils.DaoMockUtils;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.utils.MockUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenConnectorImplTest {

    @InjectMocks
    TokenConnectorImpl tokenConnectorImpl;

    @Mock
    TokenRepository tokenRepository;

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    @Captor
    ArgumentCaptor<Pageable> pageableArgumentCaptor;

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll() {
        when(tokenRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(tokenConnectorImpl.findAll().isEmpty());
        verify(tokenRepository).findAll();
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll2() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
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
        tokenEntity.setClosedAt(null);
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

        ArrayList<TokenEntity> tokenEntityList = new ArrayList<>();
        tokenEntityList.add(tokenEntity);
        when(tokenRepository.findAll()).thenReturn(tokenEntityList);
        assertEquals(1, tokenConnectorImpl.findAll().size());
        verify(tokenRepository).findAll();
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll3() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
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
        tokenEntity.setClosedAt(null);
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

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("17 High St");
        dataProtectionOfficerEntity1.setEmail("john.smith@example.org");
        dataProtectionOfficerEntity1
                .setPec("it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1
                .setAbiCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity");
        paymentServiceProviderEntity1.setBusinessRegisterNumber("Business Register Number");
        paymentServiceProviderEntity1
                .setLegalRegisterName("it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity");
        paymentServiceProviderEntity1.setLegalRegisterNumber("Legal Register Number");
        paymentServiceProviderEntity1.setVatNumberGroup(false);

        InstitutionUpdateEntity institutionUpdateEntity1 = new InstitutionUpdateEntity();
        institutionUpdateEntity1.setAddress("17 High St");
        institutionUpdateEntity1
                .setBusinessRegisterPlace("it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity");
        institutionUpdateEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionUpdateEntity1.setDescription("Description");
        institutionUpdateEntity1.setDigitalAddress("17 High St");
        institutionUpdateEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity1.setImported(false);
        institutionUpdateEntity1.setInstitutionType(InstitutionType.PG);
        institutionUpdateEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionUpdateEntity1.setRea("it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity");
        institutionUpdateEntity1
                .setShareCapital("it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity");
        institutionUpdateEntity1.setSupportEmail("john.smith@example.org");
        institutionUpdateEntity1.setSupportPhone("8605550118");
        institutionUpdateEntity1
                .setTaxCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity");
        institutionUpdateEntity1.setZipCode("OX1 1PT");

        TokenEntity tokenEntity1 = new TokenEntity();
        tokenEntity1.setChecksum("it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity");
        tokenEntity1.setClosedAt(null);
        tokenEntity1.setContractSigned("it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity");
        tokenEntity1.setContractTemplate("it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity");
        tokenEntity1.setCreatedAt(null);
        tokenEntity1.setExpiringDate(null);
        tokenEntity1.setId("Id");
        tokenEntity1.setInstitutionId("Institution Id");
        tokenEntity1.setInstitutionUpdate(institutionUpdateEntity1);
        tokenEntity1.setProductId("Product Id");
        tokenEntity1.setStatus(RelationshipState.ACTIVE);
        tokenEntity1.setType(TokenType.LEGALS);
        tokenEntity1.setUpdatedAt(null);
        tokenEntity1.setUsers(new ArrayList<>());

        ArrayList<TokenEntity> tokenEntityList = new ArrayList<>();
        tokenEntityList.add(tokenEntity1);
        tokenEntityList.add(tokenEntity);
        when(tokenRepository.findAll()).thenReturn(tokenEntityList);
        assertEquals(2, tokenConnectorImpl.findAll().size());
        verify(tokenRepository).findAll();
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll4() {
        when(tokenRepository.findAll()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        assertThrows(ResourceNotFoundException.class, () -> tokenConnectorImpl.findAll());
        verify(tokenRepository).findAll();
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll5() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("42 Main St");
        dataProtectionOfficerEntity1.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity1.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1.setAbiCode("Abi Code");
        paymentServiceProviderEntity1.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity1.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity1.setLegalRegisterNumber("42");
        paymentServiceProviderEntity1.setVatNumberGroup(true);

        DataProtectionOfficerEntity dataProtectionOfficerEntity2 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity2.setAddress("42 Main St");
        dataProtectionOfficerEntity2.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity2.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity2 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity2.setAbiCode("Abi Code");
        paymentServiceProviderEntity2.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity2.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity2.setLegalRegisterNumber("42");
        paymentServiceProviderEntity2.setVatNumberGroup(true);

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionUpdateEntity institutionUpdateEntity1 = mock(InstitutionUpdateEntity.class);
        when(institutionUpdateEntity1.isImported()).thenReturn(true);
        when(institutionUpdateEntity1.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity2);
        when(institutionUpdateEntity1.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity2);
        when(institutionUpdateEntity1.getAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionUpdateEntity1.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionUpdateEntity1.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getRea()).thenReturn("Rea");
        when(institutionUpdateEntity1.getShareCapital()).thenReturn("Share Capital");
        when(institutionUpdateEntity1.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionUpdateEntity1.getSupportPhone()).thenReturn("6625550144");
        when(institutionUpdateEntity1.getTaxCode()).thenReturn("Tax Code");
        when(institutionUpdateEntity1.getZipCode()).thenReturn("21654");
        when(institutionUpdateEntity1.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        institutionUpdateEntity1.setAddress("42 Main St");
        institutionUpdateEntity1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionUpdateEntity1.setDescription("The characteristics of someone or something");
        institutionUpdateEntity1.setDigitalAddress("42 Main St");
        institutionUpdateEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity1.setImported(true);
        institutionUpdateEntity1.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionUpdateEntity1.setRea("Rea");
        institutionUpdateEntity1.setShareCapital("Share Capital");
        institutionUpdateEntity1.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity1.setSupportPhone("6625550144");
        institutionUpdateEntity1.setTaxCode("Tax Code");
        institutionUpdateEntity1.setZipCode("21654");
        TokenEntity tokenEntity = mock(TokenEntity.class);
        when(tokenEntity.getInstitutionUpdate()).thenReturn(institutionUpdateEntity1);
        when(tokenEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(tokenEntity.getChecksum()).thenReturn("Checksum");
        when(tokenEntity.getContractSigned()).thenReturn("Contract Signed");
        when(tokenEntity.getContractTemplate()).thenReturn("Contract Template");
        when(tokenEntity.getId()).thenReturn("42");
        when(tokenEntity.getInstitutionId()).thenReturn("42");
        when(tokenEntity.getProductId()).thenReturn("42");
        when(tokenEntity.getCreatedAt()).thenReturn(null);
        when(tokenEntity.getExpiringDate()).thenReturn(null);
        when(tokenEntity.getUpdatedAt()).thenReturn(null);
        when(tokenEntity.getUsers()).thenReturn(new ArrayList<>());
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setClosedAt(null);
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

        ArrayList<TokenEntity> tokenEntityList = new ArrayList<>();
        tokenEntityList.add(tokenEntity);
        when(tokenRepository.findAll()).thenReturn(tokenEntityList);
        assertEquals(1, tokenConnectorImpl.findAll().size());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findAll()}
     */
    @Test
    void testFindAll6() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("42 Main St");
        dataProtectionOfficerEntity1.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity1.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1.setAbiCode("Abi Code");
        paymentServiceProviderEntity1.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity1.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity1.setLegalRegisterNumber("42");
        paymentServiceProviderEntity1.setVatNumberGroup(true);

        DataProtectionOfficerEntity dataProtectionOfficerEntity2 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity2.setAddress("42 Main St");
        dataProtectionOfficerEntity2.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity2.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity2 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity2.setAbiCode("Abi Code");
        paymentServiceProviderEntity2.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity2.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity2.setLegalRegisterNumber("42");
        paymentServiceProviderEntity2.setVatNumberGroup(true);

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        GeoTaxonomyEntity geoTaxonomyEntity1 = new GeoTaxonomyEntity();
        geoTaxonomyEntity1.setCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity");
        geoTaxonomyEntity1.setDesc("Desc");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity1);
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionUpdateEntity institutionUpdateEntity1 = mock(InstitutionUpdateEntity.class);
        when(institutionUpdateEntity1.isImported()).thenReturn(true);
        when(institutionUpdateEntity1.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity2);
        when(institutionUpdateEntity1.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity2);
        when(institutionUpdateEntity1.getAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionUpdateEntity1.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionUpdateEntity1.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getRea()).thenReturn("Rea");
        when(institutionUpdateEntity1.getShareCapital()).thenReturn("Share Capital");
        when(institutionUpdateEntity1.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionUpdateEntity1.getSupportPhone()).thenReturn("6625550144");
        when(institutionUpdateEntity1.getTaxCode()).thenReturn("Tax Code");
        when(institutionUpdateEntity1.getZipCode()).thenReturn("21654");
        when(institutionUpdateEntity1.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        institutionUpdateEntity1.setAddress("42 Main St");
        institutionUpdateEntity1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionUpdateEntity1.setDescription("The characteristics of someone or something");
        institutionUpdateEntity1.setDigitalAddress("42 Main St");
        institutionUpdateEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity1.setImported(true);
        institutionUpdateEntity1.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionUpdateEntity1.setRea("Rea");
        institutionUpdateEntity1.setShareCapital("Share Capital");
        institutionUpdateEntity1.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity1.setSupportPhone("6625550144");
        institutionUpdateEntity1.setTaxCode("Tax Code");
        institutionUpdateEntity1.setZipCode("21654");
        TokenEntity tokenEntity = mock(TokenEntity.class);
        when(tokenEntity.getInstitutionUpdate()).thenReturn(institutionUpdateEntity1);
        when(tokenEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(tokenEntity.getChecksum()).thenReturn("Checksum");
        when(tokenEntity.getContractSigned()).thenReturn("Contract Signed");
        when(tokenEntity.getContractTemplate()).thenReturn("Contract Template");
        when(tokenEntity.getId()).thenReturn("42");
        when(tokenEntity.getInstitutionId()).thenReturn("42");
        when(tokenEntity.getProductId()).thenReturn("42");
        when(tokenEntity.getCreatedAt()).thenReturn(null);
        when(tokenEntity.getExpiringDate()).thenReturn(null);
        when(tokenEntity.getUpdatedAt()).thenReturn(null);
        when(tokenEntity.getUsers()).thenReturn(new ArrayList<>());
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setClosedAt(null);
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

        ArrayList<TokenEntity> tokenEntityList = new ArrayList<>();
        tokenEntityList.add(tokenEntity);
        when(tokenRepository.findAll()).thenReturn(tokenEntityList);
        assertEquals(1, tokenConnectorImpl.findAll().size());
        verify(tokenRepository).findAll();
    }

    @Test
    void deleteById() {
        doNothing().when(tokenRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> tokenConnectorImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void findActiveContractTest() {
        TokenEntity token = new TokenEntity();
        token.setId("507f1f77bcf86cd799439011");
        token.setUsers(new ArrayList<>());
        when(tokenRepository.find(any(), any())).thenReturn(List.of(token));
        Token tokenResp = tokenConnectorImpl.findActiveContract("42", "42", "42");
        Assertions.assertEquals("507f1f77bcf86cd799439011", tokenResp.getId());
    }

    @Test
    void findActiveContractTest1() {
        when(tokenRepository.find(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> tokenConnectorImpl.findActiveContract("42", "42", "42"));

    }

    @Test
    void save() {
        Token token = new Token();
        token.setId("507f1f77bcf86cd799439011");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setId("507f1f77bcf86cd799439011");
        tokenEntity.setUsers(new ArrayList<>());
        token.setUsers(new ArrayList<>());
        when(tokenRepository.save(any())).thenReturn(tokenEntity);
        Token response = tokenConnectorImpl.save(token, new ArrayList<>());
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

    @Test
    void save2() {
        Token token = new Token();
        token.setProductId("507f1f77bcf86cd799439011");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setProductId("507f1f77bcf86cd799439011");
        tokenEntity.setUsers(new ArrayList<>());
        token.setUsers(new ArrayList<>());
        when(tokenRepository.save(any())).thenReturn(tokenEntity);
        Token response = tokenConnectorImpl.save(token, new ArrayList<>());
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getProductId());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#save(Token, List<InstitutionGeographicTaxonomies>)}
     */
    @Test
    void testSave() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
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
        tokenEntity.setClosedAt(null);
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
        when(tokenRepository.save(org.mockito.Mockito.any())).thenReturn(tokenEntity);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        Token actualSaveResult = tokenConnectorImpl.save(token, null);
        assertEquals("Checksum", actualSaveResult.getChecksum());
        assertNull(actualSaveResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualSaveResult.getStatus());
        assertEquals("42", actualSaveResult.getProductId());
        assertEquals("42", actualSaveResult.getInstitutionId());
        assertEquals("Contract Template", actualSaveResult.getContractTemplate());
        assertNull(actualSaveResult.getCreatedAt());
        assertEquals("Contract Signed", actualSaveResult.getContractSigned());
        assertNull(actualSaveResult.getExpiringDate());
        assertEquals("42", actualSaveResult.getId());
        InstitutionUpdate institutionUpdate1 = actualSaveResult.getInstitutionUpdate();
        assertEquals("Rea", institutionUpdate1.getRea());
        PaymentServiceProvider paymentServiceProvider1 = institutionUpdate1.getPaymentServiceProvider();
        assertEquals(paymentServiceProvider, paymentServiceProvider1);
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertEquals("21654", institutionUpdate1.getZipCode());
        assertEquals("42 Main St", institutionUpdate1.getDigitalAddress());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#save(Token, List<InstitutionGeographicTaxonomies>)}
     */
    @Test
    void testSave2() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("42 Main St");
        dataProtectionOfficerEntity1.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity1.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1.setAbiCode("Abi Code");
        paymentServiceProviderEntity1.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity1.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity1.setLegalRegisterNumber("42");
        paymentServiceProviderEntity1.setVatNumberGroup(true);

        DataProtectionOfficerEntity dataProtectionOfficerEntity2 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity2.setAddress("42 Main St");
        dataProtectionOfficerEntity2.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity2.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity2 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity2.setAbiCode("Abi Code");
        paymentServiceProviderEntity2.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity2.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity2.setLegalRegisterNumber("42");
        paymentServiceProviderEntity2.setVatNumberGroup(true);

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionUpdateEntity institutionUpdateEntity1 = mock(InstitutionUpdateEntity.class);
        when(institutionUpdateEntity1.isImported()).thenReturn(true);
        when(institutionUpdateEntity1.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity2);
        when(institutionUpdateEntity1.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity2);
        when(institutionUpdateEntity1.getAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionUpdateEntity1.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionUpdateEntity1.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getRea()).thenReturn("Rea");
        when(institutionUpdateEntity1.getShareCapital()).thenReturn("Share Capital");
        when(institutionUpdateEntity1.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionUpdateEntity1.getSupportPhone()).thenReturn("6625550144");
        when(institutionUpdateEntity1.getTaxCode()).thenReturn("Tax Code");
        when(institutionUpdateEntity1.getZipCode()).thenReturn("21654");
        when(institutionUpdateEntity1.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        institutionUpdateEntity1.setAddress("42 Main St");
        institutionUpdateEntity1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionUpdateEntity1.setDescription("The characteristics of someone or something");
        institutionUpdateEntity1.setDigitalAddress("42 Main St");
        institutionUpdateEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity1.setImported(true);
        institutionUpdateEntity1.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionUpdateEntity1.setRea("Rea");
        institutionUpdateEntity1.setShareCapital("Share Capital");
        institutionUpdateEntity1.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity1.setSupportPhone("6625550144");
        institutionUpdateEntity1.setTaxCode("Tax Code");
        institutionUpdateEntity1.setZipCode("21654");
        TokenEntity tokenEntity = mock(TokenEntity.class);
        when(tokenEntity.getInstitutionUpdate()).thenReturn(institutionUpdateEntity1);
        when(tokenEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(tokenEntity.getChecksum()).thenReturn("Checksum");
        when(tokenEntity.getContractSigned()).thenReturn("Contract Signed");
        when(tokenEntity.getContractTemplate()).thenReturn("Contract Template");
        when(tokenEntity.getId()).thenReturn("42");
        when(tokenEntity.getInstitutionId()).thenReturn("42");
        when(tokenEntity.getProductId()).thenReturn("42");
        when(tokenEntity.getCreatedAt()).thenReturn(null);
        when(tokenEntity.getExpiringDate()).thenReturn(null);
        when(tokenEntity.getUpdatedAt()).thenReturn(null);
        when(tokenEntity.getUsers()).thenReturn(new ArrayList<>());
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setClosedAt(null);
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
        when(tokenRepository.save(org.mockito.Mockito.any())).thenReturn(tokenEntity);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        Token actualSaveResult = tokenConnectorImpl.save(token, null);
        assertEquals("Checksum", actualSaveResult.getChecksum());
        assertNull(actualSaveResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualSaveResult.getStatus());
        assertEquals("42", actualSaveResult.getProductId());
        assertEquals("42", actualSaveResult.getInstitutionId());
        assertEquals("Contract Template", actualSaveResult.getContractTemplate());
        assertNull(actualSaveResult.getCreatedAt());
        assertEquals("Contract Signed", actualSaveResult.getContractSigned());
        assertNull(actualSaveResult.getExpiringDate());
        assertEquals("42", actualSaveResult.getId());
        InstitutionUpdate institutionUpdate1 = actualSaveResult.getInstitutionUpdate();
        assertEquals("Rea", institutionUpdate1.getRea());
        PaymentServiceProvider paymentServiceProvider1 = institutionUpdate1.getPaymentServiceProvider();
        assertEquals(paymentServiceProvider, paymentServiceProvider1);
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertEquals("21654", institutionUpdate1.getZipCode());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#save(Token, List<InstitutionGeographicTaxonomies>)}
     */
    @Test
    void testSave3() {
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
        institutionUpdateEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity.setImported(true);
        institutionUpdateEntity.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionUpdateEntity.setRea("Rea");
        institutionUpdateEntity.setShareCapital("Share Capital");
        institutionUpdateEntity.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity.setSupportPhone("6625550144");
        institutionUpdateEntity.setTaxCode("Tax Code");
        institutionUpdateEntity.setZipCode("21654");

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("42 Main St");
        dataProtectionOfficerEntity1.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity1.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1.setAbiCode("Abi Code");
        paymentServiceProviderEntity1.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity1.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity1.setLegalRegisterNumber("42");
        paymentServiceProviderEntity1.setVatNumberGroup(true);

        DataProtectionOfficerEntity dataProtectionOfficerEntity2 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity2.setAddress("42 Main St");
        dataProtectionOfficerEntity2.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity2.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity2 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity2.setAbiCode("Abi Code");
        paymentServiceProviderEntity2.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity2.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity2.setLegalRegisterNumber("42");
        paymentServiceProviderEntity2.setVatNumberGroup(true);

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        GeoTaxonomyEntity geoTaxonomyEntity1 = new GeoTaxonomyEntity();
        geoTaxonomyEntity1.setCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity");
        geoTaxonomyEntity1.setDesc("Desc");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity1);
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionUpdateEntity institutionUpdateEntity1 = mock(InstitutionUpdateEntity.class);
        when(institutionUpdateEntity1.isImported()).thenReturn(true);
        when(institutionUpdateEntity1.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity2);
        when(institutionUpdateEntity1.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity2);
        when(institutionUpdateEntity1.getAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionUpdateEntity1.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionUpdateEntity1.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionUpdateEntity1.getRea()).thenReturn("Rea");
        when(institutionUpdateEntity1.getShareCapital()).thenReturn("Share Capital");
        when(institutionUpdateEntity1.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionUpdateEntity1.getSupportPhone()).thenReturn("6625550144");
        when(institutionUpdateEntity1.getTaxCode()).thenReturn("Tax Code");
        when(institutionUpdateEntity1.getZipCode()).thenReturn("21654");
        when(institutionUpdateEntity1.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        institutionUpdateEntity1.setAddress("42 Main St");
        institutionUpdateEntity1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdateEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionUpdateEntity1.setDescription("The characteristics of someone or something");
        institutionUpdateEntity1.setDigitalAddress("42 Main St");
        institutionUpdateEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdateEntity1.setImported(true);
        institutionUpdateEntity1.setInstitutionType(InstitutionType.PA);
        institutionUpdateEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionUpdateEntity1.setRea("Rea");
        institutionUpdateEntity1.setShareCapital("Share Capital");
        institutionUpdateEntity1.setSupportEmail("jane.doe@example.org");
        institutionUpdateEntity1.setSupportPhone("6625550144");
        institutionUpdateEntity1.setTaxCode("Tax Code");
        institutionUpdateEntity1.setZipCode("21654");
        TokenEntity tokenEntity = mock(TokenEntity.class);
        when(tokenEntity.getInstitutionUpdate()).thenReturn(institutionUpdateEntity1);
        when(tokenEntity.getStatus()).thenReturn(RelationshipState.PENDING);
        when(tokenEntity.getChecksum()).thenReturn("Checksum");
        when(tokenEntity.getContractSigned()).thenReturn("Contract Signed");
        when(tokenEntity.getContractTemplate()).thenReturn("Contract Template");
        when(tokenEntity.getId()).thenReturn("42");
        when(tokenEntity.getInstitutionId()).thenReturn("42");
        when(tokenEntity.getProductId()).thenReturn("42");
        when(tokenEntity.getCreatedAt()).thenReturn(null);
        when(tokenEntity.getExpiringDate()).thenReturn(null);
        when(tokenEntity.getUpdatedAt()).thenReturn(null);
        when(tokenEntity.getUsers()).thenReturn(new ArrayList<>());
        tokenEntity.setChecksum("Checksum");
        tokenEntity.setClosedAt(null);
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
        when(tokenRepository.save(org.mockito.Mockito.any())).thenReturn(tokenEntity);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
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
        Token actualSaveResult = tokenConnectorImpl.save(token, null);
        assertEquals("Checksum", actualSaveResult.getChecksum());
        assertNull(actualSaveResult.getUpdatedAt());
        assertEquals(RelationshipState.PENDING, actualSaveResult.getStatus());
        assertEquals("42", actualSaveResult.getProductId());
        assertEquals("42", actualSaveResult.getInstitutionId());
        assertEquals("Contract Template", actualSaveResult.getContractTemplate());
        assertNull(actualSaveResult.getCreatedAt());
        assertEquals("Contract Signed", actualSaveResult.getContractSigned());
        assertNull(actualSaveResult.getExpiringDate());
        assertEquals("42", actualSaveResult.getId());
        InstitutionUpdate institutionUpdate1 = actualSaveResult.getInstitutionUpdate();
        assertEquals("Rea", institutionUpdate1.getRea());
        PaymentServiceProvider paymentServiceProvider1 = institutionUpdate1.getPaymentServiceProvider();
        assertEquals(paymentServiceProvider, paymentServiceProvider1);
        assertEquals("Tax Code", institutionUpdate1.getTaxCode());
        assertTrue(institutionUpdate1.isImported());
        assertEquals("42 Main St", institutionUpdate1.getAddress());
        assertEquals("Business Register Place", institutionUpdate1.getBusinessRegisterPlace());
        assertEquals("21654", institutionUpdate1.getZipCode());
    }

    /**
     * Method under test: {@link TokenConnectorImpl#findById(String)}
     */
    @Test
    void testFindById() {
        when(tokenRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tokenConnectorImpl.findById("42"));
        verify(tokenRepository).findById(any());
    }

    @Test
    void testFindById2() {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsers(new ArrayList<>());
        when(tokenRepository.findById(any())).thenReturn(Optional.of(tokenEntity));
        Token response = tokenConnectorImpl.findById("tokenId");
        assertNotNull(response);
    }

    @Test
    void testFindAndUpdateTokenUser() {
        // Given
        Token tokenMock = MockUtils.createTokenMock(null, RelationshipState.DELETED, InstitutionType.PSP);
        RelationshipState statusMock = RelationshipState.DELETED;
        String digestMock = "digestMock";
        TokenEntity updatedTokenMock = DaoMockUtils.createTokenEntityMock(null, RelationshipState.DELETED);

        when(tokenRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedTokenMock);
        // When
        Token result = tokenConnectorImpl.findAndUpdateToken(tokenMock, statusMock, digestMock);
        // Then
        assertNotNull(result);
        verify(tokenRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()).toString().contains(tokenMock.getId()));
        Update capturedUpdate = updateArgumentCaptor.getValue();
        assertTrue(capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.status.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.updatedAt.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(statusMock.toString()));
        assertTrue(capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.checksum.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.contractSigned.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.contentType.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.closedAt.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(digestMock) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(tokenMock.getContractSigned()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(tokenMock.getContentType()));
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findAndUpdateToken() {
        // Given
        Token tokenMock = MockUtils.createTokenMock(null, RelationshipState.TOBEVALIDATED, InstitutionType.GSP);
        tokenMock.setContractSigned(null);
        tokenMock.setContentType(null);
        RelationshipState statusMock = RelationshipState.PENDING;
        TokenEntity updatedTokenMock = DaoMockUtils.createTokenEntityMock(null, RelationshipState.PENDING);

        when(tokenRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedTokenMock);
        // When
        Token result = tokenConnectorImpl.findAndUpdateToken(tokenMock, statusMock, null);
        // Then
        assertNotNull(result);
        verify(tokenRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()).toString().contains(tokenMock.getId()));
        Update capturedUpdate = updateArgumentCaptor.getValue();
        assertTrue(capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.status.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(TokenEntity.Fields.updatedAt.name()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(statusMock.toString()));
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findWithFilter() {
        TokenEntity token = new TokenEntity();
        token.setId("507f1f77bcf86cd799439011");
        token.setUsers(new ArrayList<>());
        when(tokenRepository.find(any(), any())).thenReturn(List.of(token));
        Token tokens = tokenConnectorImpl.findWithFilter("42", "42");
        Assertions.assertEquals("507f1f77bcf86cd799439011", tokens.getId());
    }

    @Test
    void updateOnboardedProductCreatedAt() {
        // Given
        String tokenIdMock = "tokenIdMock";
        OffsetDateTime createdAt = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        TokenEntity updatedTokenMock = mockInstance(new TokenEntity());
        updatedTokenMock.setId(tokenIdMock);
        when(tokenRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedTokenMock);
        // When
        Token result = tokenConnectorImpl.updateTokenCreatedAt(tokenIdMock, createdAt);
        // Then
        assertNotNull(result);
        assertEquals(result.getId(), tokenIdMock);
        verify(tokenRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertSame(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()), tokenIdMock);
        assertSame(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()), tokenIdMock);
        Update capturedUpdate = updateArgumentCaptor.getValue();
        assertTrue(capturedUpdate.getUpdateObject().get("$set").toString().contains("updatedAt") &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains("updatedAt") &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(createdAt.toString()));
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        String productId = "prod-io";
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, productId, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertEquals(productId, capturedQuery.getQueryObject().get(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId_productIdNull() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, null, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertFalse(capturedQuery.getQueryObject().toString().contains(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId_productIdBlank() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        String productId = "";
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, productId, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertFalse(capturedQuery.getQueryObject().toString().contains(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }
}
