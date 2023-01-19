package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Contract;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.Premium;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;

import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {OnboardingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class OnboardingServiceImplTest {
    @MockBean
    private GeoTaxonomiesConnector geoTaxonomiesConnector;

    @MockBean
    private InstitutionConnector institutionConnector;

    @Autowired
    private OnboardingServiceImpl onboardingServiceImpl;

    @MockBean
    private TokenConnector tokenConnector;

    @MockBean
    private UserConnector userConnector;

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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

        Institution institution = new Institution();
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   it.pagopa.selfcare.mscore.exception.ResourceNotFoundException: An error occurred
        //       at it.pagopa.selfcare.mscore.model.institution.Institution.getAddress(Institution.java:15)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.validateOverridingData(OnboardingServiceImpl.java:185)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:63)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress())
                .thenThrow(new ResourceNotFoundException("An error occurred", "Onboarding institution having externalId {}"));
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("foo");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("foo");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution5() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("foo");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution6() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("foo");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution7() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("foo");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution8() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.MissingFormatArgumentException: Format specifier '%s'
        //       at java.util.Formatter.format(Formatter.java:2672)
        //       at java.util.Formatter.format(Formatter.java:2609)
        //       at java.lang.String.format(String.java:2897)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:70)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing1);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    void testOnboardingInstitution9() {
        when(institutionConnector.findByExternalId((String) any())).thenReturn(Optional.empty());

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(new ArrayList<>());
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing1);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class,
                () -> onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class)));
        verify(institutionConnector).findByExternalId((String) any());
        verify(institution).setAddress((String) any());
        verify(institution).setAttributes((List<Attributes>) any());
        verify(institution).setBilling((Billing) any());
        verify(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        verify(institution).setDescription((String) any());
        verify(institution).setDigitalAddress((String) any());
        verify(institution).setExternalId((String) any());
        verify(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        verify(institution).setId((String) any());
        verify(institution).setInstitutionType((InstitutionType) any());
        verify(institution).setIpaCode((String) any());
        verify(institution).setOnboarding((List<Onboarding>) any());
        verify(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        verify(institution).setTaxCode((String) any());
        verify(institution).setZipCode((String) any());
    }

    /**
     * Method under test: {@link OnboardingServiceImpl#onboardingInstitution(OnboardingRequest, SelfCareUser)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardingInstitution10() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   it.pagopa.selfcare.mscore.exception.ResourceNotFoundException: An error occurred
        //       at it.pagopa.selfcare.mscore.model.institution.Onboarding.getStatus(Onboarding.java:17)
        //       at it.pagopa.selfcare.mscore.core.OnboardingServiceImpl.onboardingInstitution(OnboardingServiceImpl.java:60)
        //   See https://diff.blue/R013 to resolve this issue.

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

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

        Billing billing1 = new Billing();
        billing1.setPublicServices(true);
        billing1.setRecipientCode("Recipient Code");
        billing1.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);
        Onboarding onboarding = mock(Onboarding.class);
        when(onboarding.getStatus())
                .thenThrow(new ResourceNotFoundException("An error occurred", "Onboarding institution having externalId {}"));
        when(onboarding.getProductId()).thenReturn("42");
        doNothing().when(onboarding).setBilling((Billing) any());
        doNothing().when(onboarding).setContract((String) any());
        doNothing().when(onboarding).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboarding).setPremium((Premium) any());
        doNothing().when(onboarding).setPricingPlan((String) any());
        doNothing().when(onboarding).setProductId((String) any());
        doNothing().when(onboarding).setStatus((RelationshipState) any());
        doNothing().when(onboarding).setUpdatedAt((OffsetDateTime) any());
        onboarding.setBilling(billing1);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPremium(premium);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);

        ArrayList<Onboarding> onboardingList = new ArrayList<>();
        onboardingList.add(onboarding);
        Institution institution = mock(Institution.class);
        when(institution.getAddress()).thenReturn("42 Main St");
        when(institution.getZipCode()).thenReturn("21654");
        when(institution.getDigitalAddress()).thenReturn("42 Main St");
        when(institution.getTaxCode()).thenReturn("Tax Code");
        when(institution.getDescription()).thenReturn("The characteristics of someone or something");
        when(institution.getOnboarding()).thenReturn(onboardingList);
        doNothing().when(institution).setAddress((String) any());
        doNothing().when(institution).setAttributes((List<Attributes>) any());
        doNothing().when(institution).setBilling((Billing) any());
        doNothing().when(institution).setDataProtectionOfficer((DataProtectionOfficer) any());
        doNothing().when(institution).setDescription((String) any());
        doNothing().when(institution).setDigitalAddress((String) any());
        doNothing().when(institution).setExternalId((String) any());
        doNothing().when(institution).setGeographicTaxonomies((List<GeographicTaxonomies>) any());
        doNothing().when(institution).setId((String) any());
        doNothing().when(institution).setInstitutionType((InstitutionType) any());
        doNothing().when(institution).setIpaCode((String) any());
        doNothing().when(institution).setOnboarding((List<Onboarding>) any());
        doNothing().when(institution).setPaymentServiceProvider((PaymentServiceProvider) any());
        doNothing().when(institution).setTaxCode((String) any());
        doNothing().when(institution).setZipCode((String) any());
        institution.setAddress("42 Main St");
        institution.setAttributes(new ArrayList<>());
        institution.setBilling(billing);
        institution.setDataProtectionOfficer(dataProtectionOfficer);
        institution.setDescription("The characteristics of someone or something");
        institution.setDigitalAddress("42 Main St");
        institution.setExternalId("42");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setId("42");
        institution.setInstitutionType(InstitutionType.PA);
        institution.setIpaCode("Ipa Code");
        institution.setOnboarding(new ArrayList<>());
        institution.setPaymentServiceProvider(paymentServiceProvider);
        institution.setTaxCode("Tax Code");
        institution.setZipCode("21654");
        Optional<Institution> ofResult = Optional.of(institution);
        when(institutionConnector.findByExternalId((String) any())).thenReturn(ofResult);

        Billing billing2 = new Billing();
        billing2.setPublicServices(true);
        billing2.setRecipientCode("Recipient Code");
        billing2.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        DataProtectionOfficer dataProtectionOfficer1 = new DataProtectionOfficer();
        dataProtectionOfficer1.setAddress("42 Main St");
        dataProtectionOfficer1.setEmail("jane.doe@example.org");
        dataProtectionOfficer1.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider1 = new PaymentServiceProvider();
        paymentServiceProvider1.setAbiCode("Abi Code");
        paymentServiceProvider1.setBusinessRegisterNumber("42");
        paymentServiceProvider1.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider1.setLegalRegisterNumber("42");
        paymentServiceProvider1.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setDataProtectionOfficer(dataProtectionOfficer1);
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider1);
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        DataProtectionOfficer dataProtectionOfficer2 = new DataProtectionOfficer();
        dataProtectionOfficer2.setAddress("42 Main St");
        dataProtectionOfficer2.setEmail("jane.doe@example.org");
        dataProtectionOfficer2.setPec("Pec");

        PaymentServiceProvider paymentServiceProvider2 = new PaymentServiceProvider();
        paymentServiceProvider2.setAbiCode("Abi Code");
        paymentServiceProvider2.setBusinessRegisterNumber("42");
        paymentServiceProvider2.setLegalRegisterName("Legal Register Name");
        paymentServiceProvider2.setLegalRegisterNumber("42");
        paymentServiceProvider2.setVatNumberGroup(true);

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();
        institutionUpdate1.setAddress("42 Main St");
        institutionUpdate1.setDataProtectionOfficer(dataProtectionOfficer2);
        institutionUpdate1.setDescription("The characteristics of someone or something");
        institutionUpdate1.setDigitalAddress("42 Main St");
        institutionUpdate1.setGeographicTaxonomyCodes(new ArrayList<>());
        institutionUpdate1.setInstitutionType(InstitutionType.PA);
        institutionUpdate1.setPaymentServiceProvider(paymentServiceProvider2);
        institutionUpdate1.setTaxCode("Tax Code");
        institutionUpdate1.setZipCode("21654");
        OnboardingRequest onboardingRequest = mock(OnboardingRequest.class);
        when(onboardingRequest.getProductId()).thenReturn("42");
        when(onboardingRequest.getInstitutionUpdate()).thenReturn(institutionUpdate1);
        when(onboardingRequest.getInstitutionExternalId()).thenReturn("42");
        doNothing().when(onboardingRequest).setBillingRequest((Billing) any());
        doNothing().when(onboardingRequest).setContract((Contract) any());
        doNothing().when(onboardingRequest).setInstitutionExternalId((String) any());
        doNothing().when(onboardingRequest).setInstitutionUpdate((InstitutionUpdate) any());
        doNothing().when(onboardingRequest).setPricingPlan((String) any());
        doNothing().when(onboardingRequest).setProductId((String) any());
        doNothing().when(onboardingRequest).setProductName((String) any());
        doNothing().when(onboardingRequest).setUsers((List<OnboardedUser>) any());
        onboardingRequest.setBillingRequest(billing2);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingServiceImpl.onboardingInstitution(onboardingRequest, mock(SelfCareUser.class));
    }
}

