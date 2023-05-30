package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.AttributesEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionMapperTest {
    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity() {
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(new Institution());
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity2() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        ArrayList<Attributes> attributesList = new ArrayList<>();
        institution.setAttributes(attributesList);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity3() {
        Attributes attributes = new Attributes();
        attributes.setCode("Code");
        attributes.setDescription("The characteristics of someone or something");
        attributes.setOrigin("Origin");

        ArrayList<Attributes> attributesList = new ArrayList<>();
        attributesList.add(attributes);

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        institution.setAttributes(attributesList);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        List<AttributesEntity> attributes1 = actualConvertToInstitutionEntityResult.getAttributes();
        assertEquals(1, attributes1.size());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
        AttributesEntity getResult = attributes1.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDescription());
        assertEquals("Origin", getResult.getOrigin());
        assertEquals("Code", getResult.getCode());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity4() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(null);
        institution
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institution.setOnboarding(null);
        institution.setAttributes(null);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
        PaymentServiceProviderEntity paymentServiceProvider = actualConvertToInstitutionEntityResult
                .getPaymentServiceProvider();
        assertEquals("Legal Register Name", paymentServiceProvider.getLegalRegisterName());
        assertEquals("42", paymentServiceProvider.getLegalRegisterNumber());
        assertEquals("Abi Code", paymentServiceProvider.getAbiCode());
        assertTrue(paymentServiceProvider.isVatNumberGroup());
        assertEquals("42", paymentServiceProvider.getBusinessRegisterNumber());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity5() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        institution.setAttributes(null);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
        DataProtectionOfficerEntity dataProtectionOfficer = actualConvertToInstitutionEntityResult
                .getDataProtectionOfficer();
        assertEquals("jane.doe@example.org", dataProtectionOfficer.getEmail());
        assertEquals("Pec", dataProtectionOfficer.getPec());
        assertEquals("42 Main St", dataProtectionOfficer.getAddress());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity6() {
        Institution institution = new Institution();
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        institution.setAttributes(null);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity7() {
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionGeographicTaxonomiesList
                .add(new InstitutionGeographicTaxonomies("Code", "The characteristics of someone or something"));

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        institution.setAttributes(null);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        List<GeoTaxonomyEntity> geographicTaxonomies = actualConvertToInstitutionEntityResult.getGeographicTaxonomies();
        assertEquals(1, geographicTaxonomies.size());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
        GeoTaxonomyEntity getResult = geographicTaxonomies.get(0);
        assertEquals("The characteristics of someone or something", getResult.getDesc());
        assertEquals("Code", getResult.getCode());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity8() {
        Institution institution = new Institution();
        institution.setGeographicTaxonomies(null);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        institution.setOnboarding(onboardingList);
        institution.setAttributes(null);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity10() {
        Billing billing = new Billing();
        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        ArrayList<InstitutionGeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(new Institution("42", "42", Origin.MOCK.getValue(), "42",
                        "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                        "Tax Code", null, null, billing, onboardingList, geographicTaxonomies, attributes, paymentServiceProvider,
                        new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"), "Rea", "Share Capital",
                        "Business Register Place", "jane.doe@example.org", "6625550144", true, null, null));
        assertEquals("42 Main St", actualConvertToInstitutionEntityResult.getAddress());
        assertTrue(actualConvertToInstitutionEntityResult.isImported());
        assertEquals("21654", actualConvertToInstitutionEntityResult.getZipCode());
        assertEquals("Tax Code", actualConvertToInstitutionEntityResult.getTaxCode());
        assertEquals("Business Register Place", actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertEquals("Rea", actualConvertToInstitutionEntityResult.getRea());
        assertEquals("42", actualConvertToInstitutionEntityResult.getExternalId());
        assertEquals("Share Capital", actualConvertToInstitutionEntityResult.getShareCapital());
        assertEquals("jane.doe@example.org", actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertEquals("6625550144", actualConvertToInstitutionEntityResult.getSupportPhone());
        assertEquals("42", actualConvertToInstitutionEntityResult.getId());
        assertEquals(InstitutionType.PA, actualConvertToInstitutionEntityResult.getInstitutionType());
        assertEquals("The characteristics of someone or something",
                actualConvertToInstitutionEntityResult.getDescription());
        assertEquals(Origin.MOCK, actualConvertToInstitutionEntityResult.getOrigin());
        assertEquals("42", actualConvertToInstitutionEntityResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity11() {
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

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Billing billing1 = new Billing();
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        ArrayList<Attributes> attributes = new ArrayList<>();
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42",
                "Legal Register Name", "42", true);

        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(new Institution("42", "42", Origin.MOCK.getValue(), "42",
                        "The characteristics of someone or something", InstitutionType.PA, "42 Main St", "42 Main St", "21654",
                        "Tax Code", null, null, billing1, onboardingList, institutionGeographicTaxonomiesList, attributes,
                        paymentServiceProvider, new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"), "Rea",
                        "Share Capital", "Business Register Place", "jane.doe@example.org", "6625550144", true, null, null));
        assertEquals("42 Main St", actualConvertToInstitutionEntityResult.getAddress());
        assertTrue(actualConvertToInstitutionEntityResult.isImported());
        assertEquals("21654", actualConvertToInstitutionEntityResult.getZipCode());
        assertEquals("Tax Code", actualConvertToInstitutionEntityResult.getTaxCode());
        assertEquals("Business Register Place", actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertEquals("42 Main St", actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertEquals("Rea", actualConvertToInstitutionEntityResult.getRea());
        assertEquals("42", actualConvertToInstitutionEntityResult.getExternalId());
        assertEquals("Share Capital", actualConvertToInstitutionEntityResult.getShareCapital());
        assertEquals("jane.doe@example.org", actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertEquals("6625550144", actualConvertToInstitutionEntityResult.getSupportPhone());
        assertEquals("42", actualConvertToInstitutionEntityResult.getId());
        assertEquals(InstitutionType.PA, actualConvertToInstitutionEntityResult.getInstitutionType());
        assertEquals("The characteristics of someone or something",
                actualConvertToInstitutionEntityResult.getDescription());
        assertEquals(Origin.MOCK, actualConvertToInstitutionEntityResult.getOrigin());
        assertEquals("42", actualConvertToInstitutionEntityResult.getOriginId());
    }

    /**
     * Method under test: {@link InstitutionMapper#convertToInstitutionEntity(Institution)}
     */
    @Test
    void testConvertToInstitutionEntity12() {
        List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();

        Institution institution = new Institution();
        institution.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institution.setDataProtectionOfficer(null);
        institution.setPaymentServiceProvider(null);
        institution.setOnboarding(null);
        ArrayList<Attributes> attributesList = new ArrayList<>();
        institution.setAttributes(attributesList);
        institution.setId(null);
        InstitutionEntity actualConvertToInstitutionEntityResult = InstitutionMapper
                .convertToInstitutionEntity(institution);
        assertNull(actualConvertToInstitutionEntityResult.getAddress());
        assertFalse(actualConvertToInstitutionEntityResult.isImported());
        assertNull(actualConvertToInstitutionEntityResult.getZipCode());
        assertNull(actualConvertToInstitutionEntityResult.getTaxCode());
        assertNull(actualConvertToInstitutionEntityResult.getBusinessRegisterPlace());
        assertNull(actualConvertToInstitutionEntityResult.getDigitalAddress());
        assertNull(actualConvertToInstitutionEntityResult.getRea());
        assertNull(actualConvertToInstitutionEntityResult.getExternalId());
        assertNull(actualConvertToInstitutionEntityResult.getShareCapital());
        assertNull(actualConvertToInstitutionEntityResult.getSupportEmail());
        assertNull(actualConvertToInstitutionEntityResult.getCreatedAt());
        assertNull(actualConvertToInstitutionEntityResult.getSupportPhone());
        assertNull(actualConvertToInstitutionEntityResult.getInstitutionType());
        assertNull(actualConvertToInstitutionEntityResult.getDescription());
        assertNull(actualConvertToInstitutionEntityResult.getOriginId());
    }

    @Test
    void testAddGeographicTaxonomies() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider("Abi Code", "42", "Legal Register Name",
                "42", true);

        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");
        Update update = new Update();
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertTrue(institutionUpdate.isImported());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals("6625550144", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertTrue(institutionUpdate.getGeographicTaxonomies().isEmpty());
        assertFalse(update.hasArrayFilters());
    }

    @Test
    void testAddGeographicTaxonomies2() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
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
        institutionUpdate.setGeographicTaxonomies(null);
        Update update = new Update();
        InstitutionMapper.addGeographicTaxonomies(institutionUpdate.getGeographicTaxonomies(), update);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertTrue(institutionUpdate.isImported());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals("6625550144", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertFalse(update.hasArrayFilters());
    }

    @Test
    void testAddGeographicTaxonomies7() {
        List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer("42 Main St", "jane.doe@example.org",
                "Pec");

        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
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
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        Update update = new Update();
        InstitutionMapper.addGeographicTaxonomies(institutionUpdate.getGeographicTaxonomies(), update);
        assertEquals("42 Main St", institutionUpdate.getAddress());
        assertTrue(institutionUpdate.isImported());
        assertEquals("21654", institutionUpdate.getZipCode());
        assertEquals("Tax Code", institutionUpdate.getTaxCode());
        assertEquals("6625550144", institutionUpdate.getSupportPhone());
        assertEquals("jane.doe@example.org", institutionUpdate.getSupportEmail());
        assertEquals("Share Capital", institutionUpdate.getShareCapital());
        assertEquals("Rea", institutionUpdate.getRea());
        assertSame(paymentServiceProvider, institutionUpdate.getPaymentServiceProvider());
        assertEquals(InstitutionType.PA, institutionUpdate.getInstitutionType());
        assertEquals("Business Register Place", institutionUpdate.getBusinessRegisterPlace());
        assertEquals("The characteristics of someone or something", institutionUpdate.getDescription());
        assertEquals("42 Main St", institutionUpdate.getDigitalAddress());
        assertSame(dataProtectionOfficer, institutionUpdate.getDataProtectionOfficer());
        assertTrue(institutionUpdate.getGeographicTaxonomies().isEmpty());
        assertFalse(update.hasArrayFilters());
    }
}

