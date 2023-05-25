package it.pagopa.selfcare.mscore.connector.dao.mockUtils;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.BillingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;

import java.util.ArrayList;

public class MockUtils {

    public static ConfigEntity createConfig() {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId("KafkaScheduler");
        configEntity.setProductFilter("");
        configEntity.setEnableKafkaScheduler(true);
        return configEntity;
    }

    public static InstitutionEntity createInstitutionEntity() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setAddress("42 Main St");
        institutionEntity.setAttributes(new ArrayList<>());
        institutionEntity.setBilling(createBillingEntity());
        institutionEntity.setBusinessRegisterPlace("Business Register Place");
        institutionEntity.setCreatedAt(null);
        institutionEntity.setDataProtectionOfficer(createDataProtectionOfficerEntity());
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
        institutionEntity.setPaymentServiceProvider(createPaymentServiceProviderEntity());
        institutionEntity.setRea("Rea");
        institutionEntity.setShareCapital("Share Capital");
        institutionEntity.setSupportEmail("jane.doe@example.org");
        institutionEntity.setSupportPhone("6625550144");
        institutionEntity.setTaxCode("Tax Code");
        institutionEntity.setUpdatedAt(null);
        institutionEntity.setZipCode("21654");

        return institutionEntity;
    }

    public static DataProtectionOfficerEntity createDataProtectionOfficerEntity() {
        DataProtectionOfficerEntity dataProtectionOfficerEntity = new DataProtectionOfficerEntity();
        dataProtectionOfficerEntity.setAddress("42 Main St");
        dataProtectionOfficerEntity.setEmail("jane.doe@example.org");
        dataProtectionOfficerEntity.setPec("Pec");
        return dataProtectionOfficerEntity;
    }

    public static BillingEntity createBillingEntity() {
        BillingEntity billingEntity = new BillingEntity();
        billingEntity.setPublicServices(true);
        billingEntity.setRecipientCode("Recipient Code");
        billingEntity.setVatNumber("42");
        return billingEntity;
    }

    public static PaymentServiceProviderEntity createPaymentServiceProviderEntity() {
        PaymentServiceProviderEntity paymentServiceProviderEntity = new PaymentServiceProviderEntity();
        paymentServiceProviderEntity.setAbiCode("Abi Code");
        paymentServiceProviderEntity.setBusinessRegisterNumber("42");
        paymentServiceProviderEntity.setLegalRegisterName("Legal Register Name");
        paymentServiceProviderEntity.setLegalRegisterNumber("42");
        paymentServiceProviderEntity.setVatNumberGroup(true);
        return paymentServiceProviderEntity;
    }
}
