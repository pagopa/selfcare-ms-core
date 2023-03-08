package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
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
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        geographicTaxonomies.add(new GeographicTaxonomies());
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
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        GeographicTaxonomies geographicTaxonomies1 = new GeographicTaxonomies();
        geographicTaxonomies1.setCode("code");
        geographicTaxonomies1.setDesc("desc");
        geographicTaxonomies.add(geographicTaxonomies1);
        Institution response = institutionConnectionImpl.findAndUpdate("institutionId", new Onboarding(), geographicTaxonomies);
        assertNotNull(response);
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
