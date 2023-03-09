package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.BillingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InstitutionConnectorImplTest {

    @InjectMocks
    InstitutionConnectorImpl institutionConnectionImpl;

    @Mock
    InstitutionRepository institutionRepository;

    @Test
    void findById() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("id");
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
        Institution response = institutionConnectionImpl.save(institution);
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
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("ext", response.getExternalId());
    }

    @Test
    void findByExternalIdTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void findByExternalIdNotFoundTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void deleteById() {
        doNothing().when(institutionRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> institutionConnectionImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void testFindById() {
        when(institutionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectionImpl.findById("42"));
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
        assertNotNull(institutionConnectionImpl.findById("id"));
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
        Institution response = institutionConnectionImpl.findAndUpdate("institutionId", new Onboarding(), geographicTaxonomies);
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
        institutionConnectionImpl.findAndUpdateInstitutionData("42", token, onboarding, RelationshipState.PENDING);
        verify(institutionRepository).findAndModify(org.mockito.Mockito.any(),
                org.mockito.Mockito.any(), org.mockito.Mockito.any(),
                org.mockito.Mockito.any());
    }

    @Test
    void testFindWithFilter() {
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        List<Institution> response = institutionConnectionImpl.findWithFilter("externalId", "productId", new ArrayList<>());
        assertNotNull(response);
    }

    @Test
    void testFindInstitutionProduct() {
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectionImpl.findInstitutionProduct("externalId", "productId"));
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
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
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
        institutionConnectionImpl.findAndUpdateStatus("institutionId", "tokenId", RelationshipState.ACTIVE);
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
        institutionConnectionImpl.findAndRemoveOnboarding("institutionId", new Onboarding());
        assertNotNull(institutionEntity);
    }
}
