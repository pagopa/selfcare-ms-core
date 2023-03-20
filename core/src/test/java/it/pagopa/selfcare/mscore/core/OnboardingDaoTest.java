package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.Contract;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingOperatorsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.constant.InstitutionType;

import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OnboardingDaoTest {

    @MockBean
    private ProductConnector productConnector;

    @Mock
    private InstitutionConnector institutionConnector;
    @Mock
    private TokenConnector tokenConnector;
    @Mock
    private UserConnector userConnector;
    @Mock
    private CoreConfig coreConfig;

    @InjectMocks
    private OnboardingDao onboardingDao;

    @Test
    void testPersist0() {
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenReturn(new Institution());
        when(coreConfig.getOnboardingExpiringDate()).thenReturn(60);
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        UserToOnboard user = new UserToOnboard();
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(null);
        Institution institution = new Institution();
        Assertions.assertThrows(InvalidRequestException.class, () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution,
                new ArrayList<>(), "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist() {
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenReturn(new Institution());
        Token token = new Token();
        token.setId("tokenId");
        when(tokenConnector.save(any(), any())).thenReturn(token);
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        UserToOnboard user = new UserToOnboard();
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        Institution institution = new Institution();
        OnboardingRollback actualPersistResult = onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution,
                new ArrayList<>(), "Digest");
        Onboarding onboarding = actualPersistResult.getOnboarding();
        assertEquals(RelationshipState.PENDING, onboarding.getStatus());
        assertEquals("42", onboarding.getProductId());
        assertEquals("Pricing Plan", onboarding.getPricingPlan());
        assertEquals("Path", onboarding.getContract());
        assertSame(billing, onboarding.getBilling());
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(tokenConnector).save(any(), any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist2() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist3() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"))
                .when(tokenConnector)
                .deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersist4() {
        // TODO: Complete this test.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        OnboardingRequest onboardingRequest = new OnboardingRequest();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        onboardingRequest.setBillingRequest(billing);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");

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
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, new ArrayList<>(), "Digest");
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist6() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setInstitutionType(InstitutionType.PG);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> geo = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, geo, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persist(List, List, OnboardingRequest, Institution, List, String)}
     */
    @Test
    void testPersist7() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        when(institutionConnector.findAndUpdate(any(), any(), any()))
                .thenThrow(new InvalidRequestException("An error occurred", "createToken for institution {} and product {}"));
        doNothing().when(tokenConnector).deleteById(any());
        when(tokenConnector.save(any(), any())).thenReturn(new Token());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");

        ContractImported contractImported = new ContractImported();
        contractImported.setContractType("Contract Type");
        contractImported.setFileName("foo.txt");
        contractImported.setFilePath("/directory/foo.txt");

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
        institutionUpdate.setInstitutionType(InstitutionType.GSP);
        institutionUpdate.setPaymentServiceProvider(paymentServiceProvider);
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("4105551212");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setBillingRequest(billing);
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setUsers(new ArrayList<>());
        Institution institution = new Institution();
        List<InstitutionGeographicTaxonomies> list = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persist(toUpdate, toDelete, onboardingRequest, institution, list, "Digest"));
        verify(institutionConnector).findAndUpdate(any(), any(),
                any());
        verify(institutionConnector).findAndRemoveOnboarding(any(), any());
        verify(tokenConnector).save(any(), any());
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link OnboardingDao#persistLegals(List, List, OnboardingRequest, Institution, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistLegals() {
        // TODO: Complete this test.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        OnboardingRequest onboardingRequest = new OnboardingRequest();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");
        onboardingRequest.setBillingRequest(billing);

        Contract contract = new Contract();
        contract.setPath("Path");
        contract.setVersion("1.0.2");
        onboardingRequest.setContract(contract);
        onboardingRequest.setInstitutionExternalId("42");

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
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setPricingPlan("Pricing Plan");
        onboardingRequest.setProductId("42");
        onboardingRequest.setProductName("Product Name");
        onboardingRequest.setSignContract(true);
        onboardingRequest.setTokenType(TokenType.INSTITUTION);
        onboardingRequest.setUsers(new ArrayList<>());
        onboardingDao.persistLegals(toUpdate, toDelete, onboardingRequest, new Institution(), "Digest");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.PENDING, "Digest"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.TOBEVALIDATED);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "foo"));
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.TOBEVALIDATED);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.PENDING, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.TOBEVALIDATED);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.REJECTED, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.TOBEVALIDATED);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.DELETED, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.PENDING);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.ACTIVE, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.PENDING);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.REJECTED, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testPersistForUpdate8() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateToken(OnboardingDao.java:168)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.persistForUpdate(OnboardingDao.java:78)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setStatus(RelationshipState.PENDING);
        onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.DELETED, "foo");
    }

    /**
     * Method under test: {@link OnboardingDao#persistForUpdate(Token, Institution, RelationshipState, String)}
     */
    @Test
    void testPersistForUpdate9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

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
        Token token = mock(Token.class);
        when(token.getStatus()).thenReturn(RelationshipState.ACTIVE);
        doNothing().when(token).setChecksum((String) any());
        doNothing().when(token).setClosedAt((OffsetDateTime) any());
        doNothing().when(token).setContractSigned((String) any());
        doNothing().when(token).setContractTemplate((String) any());
        doNothing().when(token).setCreatedAt((OffsetDateTime) any());
        doNothing().when(token).setExpiringDate((OffsetDateTime) any());
        doNothing().when(token).setId((String) any());
        doNothing().when(token).setInstitutionId((String) any());
        doNothing().when(token).setInstitutionUpdate((InstitutionUpdate) any());
        doNothing().when(token).setProductId((String) any());
        doNothing().when(token).setStatus((RelationshipState) any());
        doNothing().when(token).setType((TokenType) any());
        doNothing().when(token).setUpdatedAt((OffsetDateTime) any());
        doNothing().when(token).setUsers((List<TokenUser>) any());
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
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.persistForUpdate(token, new Institution(), RelationshipState.PENDING, "Digest"));
        verify(token, atLeast(1)).getStatus();
        verify(token).setChecksum((String) any());
        verify(token).setClosedAt((OffsetDateTime) any());
        verify(token).setContractSigned((String) any());
        verify(token).setContractTemplate((String) any());
        verify(token).setCreatedAt((OffsetDateTime) any());
        verify(token).setExpiringDate((OffsetDateTime) any());
        verify(token).setId((String) any());
        verify(token).setInstitutionId((String) any());
        verify(token).setInstitutionUpdate((InstitutionUpdate) any());
        verify(token).setProductId((String) any());
        verify(token).setStatus((RelationshipState) any());
        verify(token).setType((TokenType) any());
        verify(token).setUpdatedAt((OffsetDateTime) any());
        verify(token).setUsers((List<TokenUser>) any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUsersState(Institution, Token, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUsersState() {
        // TODO: Complete this test.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        Institution institution = new Institution();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");

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
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        onboardingDao.updateUsersState(institution, token, RelationshipState.PENDING);
    }

    @Test
    void testRollbackSecondStep() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Map<String, OnboardedProduct> map = new HashMap<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", new Token(), onboarding, map));
    }

    @Test
    void testRollbackSecondStep2() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doThrow(new InvalidRequestException("An error occurred", "rollback second step completed")).when(tokenConnector)
                .deleteById(any());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Map<String, OnboardedProduct> map = new HashMap<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", new Token(), onboarding, map));
    }

    @Test
    void testRollbackSecondStep3() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Map<String, OnboardedProduct> map = new HashMap<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", new Token(), onboarding, map));
    }

    @Test
    void testRollbackSecondStep4() {
        doNothing().when(institutionConnector).findAndRemoveOnboarding(any(), any());
        doNothing().when(tokenConnector).deleteById(any());
        doNothing().when(userConnector).findAndRemoveProduct(any(), any(), any());

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("rollback second step completed");
        stringList.add("rollback second step completed");
        ArrayList<String> toDelete = new ArrayList<>();

        Billing billing = new Billing();
        billing.setPublicServices(true);
        billing.setRecipientCode("Recipient Code");
        billing.setVatNumber("42");

        Premium premium = new Premium();
        premium.setContract("Contract");
        premium.setStatus(RelationshipState.PENDING);

        Onboarding onboarding = new Onboarding();
        onboarding.setBilling(billing);
        onboarding.setContract("Contract");
        onboarding.setCreatedAt(null);
        onboarding.setPricingPlan("Pricing Plan");
        onboarding.setProductId("42");
        onboarding.setStatus(RelationshipState.PENDING);
        onboarding.setUpdatedAt(null);
        Map<String, OnboardedProduct> map = new HashMap<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(stringList, toDelete, "42", new Token(), onboarding, map));
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:274)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, "42", token, onboarding, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:279)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, null, null, null, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:279)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        token.setId(null);
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, null, token, null, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep8() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:279)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, "foo", null, null, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:277)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, "foo", null, onboarding, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    void testRollbackSecondStep10() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        UserConnector userConnector = mock(UserConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.rollbackSecondStep(toUpdate, toDelete, null, null, null, new HashMap<>()));
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep11() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   it.pagopa.selfcare.mscore.exception.InvalidRequestException: An error occurred
        //       at it.pagopa.selfcare.mscore.model.onboarding.Token.getId(Token.java:15)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:273)
        //   See https://diff.blue/R013 to resolve this issue.

        UserConnector userConnector = mock(UserConnector.class);
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();
        Token token = mock(Token.class);
        when(token.getId()).thenThrow(new InvalidRequestException("An error occurred", "Code"));
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, null, token, null, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep12() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:279)
        //   See https://diff.blue/R013 to resolve this issue.

        TokenConnector tokenConnector = mock(TokenConnector.class);
        doNothing().when(tokenConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, tokenConnector, null, productConnector, new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, null, token, null, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStep(List, List, String, Token, Onboarding, Map)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStep13() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackSecondStep(OnboardingDao.java:279)
        //   See https://diff.blue/R013 to resolve this issue.

        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        doNothing().when(institutionConnector).findAndRemoveOnboarding((String) any(), (Onboarding) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(institutionConnector, null, null, productConnector,
                new CoreConfig());
        ArrayList<String> toUpdate = new ArrayList<>();
        ArrayList<String> toDelete = new ArrayList<>();

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
        onboardingDao.rollbackSecondStep(toUpdate, toDelete, "foo", null, onboarding, new HashMap<>());
    }

    /**
     * Method under test: {@link OnboardingDao#rollbackSecondStepOfUpdate(List, Institution, Token)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testRollbackSecondStepOfUpdate() {
        // TODO: Complete this test.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ArrayList<String> toUpdate = new ArrayList<>();
        Institution institution = new Institution();

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");

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
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        onboardingDao.rollbackSecondStepOfUpdate(toUpdate, institution, token);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:151)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        onboardingDao.updateUserProductState(new OnboardedUser(), "42", RelationshipState.PENDING);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState2() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:152)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(new UserBinding());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState5() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.ACTIVE);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState6() {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        assertNotNull(onboardedUser);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:151)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        (new OnboardingDao(null, null, null, productConnector, new CoreConfig())).updateUserProductState(null, "42",
                RelationshipState.PENDING);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState8() {
        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.ACTIVE);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState10() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState11() {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class, () -> onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.PENDING));
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState12() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.DELETED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState13() {
        doNothing().when(userConnector)
                .findAndUpdateState(any(), any(), any(), any());

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);

        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.SUSPENDED);
        verify(userConnector).findAndUpdateState(any(), any(), any(), any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState14() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(new ArrayList<>());
        onboardingDao.updateUserProductState(onboardedUser, "42", RelationshipState.TOBEVALIDATED);
        assertTrue(onboardedUser.getBindings().isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState15() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(new ArrayList<>());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState16() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:152)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(null);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState17() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState18() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        OnboardedProduct onboardedProduct1 = new OnboardedProduct();
        onboardedProduct1.setContract("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setCreatedAt(null);
        onboardedProduct1.setEnv(Env.DEV);
        onboardedProduct1.setProductId("Product Id");
        onboardedProduct1.setProductRole("it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct");
        onboardedProduct1.setRelationshipId("Relationship Id");
        onboardedProduct1.setRole(PartyRole.DELEGATE);
        onboardedProduct1.setStatus(RelationshipState.ACTIVE);
        onboardedProduct1.setTokenId("ABC123");
        onboardedProduct1.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct1);
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState19() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getRelationshipId()).thenReturn("42");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING);
        verify(onboardedProduct).getRelationshipId();
        verify(onboardedProduct).setContract((String) any());
        verify(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        verify(onboardedProduct).setEnv((Env) any());
        verify(onboardedProduct).setProductId((String) any());
        verify(onboardedProduct).setProductRole((String) any());
        verify(onboardedProduct).setRelationshipId((String) any());
        verify(onboardedProduct).setRole((PartyRole) any());
        verify(onboardedProduct).setStatus((RelationshipState) any());
        verify(onboardedProduct).setTokenId((String) any());
        verify(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        assertEquals(1, onboardedUser.getBindings().size());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState20() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.PENDING));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract((String) any());
        verify(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        verify(onboardedProduct).setEnv((Env) any());
        verify(onboardedProduct).setProductId((String) any());
        verify(onboardedProduct).setProductRole((String) any());
        verify(onboardedProduct).setRelationshipId((String) any());
        verify(onboardedProduct).setRole((PartyRole) any());
        verify(onboardedProduct).setStatus((RelationshipState) any());
        verify(onboardedProduct).setTokenId((String) any());
        verify(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState21() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.ACTIVE));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract((String) any());
        verify(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        verify(onboardedProduct).setEnv((Env) any());
        verify(onboardedProduct).setProductId((String) any());
        verify(onboardedProduct).setProductRole((String) any());
        verify(onboardedProduct).setRelationshipId((String) any());
        verify(onboardedProduct).setRole((PartyRole) any());
        verify(onboardedProduct).setStatus((RelationshipState) any());
        verify(onboardedProduct).setTokenId((String) any());
        verify(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState22() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.SUSPENDED));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract((String) any());
        verify(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        verify(onboardedProduct).setEnv((Env) any());
        verify(onboardedProduct).setProductId((String) any());
        verify(onboardedProduct).setProductRole((String) any());
        verify(onboardedProduct).setRelationshipId((String) any());
        verify(onboardedProduct).setRole((PartyRole) any());
        verify(onboardedProduct).setStatus((RelationshipState) any());
        verify(onboardedProduct).setTokenId((String) any());
        verify(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState23() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.checkStatus(OnboardingDao.java:251)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.lambda$updateUserProductState$2(OnboardingDao.java:154)
        //       at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        //       at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177)
        //       at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
        //       at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:154)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.DELETED);
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    void testUpdateUserProductState24() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.PENDING);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.REJECTED));
        verify(onboardedProduct).getStatus();
        verify(onboardedProduct, atLeast(1)).getRelationshipId();
        verify(onboardedProduct).setContract((String) any());
        verify(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        verify(onboardedProduct).setEnv((Env) any());
        verify(onboardedProduct).setProductId((String) any());
        verify(onboardedProduct).setProductRole((String) any());
        verify(onboardedProduct).setRelationshipId((String) any());
        verify(onboardedProduct).setRole((PartyRole) any());
        verify(onboardedProduct).setStatus((RelationshipState) any());
        verify(onboardedProduct).setTokenId((String) any());
        verify(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
    }

    /**
     * Method under test: {@link OnboardingDao#updateUserProductState(OnboardedUser, String, RelationshipState)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testUpdateUserProductState25() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.checkStatus(OnboardingDao.java:251)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.lambda$updateUserProductState$2(OnboardingDao.java:154)
        //       at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        //       at java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:177)
        //       at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1654)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:150)
        //       at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:173)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.updateUserProductState(OnboardingDao.java:154)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());
        OnboardedProduct onboardedProduct = mock(OnboardedProduct.class);
        when(onboardedProduct.getStatus()).thenReturn(RelationshipState.SUSPENDED);
        when(onboardedProduct.getRelationshipId()).thenReturn("foo");
        doNothing().when(onboardedProduct).setContract((String) any());
        doNothing().when(onboardedProduct).setCreatedAt((OffsetDateTime) any());
        doNothing().when(onboardedProduct).setEnv((Env) any());
        doNothing().when(onboardedProduct).setProductId((String) any());
        doNothing().when(onboardedProduct).setProductRole((String) any());
        doNothing().when(onboardedProduct).setRelationshipId((String) any());
        doNothing().when(onboardedProduct).setRole((PartyRole) any());
        doNothing().when(onboardedProduct).setStatus((RelationshipState) any());
        doNothing().when(onboardedProduct).setTokenId((String) any());
        doNothing().when(onboardedProduct).setUpdatedAt((OffsetDateTime) any());
        onboardedProduct.setContract("Contract");
        onboardedProduct.setCreatedAt(null);
        onboardedProduct.setEnv(Env.ROOT);
        onboardedProduct.setProductId("42");
        onboardedProduct.setProductRole("Product Role");
        onboardedProduct.setRelationshipId("42");
        onboardedProduct.setRole(PartyRole.MANAGER);
        onboardedProduct.setStatus(RelationshipState.PENDING);
        onboardedProduct.setTokenId("42");
        onboardedProduct.setUpdatedAt(null);

        ArrayList<OnboardedProduct> onboardedProductList = new ArrayList<>();
        onboardedProductList.add(onboardedProduct);

        UserBinding userBinding = new UserBinding();
        userBinding.setProducts(onboardedProductList);

        ArrayList<UserBinding> userBindingList = new ArrayList<>();
        userBindingList.add(userBinding);

        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setBindings(userBindingList);
        onboardingDao.updateUserProductState(onboardedUser, "foo", RelationshipState.ACTIVE);
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator() {
        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingOperatorsRequest.setUsers(users);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }


    @Test
    void testOnboardOperator1() {
        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        UserToOnboard user = new UserToOnboard();
        user.setId("id");
        List<UserToOnboard> users = new ArrayList<>();
        users.add(user);
        onboardingOperatorsRequest.setUsers(users);
        when(userConnector.findById(any())).thenReturn(new OnboardedUser());
        assertFalse(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(new ArrayList<>());
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardOperator3() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.onboardOperator(OnboardingDao.java:214)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("users to update: {}");
        userToOnboard.setProductRole("users to update: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("users to update: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testOnboardOperator4() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at java.util.Objects.requireNonNull(Objects.java:221)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.rollbackUser(OnboardingDao.java:299)
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.onboardOperator(OnboardingDao.java:214)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, null, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("jane.doe@example.org");
        userToOnboard.setEnv(Env.ROOT);
        userToOnboard.setId("42");
        userToOnboard.setName("users to update: {}");
        userToOnboard.setProductRole("users to update: {}");
        userToOnboard.setRole(PartyRole.MANAGER);
        userToOnboard.setSurname("Doe");
        userToOnboard.setTaxCode("users to update: {}");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("john.smith@example.org");
        userToOnboard1.setEnv(Env.DEV);
        userToOnboard1.setId("can not onboard operators");
        userToOnboard1.setName("Name");
        userToOnboard1.setProductRole("Product Role");
        userToOnboard1.setRole(PartyRole.DELEGATE);
        userToOnboard1.setSurname("can not onboard operators");
        userToOnboard1.setTaxCode("Tax Code");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator5() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
        assertEquals(1, actualOnboardOperatorResult.size());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(0);
        assertSame(institution, getResult.getInstitution());
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        OnboardedProduct onboardedProduct = getResult.getOnboardedProduct();
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.SUB_DELEGATE, onboardedProduct.getRole());
        assertEquals("42", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.COLL, onboardedProduct.getEnv());
        verify(userConnector).findById((String) any());
        verify(userConnector).findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(),
                (OnboardedProduct) any(), (UserBinding) any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator6() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        UserConnector userConnector = mock(UserConnector.class);
        doNothing().when(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findById((String) any());
        verify(userConnector).deleteById((String) any());
        verify(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        verify(userConnector).findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(),
                (OnboardedProduct) any(), (UserBinding) any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator7() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        UserConnector userConnector = mock(UserConnector.class);
        doThrow(new InvalidRequestException("An error occurred", "can not onboard operators")).when(userConnector)
                .findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertThrows(InvalidRequestException.class,
                () -> onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()));
        verify(userConnector).findById((String) any());
        verify(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        verify(userConnector).findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(),
                (OnboardedProduct) any(), (UserBinding) any());
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator8() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate((String) any(), (UserBinding) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
        assertEquals(1, actualOnboardOperatorResult.size());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(0);
        assertSame(institution, getResult.getInstitution());
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        OnboardedProduct onboardedProduct = getResult.getOnboardedProduct();
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.SUB_DELEGATE, onboardedProduct.getRole());
        assertEquals("42", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.COLL, onboardedProduct.getEnv());
        verify(userConnector).findAndCreate((String) any(), (UserBinding) any());
        verify(userConnector).findById((String) any());
        verify(onboardedUser).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator9() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate((String) any(), (UserBinding) any()))
                .thenThrow(new InvalidRequestException("An error occurred", "users to update: {}"));
        doNothing().when(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findAndCreate((String) any(), (UserBinding) any());
        verify(userConnector).findById((String) any());
        verify(userConnector).deleteById((String) any());
        verify(onboardedUser).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator10() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenThrow(new ResourceNotFoundException("An error occurred", "Code"));
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate((String) any(), (UserBinding) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());

        UserToOnboard userToOnboard = new UserToOnboard();
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        UserToOnboard userToOnboard1 = new UserToOnboard();
        userToOnboard1.setEmail("jane.doe@example.org");
        userToOnboard1.setEnv(Env.ROOT);
        userToOnboard1.setId("42");
        userToOnboard1.setName("users to update: {}");
        userToOnboard1.setProductRole("users to update: {}");
        userToOnboard1.setRole(PartyRole.MANAGER);
        userToOnboard1.setSurname("Doe");
        userToOnboard1.setTaxCode("users to update: {}");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard1);
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        Institution institution = new Institution();
        List<RelationshipInfo> actualOnboardOperatorResult = onboardingDao.onboardOperator(onboardingOperatorsRequest,
                institution);
        assertEquals(2, actualOnboardOperatorResult.size());
        assertEquals("42", actualOnboardOperatorResult.get(0).getUserId());
        RelationshipInfo getResult = actualOnboardOperatorResult.get(1);
        assertEquals("it.pagopa.selfcare.mscore.model.user.UserToOnboard", getResult.getUserId());
        assertSame(institution, getResult.getInstitution());
        OnboardedProduct onboardedProduct = getResult.getOnboardedProduct();
        assertEquals(RelationshipState.ACTIVE, onboardedProduct.getStatus());
        assertEquals(PartyRole.SUB_DELEGATE, onboardedProduct.getRole());
        assertEquals("42", onboardedProduct.getProductRole());
        assertEquals("42", onboardedProduct.getProductId());
        assertEquals(Env.COLL, onboardedProduct.getEnv());
        verify(userConnector, atLeast(1)).findAndCreate((String) any(), (UserBinding) any());
        verify(userConnector, atLeast(1)).findById((String) any());
        verify(onboardedUser, atLeast(1)).getId();
    }

    /**
     * Method under test: {@link OnboardingDao#onboardOperator(OnboardingOperatorsRequest, Institution)}
     */
    @Test
    void testOnboardOperator11() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        new ResourceNotFoundException("An error occurred", "Code");

        OnboardedUser onboardedUser = mock(OnboardedUser.class);
        when(onboardedUser.getId()).thenReturn("42");
        UserConnector userConnector = mock(UserConnector.class);
        when(userConnector.findAndCreate((String) any(), (UserBinding) any())).thenReturn(new OnboardedUser());
        doNothing().when(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        doThrow(new InvalidRequestException("An error occurred", "users to update: {}")).when(userConnector)
                .findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(), (OnboardedProduct) any(),
                        (UserBinding) any());
        when(userConnector.findById((String) any())).thenReturn(onboardedUser);
        doNothing().when(userConnector).deleteById((String) any());
        ProductConnector productConnector = mock(ProductConnector.class);
        OnboardingDao onboardingDao = new OnboardingDao(null, null, userConnector, productConnector, new CoreConfig());
        UserToOnboard userToOnboard = mock(UserToOnboard.class);
        when(userToOnboard.getRole()).thenReturn(PartyRole.MANAGER);
        when(userToOnboard.getEnv()).thenReturn(Env.ROOT);
        when(userToOnboard.getId()).thenReturn("42");
        when(userToOnboard.getProductRole()).thenReturn("Product Role");
        doNothing().when(userToOnboard).setEmail((String) any());
        doNothing().when(userToOnboard).setEnv((Env) any());
        doNothing().when(userToOnboard).setId((String) any());
        doNothing().when(userToOnboard).setName((String) any());
        doNothing().when(userToOnboard).setProductRole((String) any());
        doNothing().when(userToOnboard).setRole((PartyRole) any());
        doNothing().when(userToOnboard).setSurname((String) any());
        doNothing().when(userToOnboard).setTaxCode((String) any());
        userToOnboard.setEmail("prof.einstein@example.org");
        userToOnboard.setEnv(Env.COLL);
        userToOnboard.setId("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setName("42");
        userToOnboard.setProductRole("42");
        userToOnboard.setRole(PartyRole.SUB_DELEGATE);
        userToOnboard.setSurname("it.pagopa.selfcare.mscore.model.user.UserToOnboard");
        userToOnboard.setTaxCode("42");

        ArrayList<UserToOnboard> userToOnboardList = new ArrayList<>();
        userToOnboardList.add(userToOnboard);

        OnboardingOperatorsRequest onboardingOperatorsRequest = new OnboardingOperatorsRequest();
        onboardingOperatorsRequest.setInstitutionId("42");
        onboardingOperatorsRequest.setProductId("42");
        onboardingOperatorsRequest.setUsers(userToOnboardList);
        assertTrue(onboardingDao.onboardOperator(onboardingOperatorsRequest, new Institution()).isEmpty());
        verify(userConnector).findById((String) any());
        verify(userConnector).findAndRemoveProduct((String) any(), (String) any(), (OnboardedProduct) any());
        verify(userConnector).findAndUpdate((OnboardedUser) any(), (String) any(), (String) any(),
                (OnboardedProduct) any(), (UserBinding) any());
        verify(onboardedUser).getId();
        verify(userToOnboard).getRole();
        verify(userToOnboard, atLeast(1)).getEnv();
        verify(userToOnboard, atLeast(1)).getId();
        verify(userToOnboard).getProductRole();
        verify(userToOnboard).setEmail((String) any());
        verify(userToOnboard).setEnv((Env) any());
        verify(userToOnboard).setId((String) any());
        verify(userToOnboard).setName((String) any());
        verify(userToOnboard).setProductRole((String) any());
        verify(userToOnboard).setRole((PartyRole) any());
        verify(userToOnboard).setSurname((String) any());
        verify(userToOnboard).setTaxCode((String) any());
    }

    /**
     * Method under test: {@link OnboardingDao#getProductById(String)}
     */
    @Test
    void testGetProductById() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        ProductConnector productConnector = mock(ProductConnector.class);
        when(productConnector.getProductById((String) any())).thenReturn(product);
        assertSame(product,
                (new OnboardingDao(null, null, null, productConnector, new CoreConfig())).getProductById("42"));
        verify(productConnector).getProductById((String) any());
    }

    /**
     * Method under test: {@link OnboardingDao#getProductById(String)}
     */
    @Test
    void testGetProductById2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        when(productConnector.getProductById((String) any()))
                .thenThrow(new InvalidRequestException("An error occurred", "Code"));
        assertThrows(InvalidRequestException.class,
                () -> (new OnboardingDao(null, null, null, productConnector, new CoreConfig())).getProductById("42"));
        verify(productConnector).getProductById((String) any());
    }

    /**
     * Method under test: {@link OnboardingDao#getTokenById(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetTokenById() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException
        //       at it.pagopa.selfcare.mscore.core.OnboardingDao.getTokenById(OnboardingDao.java:262)
        //   See https://diff.blue/R013 to resolve this issue.

        ProductConnector productConnector = mock(ProductConnector.class);
        (new OnboardingDao(null, null, null, productConnector, new CoreConfig())).getTokenById("42");
    }

    /**
     * Method under test: {@link OnboardingDao#getTokenById(String)}
     */
    @Test
    void testGetTokenById2() {
        //   Diffblue Cover was unable to write a Spring test,
        //   so wrote a non-Spring test instead.
        //   Reason: R026 Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: Failed to load ApplicationContext
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   org.springframework.beans.factory.BeanDefinitionStoreException: Failed to parse configuration class [it.pagopa.selfcare.mscore.config.CoreConfig]; nested exception is java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   java.io.FileNotFoundException: class path resource [config/core-config.properties] cannot be opened because it does not exist
        //       at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //       at java.util.Spliterators$ArraySpliterator.tryAdvance(Spliterators.java:958)
        //       at java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:127)
        //       at java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:502)
        //       at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:488)
        //       at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
        //       at java.util.stream.FindOps$FindOp.evaluateSequential(FindOps.java:150)
        //       at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.util.stream.ReferencePipeline.findFirst(ReferencePipeline.java:543)
        //   See https://diff.blue/R026 to resolve this issue.

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
        TokenConnector tokenConnector = mock(TokenConnector.class);
        when(tokenConnector.findById((String) any())).thenReturn(token);
        ProductConnector productConnector = mock(ProductConnector.class);
        assertSame(token,
                (new OnboardingDao(null, tokenConnector, null, productConnector, new CoreConfig())).getTokenById("42"));
        verify(tokenConnector).findById((String) any());
    }
}
