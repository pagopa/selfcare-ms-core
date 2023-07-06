package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.constant.*;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {InstitutionConnectorImpl.class})
@ExtendWith(MockitoExtension.class)
class InstitutionConnectorImplTest {


    @InjectMocks
    InstitutionConnectorImpl institutionConnectorImpl;

    @Mock
    InstitutionRepository institutionRepository;

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    @Captor
    ArgumentCaptor<Pageable> pageableArgumentCaptor;

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll() {
        when(institutionRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findAll().isEmpty());
        verify(institutionRepository).findAll();
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll2() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.findAll()).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findAll().size());
        verify(institutionRepository).findAll();
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll3() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(false);
        billingEntity1.setRecipientCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.BillingEntity");
        billingEntity1.setVatNumber("Vat Number");

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

        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setAddress("17 High St");
        institutionEntity1.setAttributes(new ArrayList<>());
        institutionEntity1.setBilling(billingEntity1);
        institutionEntity1.setBusinessRegisterPlace("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setCreatedAt(null);
        institutionEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionEntity1.setDescription("Description");
        institutionEntity1.setDigitalAddress("17 High St");
        institutionEntity1.setExternalId("External Id");
        institutionEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity1.setId("Id");
        institutionEntity1.setImported(false);
        institutionEntity1.setInstitutionType(InstitutionType.PG);
        institutionEntity1.setOnboarding(new ArrayList<>());
        institutionEntity1.setOrigin(Origin.IPA);
        institutionEntity1.setOriginId("Origin Id");
        institutionEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionEntity1.setRea("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setShareCapital("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setSupportEmail("john.smith@example.org");
        institutionEntity1.setSupportPhone("8605550118");
        institutionEntity1.setTaxCode("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setUpdatedAt(null);
        institutionEntity1.setZipCode("OX1 1PT");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity1);
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.findAll()).thenReturn(institutionEntityList);
        assertEquals(2, institutionConnectorImpl.findAll().size());
        verify(institutionRepository).findAll();
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll4() {
        when(institutionRepository.findAll()).thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class, () -> institutionConnectorImpl.findAll());
        verify(institutionRepository).findAll();
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll5() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        AttributesEntity attributesEntity = new AttributesEntity();
        attributesEntity.setCode("Code");
        attributesEntity.setDescription("The characteristics of someone or something");
        attributesEntity.setOrigin("Origin");

        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        attributesEntityList.add(attributesEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(attributesEntityList);
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.findAll()).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findAll().size());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll6() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());

        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.findAll()).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findAll().size());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAll()}
     */
    @Test
    void testFindAll7() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(true);
        billingEntity1.setRecipientCode("Recipient Code");
        billingEntity1.setVatNumber("42");

        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setBilling(billingEntity1);
        onboardingEntity.setClosedAt(null);
        onboardingEntity.setContract("Contract");
        onboardingEntity.setCreatedAt(null);
        onboardingEntity.setPricingPlan("Pricing Plan");
        onboardingEntity.setProductId("42");
        onboardingEntity.setStatus(RelationshipState.PENDING);
        onboardingEntity.setTokenId("42");
        onboardingEntity.setUpdatedAt(null);

        ArrayList<OnboardingEntity> onboardingEntityList = new ArrayList<>();
        onboardingEntityList.add(onboardingEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(onboardingEntityList);
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.findAll()).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findAll().size());
        verify(institutionRepository).findAll();
    }

    @Test
    void findById() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectorImpl.findByExternalId("id");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void save() {
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        geographicTaxonomies.add(new InstitutionGeographicTaxonomies());
        List<Onboarding> onboardings = new ArrayList<>();
        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(new Billing());
        onboarding.setContract("contract");
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setCreatedAt(OffsetDateTime.now());
        onboarding.setProductId("productId");
        onboarding.setUpdatedAt(OffsetDateTime.now());
        onboarding.setPricingPlan("pricingPal");
        onboardings.add(onboarding);
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        institution.setOnboarding(onboardings);
        institution.setGeographicTaxonomies(geographicTaxonomies);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        List<AttributesEntity> attributes = new ArrayList<>();
        AttributesEntity attributesEntity = new AttributesEntity();
        attributesEntity.setCode("code");
        attributesEntity.setOrigin("origin");
        attributesEntity.setDescription("description");
        attributes.add(attributesEntity);
        institutionEntity.setAttributes(attributes);
        institutionEntity.setOnboarding(new ArrayList<>());
        List<GeoTaxonomyEntity> geoTaxonomyEntities = new ArrayList<>();
        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("code");
        geoTaxonomyEntity.setDesc("desc");
        geoTaxonomyEntities.add(geoTaxonomyEntity);
        institutionEntity.setGeographicTaxonomies(geoTaxonomyEntities);
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectorImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

    @Test
    void save3() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setExternalId("ext");
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectorImpl.save(institution);
        Assertions.assertEquals("ext", response.getExternalId());
    }

    @Test
    void findByExternalIdTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        Optional<Institution> response = institutionConnectorImpl.findByExternalId("ext");
        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void findByExternalIdNotFoundTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectorImpl.findByExternalId("ext");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void deleteById() {
        doNothing().when(institutionRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> institutionConnectorImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void testFindById() {
        when(institutionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectorImpl.findById("42"));
    }

    @Test
    void testFindById2() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.findById(any())).thenReturn(Optional.of(institutionEntity));
        assertNotNull(institutionConnectorImpl.findById("id"));
    }

    @Test
    void testFindAndUpdate() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.findAndModify(any(), any(), any(), any())).thenReturn(institutionEntity);
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        InstitutionGeographicTaxonomies geographicTaxonomies1 = new InstitutionGeographicTaxonomies();
        geographicTaxonomies1.setCode("code");
        geographicTaxonomies1.setDesc("desc");
        geographicTaxonomies.add(geographicTaxonomies1);
        Institution response = institutionConnectorImpl.findAndUpdate("institutionId", new Onboarding(), geographicTaxonomies, new InstitutionUpdate());
        assertNotNull(response);
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAndUpdateInstitutionData(String, Token, Onboarding, RelationshipState)}
     */
    @Test
    void testFindAndUpdateInstitutionData() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.findAndModify(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(institutionEntity);

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
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

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        institutionConnectorImpl.findAndUpdateInstitutionData("42", token, onboarding, RelationshipState.PENDING);
        verify(institutionRepository).findAndModify(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAndUpdateInstitutionData(String, Token, Onboarding, RelationshipState)}
     */
    @Test
    void testFindAndUpdateInstitutionData2() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.findAndModify(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any())).thenReturn(institutionEntity);

        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress("42 Main St");
        dataProtectionOfficer.setEmail("jane.doe@example.org");
        dataProtectionOfficer.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode("Abi Code");
        paymentServiceProvider.setBusinessRegisterNumber("42");
        paymentServiceProvider.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider.setLegalRegisterNumber("42");
        paymentServiceProvider.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
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

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setClosedAt(null);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setTokenId("42");
        onboarding.setUpdatedAt(null);
        institutionConnectorImpl.findAndUpdateInstitutionData("42", token, onboarding, RelationshipState.DELETED);
        verify(institutionRepository).findAndModify(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies() {
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).isEmpty());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies2() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies3() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(false);
        billingEntity1.setRecipientCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.BillingEntity");
        billingEntity1.setVatNumber("Vat Number");

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

        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setAddress("17 High St");
        institutionEntity1.setAttributes(new ArrayList<>());
        institutionEntity1.setBilling(billingEntity1);
        institutionEntity1.setBusinessRegisterPlace("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setCreatedAt(null);
        institutionEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionEntity1.setDescription("Description");
        institutionEntity1.setDigitalAddress("17 High St");
        institutionEntity1.setExternalId("External Id");
        institutionEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity1.setId("Id");
        institutionEntity1.setImported(false);
        institutionEntity1.setInstitutionType(InstitutionType.PG);
        institutionEntity1.setOnboarding(new ArrayList<>());
        institutionEntity1.setOrigin(Origin.IPA);
        institutionEntity1.setOriginId("Origin Id");
        institutionEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionEntity1.setRea("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setShareCapital("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setSupportEmail("john.smith@example.org");
        institutionEntity1.setSupportPhone("8605550118");
        institutionEntity1.setTaxCode("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setUpdatedAt(null);
        institutionEntity1.setZipCode("OX1 1PT");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity1);
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(2, institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies4() {
        when(institutionRepository.find(org.mockito.Mockito.any(),org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ANY).isEmpty());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies5() {
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.EXACT).isEmpty());
        verify(institutionRepository).find(org.mockito.Mockito.any(),org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies6() {
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        List<String> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> institutionConnectorImpl.findByGeotaxonomies(list, SearchMode.ALL));
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies7() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        AttributesEntity attributesEntity = new AttributesEntity();
        attributesEntity.setCode("Code");
        attributesEntity.setDescription("The characteristics of someone or something");
        attributesEntity.setOrigin("Origin");

        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        attributesEntityList.add(attributesEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(attributesEntityList);
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies8() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).size());
        verify(institutionRepository).find(org.mockito.Mockito.any(),org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByGeotaxonomies(List, SearchMode)}
     */
    @Test
    void testFindByGeotaxonomies9() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(true);
        billingEntity1.setRecipientCode("Recipient Code");
        billingEntity1.setVatNumber("42");

        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setBilling(billingEntity1);
        onboardingEntity.setClosedAt(null);
        onboardingEntity.setContract("Contract");
        onboardingEntity.setCreatedAt(null);
        onboardingEntity.setPricingPlan("Pricing Plan");
        onboardingEntity.setProductId("42");
        onboardingEntity.setStatus(RelationshipState.PENDING);
        onboardingEntity.setTokenId("42");
        onboardingEntity.setUpdatedAt(null);

        ArrayList<OnboardingEntity> onboardingEntityList = new ArrayList<>();
        onboardingEntityList.add(onboardingEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(onboardingEntityList);
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByGeotaxonomies(new ArrayList<>(), SearchMode.ALL).size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId() {
        when(institutionRepository.find(org.mockito.Mockito.any(),org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findByProductId("42").isEmpty());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId2() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode(".");
        billingEntity.setVatNumber("42");

        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec(".");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode(".");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName(".");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace(".");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea(".");
        institutionEntity.setShareCapital(".");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode(".");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByProductId("42").size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId3() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode(".");
        billingEntity.setVatNumber("42");

        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec(".");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode(".");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName(".");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace(".");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea(".");
        institutionEntity.setShareCapital(".");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode(".");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(false);
        billingEntity1.setRecipientCode("Recipient Code");
        billingEntity1.setVatNumber(".");

        DataProtectionOfficerEntity dataProtectionOfficerEntity1 = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity1.setAddress("17 High St");
        dataProtectionOfficerEntity1.setEmail("john.smith@example.org");
        dataProtectionOfficerEntity1.setPec("Pec");

        PaymentServiceProviderEntity paymentServiceProviderEntity1 = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity1.setAbiCode("Abi Code");
        paymentServiceProviderEntity1.setBusinessRegisterNumber(".");
        paymentServiceProviderEntity1.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity1.setLegalRegisterNumber(".");
        paymentServiceProviderEntity1.setVatNumberGroup(false);

        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setAddress("17 High St");
        institutionEntity1.setAttributes(new ArrayList<>());
        institutionEntity1.setBilling(billingEntity1);
        institutionEntity1.setBusinessRegisterPlace("Business Register Place");
        institutionEntity1.setCreatedAt(null);
        institutionEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionEntity1.setDescription(".");
        institutionEntity1.setDigitalAddress("17 High St");
        institutionEntity1.setExternalId(".");
        institutionEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity1.setId(".");
        institutionEntity1.setImported(false);
        institutionEntity1.setInstitutionType(InstitutionType.PG);
        institutionEntity1.setOnboarding(new ArrayList<>());
        institutionEntity1.setOrigin(Origin.IPA);
        institutionEntity1.setOriginId(".");
        institutionEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionEntity1.setRea("Rea");
        institutionEntity1.setShareCapital("Share Capital");
        institutionEntity1.setSupportEmail("john.smith@example.org");
        institutionEntity1.setSupportPhone("8605550118");
        institutionEntity1.setTaxCode("Tax Code");
        institutionEntity1.setUpdatedAt(null);
        institutionEntity1.setZipCode("OX1 1PT");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity1);
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(2, institutionConnectorImpl.findByProductId("42").size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId4() {
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any()))
                .thenThrow(new InvalidRequestException("An error occurred", "."));
        assertThrows(InvalidRequestException.class, () -> institutionConnectorImpl.findByProductId("42"));
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId5() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode(".");
        billingEntity.setVatNumber("42");

        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec(".");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode(".");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName(".");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

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

        AttributesEntity attributesEntity = new AttributesEntity();
        attributesEntity.setCode("Code");
        attributesEntity.setDescription("The characteristics of someone or something");
        attributesEntity.setOrigin("Origin");

        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        attributesEntityList.add(attributesEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(attributesEntityList);
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace(".");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea(".");
        institutionEntity.setShareCapital(".");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode(".");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByProductId("42").size());
        verify(institutionRepository).find(org.mockito.Mockito.any(),org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId6() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode(".");
        billingEntity.setVatNumber("42");

        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec(".");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode(".");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName(".");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

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

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace(".");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea(".");
        institutionEntity.setShareCapital(".");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode(".");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByProductId("42").size());
        verify(institutionRepository).find(org.mockito.Mockito.any(), org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByProductId}
     */
    @Test
    void testFindByProductId7() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode(".");
        billingEntity.setVatNumber("42");

        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec(".");

        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode(".");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName(".");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);

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

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(true);
        billingEntity1.setRecipientCode("Recipient Code");
        billingEntity1.setVatNumber("42");

        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setBilling(billingEntity1);
        onboardingEntity.setClosedAt(null);
        onboardingEntity.setContract("Contract");
        onboardingEntity.setCreatedAt(null);
        onboardingEntity.setPricingPlan("Pricing Plan");
        onboardingEntity.setProductId("42");
        onboardingEntity.setStatus(RelationshipState.PENDING);
        onboardingEntity.setTokenId("42");
        onboardingEntity.setUpdatedAt(null);

        ArrayList<OnboardingEntity> onboardingEntityList = new ArrayList<>();
        onboardingEntityList.add(onboardingEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(onboardingEntityList);
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace(".");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea(".");
        institutionEntity.setShareCapital(".");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode(".");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        ArrayList<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(),
                (Class<InstitutionEntity>) org.mockito.Mockito.any())).thenReturn(institutionEntityList);
        assertEquals(1, institutionConnectorImpl.findByProductId("42").size());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findAllByIds(List)}
     */
    @Test
    void testFindAllByIds3() {
        when(institutionRepository.findAllById(org.mockito.Mockito.any()))
                .thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        List<String> ids = new ArrayList<>();
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectorImpl.findAllByIds(ids));
        verify(institutionRepository).findAllById(org.mockito.Mockito.any());
    }

    @Test
    void testFindAllByIds2() {
        when(institutionRepository.findAllById(org.mockito.Mockito.any())).thenReturn(List.of(new InstitutionEntity()));
        List<String> ids = new ArrayList<>();
        assertDoesNotThrow(() -> institutionConnectorImpl.findAllByIds(ids));
        verify(institutionRepository).findAllById(org.mockito.Mockito.any());
    }

    @Test
    void testFindWithFilter() {
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        List<Institution> response = institutionConnectorImpl.findWithFilter("externalId", "productId", new ArrayList<>());
        assertNotNull(response);
    }

    @Test
    void testFindInstitutionProduct() {
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectorImpl.findByExternalIdAndProductId("externalId", "productId"));
    }

    @Test
    void save2() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setDataProtectionOfficer(new DataProtectionOfficer());
        institution.setPaymentServiceProvider(new PaymentServiceProvider());

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectorImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#saveOrRetrievePnPg(Institution)}
     */
    @Test
    void testSaveOrRetrievePnPg() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        institutionEntity.setAttributes(attributesEntityList);
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.save(org.mockito.Mockito.any())).thenReturn(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        Institution actualSaveOrRetrievePnPgResult = institutionConnectorImpl.saveOrRetrievePnPg(new Institution());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getAddress());
        assertTrue(actualSaveOrRetrievePnPgResult.isImported());
        assertEquals("21654", actualSaveOrRetrievePnPgResult.getZipCode());
        assertNull(actualSaveOrRetrievePnPgResult.getUpdatedAt());
        assertEquals("Tax Code", actualSaveOrRetrievePnPgResult.getTaxCode());
        assertEquals("6625550144", actualSaveOrRetrievePnPgResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualSaveOrRetrievePnPgResult.getSupportEmail());
        assertEquals("Share Capital", actualSaveOrRetrievePnPgResult.getShareCapital());
        assertEquals("Rea", actualSaveOrRetrievePnPgResult.getRea());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getOriginId());
        assertEquals("Business Register Place", actualSaveOrRetrievePnPgResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getDigitalAddress());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getExternalId());
        assertNull(actualSaveOrRetrievePnPgResult.getCreatedAt());
        assertEquals(Origin.MOCK.getValue(), actualSaveOrRetrievePnPgResult.getOrigin());
        assertEquals(InstitutionType.PA, actualSaveOrRetrievePnPgResult.getInstitutionType());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getId());
        assertEquals("The characteristics of someone or something", actualSaveOrRetrievePnPgResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#saveOrRetrievePnPg(Institution)}
     */
    @Test
    void testSaveOrRetrievePnPg2() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        AttributesEntity attributesEntity = new AttributesEntity();
        attributesEntity.setCode("Code");
        attributesEntity.setDescription("The characteristics of someone or something");
        attributesEntity.setOrigin("Origin");

        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        attributesEntityList.add(attributesEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(attributesEntityList);
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        ArrayList<AttributesEntity> attributesEntityList1 = new ArrayList<>();
        institutionEntity.setAttributes(attributesEntityList1);
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.save(org.mockito.Mockito.any())).thenReturn(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        Institution actualSaveOrRetrievePnPgResult = institutionConnectorImpl.saveOrRetrievePnPg(new Institution());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getAddress());
        assertTrue(actualSaveOrRetrievePnPgResult.isImported());
        assertEquals("21654", actualSaveOrRetrievePnPgResult.getZipCode());
        assertNull(actualSaveOrRetrievePnPgResult.getUpdatedAt());
        assertEquals("Tax Code", actualSaveOrRetrievePnPgResult.getTaxCode());
        assertEquals("6625550144", actualSaveOrRetrievePnPgResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualSaveOrRetrievePnPgResult.getSupportEmail());
        assertEquals("Share Capital", actualSaveOrRetrievePnPgResult.getShareCapital());
        assertEquals("Rea", actualSaveOrRetrievePnPgResult.getRea());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getOriginId());
        List<Attributes> attributes = actualSaveOrRetrievePnPgResult.getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("Business Register Place", actualSaveOrRetrievePnPgResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getDigitalAddress());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getExternalId());
        assertNull(actualSaveOrRetrievePnPgResult.getCreatedAt());
        assertEquals(InstitutionType.PA, actualSaveOrRetrievePnPgResult.getInstitutionType());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getId());
        assertEquals("The characteristics of someone or something", actualSaveOrRetrievePnPgResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#saveOrRetrievePnPg(Institution)}
     */
    @Test
    void testSaveOrRetrievePnPg3() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        GeoTaxonomyEntity geoTaxonomyEntity = new GeoTaxonomyEntity();
        geoTaxonomyEntity.setCode("Code");
        geoTaxonomyEntity.setDesc("The characteristics of someone or something");

        ArrayList<GeoTaxonomyEntity> geoTaxonomyEntityList = new ArrayList<>();
        geoTaxonomyEntityList.add(geoTaxonomyEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(geoTaxonomyEntityList);
        when(institutionEntity.getOnboarding()).thenReturn(new ArrayList<>());
        institutionEntity.setAddress("42 Main St");
        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        institutionEntity.setAttributes(attributesEntityList);
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.save(org.mockito.Mockito.any())).thenReturn(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        Institution actualSaveOrRetrievePnPgResult = institutionConnectorImpl.saveOrRetrievePnPg(new Institution());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getAddress());
        assertTrue(actualSaveOrRetrievePnPgResult.isImported());
        assertEquals("21654", actualSaveOrRetrievePnPgResult.getZipCode());
        assertNull(actualSaveOrRetrievePnPgResult.getUpdatedAt());
        assertEquals("Tax Code", actualSaveOrRetrievePnPgResult.getTaxCode());
        assertEquals("6625550144", actualSaveOrRetrievePnPgResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualSaveOrRetrievePnPgResult.getSupportEmail());
        assertEquals("Share Capital", actualSaveOrRetrievePnPgResult.getShareCapital());
        assertEquals("Rea", actualSaveOrRetrievePnPgResult.getRea());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getOriginId());
        assertEquals("Business Register Place", actualSaveOrRetrievePnPgResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getDigitalAddress());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getExternalId());
        assertNull(actualSaveOrRetrievePnPgResult.getCreatedAt());
        assertEquals(InstitutionType.PA, actualSaveOrRetrievePnPgResult.getInstitutionType());
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = actualSaveOrRetrievePnPgResult
                .getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getId());
        assertEquals("The characteristics of someone or something", actualSaveOrRetrievePnPgResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#saveOrRetrievePnPg(Institution)}
     */
    @Test
    void testSaveOrRetrievePnPg4() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(true);
        billingEntity1.setRecipientCode("Recipient Code");
        billingEntity1.setVatNumber("42");

        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setBilling(billingEntity1);
        onboardingEntity.setClosedAt(null);
        onboardingEntity.setContract("Contract");
        onboardingEntity.setCreatedAt(null);
        onboardingEntity.setPricingPlan("Pricing Plan");
        onboardingEntity.setProductId("42");
        onboardingEntity.setStatus(RelationshipState.PENDING);
        onboardingEntity.setTokenId("42");
        onboardingEntity.setUpdatedAt(null);

        ArrayList<OnboardingEntity> onboardingEntityList = new ArrayList<>();
        onboardingEntityList.add(onboardingEntity);
        InstitutionEntity institutionEntity = mock(InstitutionEntity.class);
        when(institutionEntity.isImported()).thenReturn(true);
        when(institutionEntity.getDataProtectionOfficer()).thenReturn(dataProtectionOfficerEntity1);
        when(institutionEntity.getPaymentServiceProvider()).thenReturn(paymentServiceProviderEntity1);
        when(institutionEntity.getInstitutionType()).thenReturn(InstitutionType.PA);
        when(institutionEntity.getOrigin()).thenReturn(Origin.MOCK);
        when(institutionEntity.getAddress()).thenReturn("42 Main St");
        when(institutionEntity.getBusinessRegisterPlace()).thenReturn("Business Register Place");
        when(institutionEntity.getDescription()).thenReturn("The characteristics of someone or something");
        when(institutionEntity.getDigitalAddress()).thenReturn("42 Main St");
        when(institutionEntity.getExternalId()).thenReturn("42");
        when(institutionEntity.getId()).thenReturn("42");
        when(institutionEntity.getOriginId()).thenReturn("42");
        when(institutionEntity.getRea()).thenReturn("Rea");
        when(institutionEntity.getShareCapital()).thenReturn("Share Capital");
        when(institutionEntity.getSupportEmail()).thenReturn("jane.doe@example.org");
        when(institutionEntity.getSupportPhone()).thenReturn("6625550144");
        when(institutionEntity.getTaxCode()).thenReturn("Tax Code");
        when(institutionEntity.getZipCode()).thenReturn("21654");
        when(institutionEntity.getCreatedAt()).thenReturn(null);
        when(institutionEntity.getUpdatedAt()).thenReturn(null);
        when(institutionEntity.getAttributes()).thenReturn(new ArrayList<>());
        when(institutionEntity.getGeographicTaxonomies()).thenReturn(new ArrayList<>());
        when(institutionEntity.getOnboarding()).thenReturn(onboardingEntityList);
        institutionEntity.setAddress("42 Main St");
        ArrayList<AttributesEntity> attributesEntityList = new ArrayList<>();
        institutionEntity.setAttributes(attributesEntityList);
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(new ArrayList<>());
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");
        when(institutionRepository.save(org.mockito.Mockito.any())).thenReturn(institutionEntity);
        when(institutionRepository.find(org.mockito.Mockito.any(), org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        Institution actualSaveOrRetrievePnPgResult = institutionConnectorImpl.saveOrRetrievePnPg(new Institution());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getAddress());
        assertTrue(actualSaveOrRetrievePnPgResult.isImported());
        assertEquals("21654", actualSaveOrRetrievePnPgResult.getZipCode());
        assertNull(actualSaveOrRetrievePnPgResult.getUpdatedAt());
        assertEquals("Tax Code", actualSaveOrRetrievePnPgResult.getTaxCode());
        assertEquals("6625550144", actualSaveOrRetrievePnPgResult.getSupportPhone());
        assertEquals("jane.doe@example.org", actualSaveOrRetrievePnPgResult.getSupportEmail());
        assertEquals("Share Capital", actualSaveOrRetrievePnPgResult.getShareCapital());
        assertEquals("Rea", actualSaveOrRetrievePnPgResult.getRea());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getOriginId());
        assertEquals("Business Register Place", actualSaveOrRetrievePnPgResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualSaveOrRetrievePnPgResult.getDigitalAddress());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getExternalId());
        assertNull(actualSaveOrRetrievePnPgResult.getCreatedAt());
        assertEquals(Origin.MOCK.getValue(), actualSaveOrRetrievePnPgResult.getOrigin());
        assertEquals(1, actualSaveOrRetrievePnPgResult.getOnboarding().size());
        assertEquals(InstitutionType.PA, actualSaveOrRetrievePnPgResult.getInstitutionType());
        assertEquals("42", actualSaveOrRetrievePnPgResult.getId());
        assertEquals("The characteristics of someone or something", actualSaveOrRetrievePnPgResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByExternalIdsAndProductId(List, String)}
     */
    @Test
    void testFindByExternalIdAndProductId() {
        when(institutionRepository.find(org.mockito.Mockito.any(),org.mockito.Mockito.any())).thenReturn(new ArrayList<>());
        assertTrue(institutionConnectorImpl.findByExternalIdsAndProductId(new ArrayList<>(), "42").isEmpty());
        verify(institutionRepository).find( org.mockito.Mockito.any(),org.mockito.Mockito.any());
    }

    /**
     * Method under test: {@link InstitutionConnectorImpl#findByExternalIdsAndProductId(List, String)}
     */
    @Test
    void testFindByExternalIdAndProductId2() {
        when(institutionRepository.find( org.mockito.Mockito.any(),org.mockito.Mockito.any()))
                .thenThrow(new InvalidRequestException("An error occurred", "."));
        List<ValidInstitution> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> institutionConnectorImpl.findByExternalIdsAndProductId(list, "42"));
        verify(institutionRepository).find(org.mockito.Mockito.any(),org.mockito.Mockito.any());
    }

    @Test
    void findAndUpdateStatus() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.findAndModify(any(), any(), any(), any())).thenReturn(institutionEntity);
        institutionConnectorImpl.findAndUpdateStatus("institutionId", "tokenId", RelationshipState.ACTIVE);
        assertNotNull(institutionEntity);
    }

    @Test
    void findAndUpdateStatus2() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.findAndModify(any(), any(), any(), any())).thenReturn(institutionEntity);
        institutionConnectorImpl.findAndUpdateStatus("institutionId", "tokenId", RelationshipState.DELETED);
        assertNotNull(institutionEntity);
    }

    @Test
    void findAndRemoveOnboarding() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        List<OnboardingEntity> onboardings = new ArrayList<>();
        onboardings.add(new OnboardingEntity());
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setOnboarding(onboardings);
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProviderEntity());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficerEntity());
        when(institutionRepository.findAndModify(any(), any(), any(), any())).thenReturn(institutionEntity);
        institutionConnectorImpl.findAndRemoveOnboarding("institutionId", new Onboarding());
        assertNotNull(institutionEntity);
    }

    @Test
    void updateOnboardedProductCreatedAt() {
        // Given
        String institutionIdMock = "InstitutionIdMock";
        String productIdMock = "ProductIdMock";
        OffsetDateTime createdAt = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        InstitutionEntity updatedInstitutionMock = mockInstance(new InstitutionEntity());
        OnboardingEntity onboardingEntityMock = mockInstance(new OnboardingEntity());
        onboardingEntityMock.setProductId(productIdMock);
        updatedInstitutionMock.setOnboarding(List.of(onboardingEntityMock));
        updatedInstitutionMock.setId(institutionIdMock);
        updatedInstitutionMock.setInstitutionType(InstitutionType.PA);
        updatedInstitutionMock.setOrigin(Origin.IPA);
        when(institutionRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedInstitutionMock);
        // When
        Institution result = institutionConnectorImpl.updateOnboardedProductCreatedAt(institutionIdMock, productIdMock, createdAt);
        // Then
        assertNotNull(result);
        assertEquals(result.getId(), institutionIdMock);
        verify(institutionRepository, times(2))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(InstitutionEntity.class));
        List<Query> capturedQuery = queryArgumentCaptor.getAllValues();
        assertEquals(2, capturedQuery.size());
        assertSame(capturedQuery.get(0).getQueryObject().get(InstitutionEntity.Fields.id.name()), institutionIdMock);
        assertSame(capturedQuery.get(1).getQueryObject().get(InstitutionEntity.Fields.id.name()), institutionIdMock);
        assertEquals(2, updateArgumentCaptor.getAllValues().size());
        Update updateOnboardedProduct = updateArgumentCaptor.getAllValues().get(0);
        Update updateInstitutionEntityUpdatedAt = updateArgumentCaptor.getAllValues().get(1);
        assertEquals(1, updateOnboardedProduct.getArrayFilters().size());
        assertTrue(updateInstitutionEntityUpdatedAt.getArrayFilters().isEmpty());
        assertTrue(updateInstitutionEntityUpdatedAt.getUpdateObject().get("$set").toString().contains(InstitutionEntity.Fields.updatedAt.name()));
        assertTrue(updateOnboardedProduct.getUpdateObject().get("$set").toString().contains("onboarding.$[current].createdAt") &&
                updateOnboardedProduct.getUpdateObject().get("$set").toString().contains("onboarding.$[current].updatedAt") &&
                updateOnboardedProduct.getUpdateObject().get("$set").toString().contains(createdAt.toString()));
        verifyNoMoreInteractions(institutionRepository);
    }

    @Test
    void shouldFindOnboardingByIdAndProductId() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setOnboarding(List.of(new OnboardingEntity()));
        when(institutionRepository.findByInstitutionIdAndOnboardingProductId(anyString(), anyString()))
                .thenReturn(institutionEntity);

        List<Onboarding> onboardings = institutionConnectorImpl
                .findOnboardingByIdAndProductId("example", "example");

        assertFalse(onboardings.isEmpty());
    }

    @Test
    void shouldFindOnboardingByIdAndProductIdIfProductIsNull() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setOnboarding(List.of(new OnboardingEntity()));
        when(institutionRepository.findById(anyString()))
                .thenReturn(Optional.of(institutionEntity));

        List<Onboarding> onboardings = institutionConnectorImpl
                .findOnboardingByIdAndProductId("example", null);

        assertFalse(onboardings.isEmpty());
    }

    @Test
    void shouldFindByTaxCodeAndSubunitCode() {
        InstitutionEntity institutionEntity = new InstitutionEntity();

        when(institutionRepository.find(any(), any()))
                .thenReturn(List.of(institutionEntity));

        List<Institution> onboardings = institutionConnectorImpl
                .findByTaxCodeAndSubunitCode("example", "example");

        assertFalse(onboardings.isEmpty());
    }

    @Test
    void shouldExistsByTaxCodeAndSubunitCodeAndProductAndStatusList() {

        when(institutionRepository.exists(any(), any())).thenReturn(Boolean.TRUE);

        Boolean exists = institutionConnectorImpl.existsByTaxCodeAndSubunitCodeAndProductAndStatusList("example",
                Optional.of("example"), Optional.of("example"), List.of());

        assertTrue(exists);
    }

    @Test
    void testRetrieveByProduct() {
        String productId = "productId";
        Integer pageNumber = 0;
        Integer sizeNumber = 5;

        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");

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

        OnboardingEntity onboardingEntity = new OnboardingEntity();
        onboardingEntity.setBilling(billingEntity);
        onboardingEntity.setClosedAt(null);
        onboardingEntity.setContract("Contract");
        onboardingEntity.setCreatedAt(null);
        onboardingEntity.setPricingPlan("Pricing Plan");
        onboardingEntity.setProductId(productId);
        onboardingEntity.setStatus(RelationshipState.PENDING);
        onboardingEntity.setTokenId("42");
        onboardingEntity.setUpdatedAt(null);

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(billingEntity);
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(dataProtectionOfficerEntity);
        institutionEntity.setDescription("The characteristics of someone or something");
        institutionEntity.setDigitalAddress("42 Main St");
        institutionEntity.setExternalId("42");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setId("42");
        institutionEntity.setImported(true);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setOnboarding(List.of(onboardingEntity));
        institutionEntity.setOrigin(Origin.MOCK);
        institutionEntity.setOriginId("42");
        institutionEntity.setPaymentServiceProvider(paymentServiceProviderEntity);
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        BillingEntity billingEntity1 = new BillingEntity();
        billingEntity1.setPublicServices(false);
        billingEntity1.setRecipientCode("it.pagopa.selfcare.mscore.connector.dao.model.inner.BillingEntity");
        billingEntity1.setVatNumber("Vat Number");

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

        OnboardingEntity onboardingEntity1 = new OnboardingEntity();
        onboardingEntity1.setBilling(billingEntity);
        onboardingEntity1.setClosedAt(null);
        onboardingEntity1.setContract("Contract");
        onboardingEntity1.setCreatedAt(null);
        onboardingEntity1.setPricingPlan("Pricing Plan");
        onboardingEntity1.setProductId(productId);
        onboardingEntity1.setStatus(RelationshipState.PENDING);
        onboardingEntity1.setTokenId("42");
        onboardingEntity1.setUpdatedAt(null);

        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setAddress("17 High St");
        institutionEntity1.setAttributes(new ArrayList<>());
        institutionEntity1.setBilling(billingEntity1);
        institutionEntity1.setBusinessRegisterPlace("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setCreatedAt(null);
        institutionEntity1.setDataProtectionOfficer(dataProtectionOfficerEntity1);
        institutionEntity1.setDescription("Description");
        institutionEntity1.setDigitalAddress("17 High St");
        institutionEntity1.setExternalId("External Id");
        institutionEntity1.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity1.setId("Id");
        institutionEntity1.setImported(false);
        institutionEntity1.setInstitutionType(InstitutionType.PG);
        institutionEntity1.setOnboarding(List.of(onboardingEntity1));
        institutionEntity1.setOrigin(Origin.IPA);
        institutionEntity1.setOriginId("Origin Id");
        institutionEntity1.setPaymentServiceProvider(paymentServiceProviderEntity1);
        institutionEntity1.setRea("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setShareCapital("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setSupportEmail("john.smith@example.org");
        institutionEntity1.setSupportPhone("8605550118");
        institutionEntity1.setTaxCode("it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity");
        institutionEntity1.setUpdatedAt(null);
        institutionEntity1.setZipCode("OX1 1PT");

        List<InstitutionEntity> institutionEntityList = new ArrayList<>();
        institutionEntityList.add(institutionEntity1);
        institutionEntityList.add(institutionEntity);
        Page<InstitutionEntity> institutionEntityPage = new PageImpl<>(institutionEntityList);

        doReturn(institutionEntityPage)
                .when(institutionRepository)
                .find(any(), any(), any());

        // When
        List<Institution> institutionsResult = institutionConnectorImpl.findInstitutionsByProductId(productId,
                pageNumber, sizeNumber);
        // Then
        assertNotNull(institutionsResult);
        assertEquals(2, institutionsResult.size());
        verify(institutionRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(InstitutionEntity.class));

        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(institutionRepository);
    }
}
