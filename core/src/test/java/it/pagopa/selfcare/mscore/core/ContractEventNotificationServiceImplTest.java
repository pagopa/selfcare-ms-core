package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.esig.dss.spi.tsl.TrustedListsCertificateSource;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.KafkaPropertiesConfig;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Certification;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static it.pagopa.selfcare.commons.utils.TestUtils.checkNotNullFields;
import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ContractEventNotificationServiceImplTest {



    @InjectMocks
    private ContractEventNotificationServiceImpl contractService;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PartyRegistryProxyConnector partyRegistryProxyConnectorMock;

    @Mock
    private KafkaPropertiesConfig kafkaPropertiesConfig;

    @Mock
    private InstitutionConnector institutionConnector;

    /**
     * Method under test: {@link ContractEventNotificationServiceImpl#sendDataLakeNotification(Institution, Token, QueueEvent)}
     */
    @Test
    void testSendDataLakeNotification2() {
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        CoreConfig coreConfig = new CoreConfig();

        UserRegistryConnector userRegistryConnector = mock(UserRegistryConnector.class);
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ContractEventNotificationService contractService = new ContractEventNotificationServiceImpl(kafkaTemplate, new KafkaPropertiesConfig(),
               partyRegistryProxyConnector, institutionConnector, coreConfig);
        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId("prod");

        Institution institution = mockInstance(new Institution(), "setCity", "setCounty", "setCountry");
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));

        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());

        TokenUser tokenUser1 = new TokenUser("tokenUserId1", PartyRole.MANAGER);
        TokenUser tokenUser2 = new TokenUser("tokenUserId2", PartyRole.DELEGATE);

        Token token = mockInstance(new Token());
        token.setId(UUID.randomUUID().toString());
        token.setProductId("prod");
        token.setStatus(RelationshipState.ACTIVE);
        token.setInstitutionUpdate(institutionUpdate);
        token.setDeletedAt(null);
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setContractSigned("docs/parties".concat("/").concat(token.getId()).concat("/").concat("fileName.pdf"));
        token.setContentType(MediaType.APPLICATION_JSON_VALUE);

        User user1 = new User();
        user1.setId(tokenUser1.getUserId());
        user1.setName(createCertifiedField_certified("User1Name"));
        user1.setFamilyName(createCertifiedField_certified("User1FamilyName"));
        user1.setFiscalCode("FiscalCode1");
        Map<String, WorkContact> workContacts1 = new HashMap<>();
        workContacts1.put("NotFoundInstitutionId", createWorkContact("user1workemailNotFound@examepl.com"));
        workContacts1.put(institution.getId(), createWorkContact("user1workEmail@example.com"));
        user1.setWorkContacts(workContacts1);

        User user2 = new User();
        user2.setId(tokenUser1.getUserId());
        user2.setName(createCertifiedField_uncertified("User2Name"));
        user2.setFamilyName(createCertifiedField_certified("User2FamilyName"));
        user2.setFiscalCode("FiscalCode2");
        Map<String, WorkContact> workContacts2 = new HashMap<>();
        workContacts2.put(institution.getId(), createWorkContact("user2workEmail@example.com"));
        workContacts2.put("NotFoundInstitutionId", createWorkContact("user2workemailNotFound@examepl.com"));
        workContacts2.put("NotFoundInstitutionId2", createWorkContact("user2workemailNotFound2@examepl.com"));
        user2.setWorkContacts(workContacts2);

        InstitutionProxyInfo institutionProxyInfoMock = mockInstance(new InstitutionProxyInfo());
        institutionProxyInfoMock.setTaxCode(institution.getExternalId());

        GeographicTaxonomies geographicTaxonomiesMock = mockInstance(new GeographicTaxonomies());
        geographicTaxonomiesMock.setIstatCode(institutionProxyInfoMock.getIstatCode());
        when(partyRegistryProxyConnector.getInstitutionById(any()))
                .thenReturn(institutionProxyInfoMock);
        when(partyRegistryProxyConnector.getExtByCode(any())).thenReturn(geographicTaxonomiesMock);

        assertThrows(IllegalArgumentException.class, () -> contractService.sendDataLakeNotification(institution, token, QueueEvent.ADD),
                "Topic cannot be null");

        verify(partyRegistryProxyConnector, times(1))
                .getInstitutionById(institution.getExternalId());
        verifyNoMoreInteractions(userRegistryConnector, partyRegistryProxyConnector);
    }

    @Test
    void testSendDataLakeNotification3() {
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        CoreConfig coreConfig = new CoreConfig();

        UserRegistryConnector userRegistryConnector = mock(UserRegistryConnector.class);
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);
        ContractEventNotificationService contractService = new ContractEventNotificationServiceImpl(kafkaTemplate, new KafkaPropertiesConfig(),
                partyRegistryProxyConnector, institutionConnector, coreConfig);

        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId("prod");

        Institution institution = mockInstance(new Institution());
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));

        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());

        TokenUser tokenUser1 = new TokenUser("tokenUserId1", PartyRole.MANAGER);
        TokenUser tokenUser2 = new TokenUser("tokenUserId2", PartyRole.DELEGATE);

        Token token = mockInstance(new Token());
        token.setId(UUID.randomUUID().toString());
        token.setProductId("prod");
        token.setStatus(RelationshipState.ACTIVE);
        token.setInstitutionUpdate(institutionUpdate);
        token.setDeletedAt(null);
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setContractSigned("docs/parties".concat("/").concat(token.getId()).concat("/").concat("fileName.pdf"));
        token.setContentType(MediaType.APPLICATION_JSON_VALUE);
        assertThrows(IllegalArgumentException.class, () -> contractService.sendDataLakeNotification(institution, token, QueueEvent.ADD),
                "Topic cannot be null");

        verifyNoMoreInteractions(userRegistryConnector, partyRegistryProxyConnector);
    }

    /**
     * Method under test: {@link ContractEventNotificationServiceImpl#sendDataLakeNotification(Institution, Token, QueueEvent)}
     */
    @ParameterizedTest
    @ValueSource(classes = {
            MsCoreException.class,
            ResourceNotFoundException.class
    })
    void testSendDataLakeNotification_notOnIpa(Class<?> clazz) throws ExecutionException, InterruptedException {
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        PagoPaSignatureConfig pagoPaSignatureConfig = new PagoPaSignatureConfig();
        CoreConfig coreConfig = new CoreConfig();
        Pkcs7HashSignService pkcs7HashSignService = mock(Pkcs7HashSignService.class);
        SignatureService signatureService = new SignatureService(new TrustedListsCertificateSource());
        UserRegistryConnector userRegistryConnector = mock(UserRegistryConnector.class);
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);

        ContractEventNotificationService contractService = new ContractEventNotificationServiceImpl(kafkaTemplate, new KafkaPropertiesConfig(),
                partyRegistryProxyConnector, institutionConnector, coreConfig);

        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId("prod");

        Institution institution = mockInstance(new Institution(), "setCity");
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));

        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());

        TokenUser tokenUser1 = new TokenUser("tokenUserId1", PartyRole.MANAGER);
        TokenUser tokenUser2 = new TokenUser("tokenUserId2", PartyRole.DELEGATE);

        Token token = mockInstance(new Token());
        token.setProductId("prod");
        token.setStatus(RelationshipState.ACTIVE);
        token.setInstitutionUpdate(institutionUpdate);
        token.setDeletedAt(null);
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setContractSigned(null);

        User user1 = new User();
        user1.setId(tokenUser1.getUserId());
        user1.setName(createCertifiedField_certified("User1Name"));
        user1.setFamilyName(createCertifiedField_certified("User1FamilyName"));
        user1.setFiscalCode("FiscalCode1");
        Map<String, WorkContact> workContacts1 = new HashMap<>();
        workContacts1.put("NotFoundInstitutionId", createWorkContact("user1workemailNotFound@examepl.com"));
        workContacts1.put(institution.getId(), createWorkContact("user1workEmail@example.com"));
        user1.setWorkContacts(workContacts1);

        User user2 = new User();
        user2.setId(tokenUser1.getUserId());
        user2.setName(createCertifiedField_uncertified("User2Name"));
        user2.setFamilyName(createCertifiedField_certified("User2FamilyName"));
        user2.setFiscalCode("FiscalCode2");
        Map<String, WorkContact> workContacts2 = new HashMap<>();
        workContacts2.put(institution.getId(), createWorkContact("user2workEmail@example.com"));
        workContacts2.put("NotFoundInstitutionId", createWorkContact("user2workemailNotFound@examepl.com"));
        workContacts2.put("NotFoundInstitutionId2", createWorkContact("user2workemailNotFound2@examepl.com"));
        user2.setWorkContacts(workContacts2);

        Exception exceptionMock = (Exception) Mockito.mock(clazz);

        when(partyRegistryProxyConnector.getInstitutionById(any()))
                .thenThrow(exceptionMock);

        assertThrows(IllegalArgumentException.class, () -> contractService.sendDataLakeNotification(institution, token, QueueEvent.ADD),
                "Topic cannot be null");

        verifyNoMoreInteractions(userRegistryConnector, partyRegistryProxyConnector);
    }

    @Test
    void testSendDataLakeNotification_updateQueueEvent() {
        ProducerFactory<String, String> producerFactory = (ProducerFactory<String, String>) mock(ProducerFactory.class);
        when(producerFactory.transactionCapable()).thenReturn(true);
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);

        CoreConfig coreConfig = new CoreConfig();
        PartyRegistryProxyConnector partyRegistryProxyConnector = mock(PartyRegistryProxyConnector.class);
        InstitutionConnector institutionConnector = mock(InstitutionConnector.class);

        ContractEventNotificationService contractService = new ContractEventNotificationServiceImpl(kafkaTemplate, new KafkaPropertiesConfig(),
                partyRegistryProxyConnector, institutionConnector, coreConfig);

        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId("prod");

        Institution institution = mockInstance(new Institution(), "setCity", "setCounty", "setCountry");
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));

        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());

        TokenUser tokenUser1 = new TokenUser("tokenUserId1", PartyRole.MANAGER);
        TokenUser tokenUser2 = new TokenUser("tokenUserId2", PartyRole.DELEGATE);

        Token token = mockInstance(new Token());
        token.setProductId("prod");
        token.setStatus(RelationshipState.DELETED);
        token.setInstitutionUpdate(institutionUpdate);
        token.setDeletedAt(null);
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setContractSigned(null);
        token.setContentType(null);

        User user1 = new User();
        user1.setId(tokenUser1.getUserId());
        user1.setName(createCertifiedField_certified("User1Name"));
        user1.setFamilyName(createCertifiedField_certified("User1FamilyName"));
        user1.setFiscalCode("FiscalCode1");
        Map<String, WorkContact> workContacts1 = new HashMap<>();
        workContacts1.put("NotFoundInstitutionId", createWorkContact("user1workemailNotFound@examepl.com"));
        workContacts1.put(institution.getId(), createWorkContact("user1workEmail@example.com"));
        user1.setWorkContacts(workContacts1);

        User user2 = new User();
        user2.setId(tokenUser1.getUserId());
        user2.setName(createCertifiedField_uncertified("User2Name"));
        user2.setFamilyName(createCertifiedField_certified("User2FamilyName"));
        user2.setFiscalCode("FiscalCode2");
        Map<String, WorkContact> workContacts2 = new HashMap<>();
        workContacts2.put(institution.getId(), createWorkContact("user2workEmail@example.com"));
        workContacts2.put("NotFoundInstitutionId", createWorkContact("user2workemailNotFound@examepl.com"));
        workContacts2.put("NotFoundInstitutionId2", createWorkContact("user2workemailNotFound2@examepl.com"));
        user2.setWorkContacts(workContacts2);

        InstitutionProxyInfo institutionProxyInfoMock = mockInstance(new InstitutionProxyInfo());
        institutionProxyInfoMock.setTaxCode(institution.getExternalId());

        GeographicTaxonomies geographicTaxonomiesMock = mockInstance(new GeographicTaxonomies());
        geographicTaxonomiesMock.setIstatCode(institutionProxyInfoMock.getIstatCode());
        when(partyRegistryProxyConnector.getInstitutionById(any()))
                .thenReturn(institutionProxyInfoMock);
        when(partyRegistryProxyConnector.getExtByCode(any())).thenReturn(geographicTaxonomiesMock);

        assertThrows(IllegalArgumentException.class, () -> contractService.sendDataLakeNotification(institution, token, QueueEvent.UPDATE),
                "Topic cannot be null");

        verify(partyRegistryProxyConnector, times(1))
                .getInstitutionById(institution.getExternalId());
    }



    /**
     * Method under test: {@link ContractEventNotificationServiceImpl#toNotificationToSend(Institution, Token, QueueEvent)}
     */
    @Test
    void testGenerateMessageActiveWithActivatedAt() throws ExecutionException, InterruptedException {

        String institutionId = "i1";
        String tokenId = "t1";

        Onboarding onboarding = createOnboarding(tokenId, "prod");
        Institution institution = createInstitutionWithoutLocation(institutionId, onboarding);
        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        Token token = createToken(institutionId, tokenId, institutionUpdate,
                RelationshipState.ACTIVE,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt

        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institution);

        NotificationToSend notification = contractService.toNotificationToSend(institution, token, QueueEvent.ADD);

        assertNotNull(notification);
        assertNull(notification.getClosedAt());
        assertEquals(RelationshipState.ACTIVE.toString(), notification.getState());
        assertEquals(token.getActivatedAt(), notification.getCreatedAt());
        assertEquals(token.getUpdatedAt(), notification.getCreatedAt());
        assertEquals(QueueEvent.ADD, notification.getNotificationType());
    }

    /**
     * Method under test: {@link ContractEventNotificationServiceImpl#toNotificationToSend(Institution, Token, QueueEvent)}
     */
    @Test
    void testGenerateMessageActiveWithoutActivatedAt() {

        String institutionId = "i1";
        String tokenId = "t1";

        Onboarding onboarding = createOnboarding(tokenId, "prod");
        Institution institution = createInstitutionWithoutLocation(institutionId, onboarding);
        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        Token token = createToken(institutionId, tokenId, institutionUpdate,
                RelationshipState.ACTIVE,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt

        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institution);

        NotificationToSend notification = contractService.toNotificationToSend(institution, token, QueueEvent.ADD);

        assertNotNull(notification);
        assertNull(notification.getClosedAt());
        assertEquals(RelationshipState.ACTIVE.toString(), notification.getState());
        assertEquals(token.getCreatedAt(), notification.getCreatedAt());
        assertEquals(token.getCreatedAt(), notification.getUpdatedAt());
        assertEquals(QueueEvent.ADD, notification.getNotificationType());
    }

    /**
     * Method under test: {@link ContractEventNotificationServiceImpl#toNotificationToSend(Institution, Token, QueueEvent)}
     */
    @Test
    void testGenerateMessageClosedWithoutActivatedAt() {

        String institutionId = "i1";
        String tokenId = "t1";

        Onboarding onboarding = createOnboarding(tokenId, "prod");
        Institution institution = createInstitutionWithoutLocation(institutionId, onboarding);
        InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        Token token = createToken(institutionId, tokenId, institutionUpdate,
                RelationshipState.DELETED,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt

        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institution);
        when(institutionConnector.findById(any())).thenReturn(null);

        NotificationToSend notification = contractService.toNotificationToSend(institution, token, QueueEvent.UPDATE);

        assertNotNull(notification);
        assertNotNull(notification.getClosedAt());
        assertEquals("CLOSED", notification.getState());
        assertEquals(token.getCreatedAt(), notification.getCreatedAt());
        assertEquals(token.getUpdatedAt(), notification.getUpdatedAt());
        assertEquals(QueueEvent.UPDATE, notification.getNotificationType());
    }

    @Test
    void toNotificationToSend_attributesNull() {
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String tokenId = UUID.randomUUID().toString();
        Institution institutionMock = createInstitution(institutionId, createOnboarding(tokenId, "product"));
        institutionMock.setAttributes(null);
        institutionMock.setOrigin("SELC");
        institutionMock.setRootParentId(null);
        InstitutionProxyInfo institutionProxyInfoMock = mockInstance(new InstitutionProxyInfo());
        institutionProxyInfoMock.setTaxCode(institutionMock.getTaxCode());
        Token tokenMock = createToken(institutionId, tokenId, null,
                RelationshipState.ACTIVE,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt
        tokenMock.setProductId("product");
        //when
        NotificationToSend notificationToSend = contractService.toNotificationToSend(institutionMock, tokenMock, QueueEvent.ADD);
        //then
        checkNotNullFields(notificationToSend, "closedAt");
    }

    @Test
    void toNotificationToSend_emptyAttributes() {
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String tokenId = UUID.randomUUID().toString();
        Institution institutionMock = createInstitutionWithoutLocation(institutionId, createOnboarding(tokenId, "product"));
        institutionMock.setAttributes(new ArrayList<>());
        institutionMock.setOrigin("IPA");
        institutionMock.setRootParentId(null);
        Token tokenMock = createToken(institutionId, tokenId, null,
                RelationshipState.ACTIVE,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt
        tokenMock.setProductId("product");
        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institutionMock);
        //when
        NotificationToSend notificationToSend = contractService.toNotificationToSend(institutionMock, tokenMock, QueueEvent.ADD);
        //then
        checkNotNullFields(notificationToSend, "closedAt");
    }

    @Test
    void toNotificationToSend_nullLocation() {
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String tokenId = UUID.randomUUID().toString();
        Onboarding onboarding = createOnboarding(tokenId, "prod");
        Institution institutionMock = createInstitutionWithoutLocation(institutionId, onboarding);
        institutionMock.setRootParentId(null);
        Token tokenMock = createToken(institutionId, tokenId, null,
                RelationshipState.DELETED,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt
        tokenMock.setProductId("prod");
        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institutionMock);
        //when
        NotificationToSend notification = contractService.toNotificationToSend(institutionMock, tokenMock, QueueEvent.UPDATE);
        //then
        assertNotNull(notification.getInstitution().getCategory());
        assertNotNull(notification.getInstitution().getCity());
        verify(partyRegistryProxyConnectorMock, times(1)).getInstitutionById(institutionMock.getExternalId());
        verify(partyRegistryProxyConnectorMock, times(1)).getExtByCode(any());
    }

    @Test
    void toNotificationAttributesNotNull() {
        //given
        final String institutionId = UUID.randomUUID().toString();
        final String tokenId = UUID.randomUUID().toString();
        Institution institutionMock = createInstitution(institutionId, createOnboarding(tokenId, "product"));
        Attributes attribute = new Attributes();
        attribute.setCode("code");
        institutionMock.setAttributes(List.of(attribute));
        institutionMock.setOrigin("IPA");
        institutionMock.setCity(null);
        institutionMock.setRootParentId(null);
        Token tokenMock = createToken(institutionId, tokenId, null,
                RelationshipState.ACTIVE,
                OffsetDateTime.parse("2020-11-01T10:00:00Z"), // createdAt
                null, // activatedAt
                OffsetDateTime.parse("2020-11-02T10:00:00Z"), // updatedAt
                null); // deletedAt
        tokenMock.setProductId("product");
        mockPartyRegistryProxy(partyRegistryProxyConnectorMock, institutionMock);
        //when
        NotificationToSend notificationToSend = contractService.toNotificationToSend(institutionMock, tokenMock, QueueEvent.ADD);
        //then
        checkNotNullFields(notificationToSend, "closedAt");

    }



    private static Institution createInstitution(String institutionId, Onboarding onboarding) {
        Institution institution = mockInstance(new Institution());
        institution.setId(institutionId);
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));
        return institution;
    }

    private static Onboarding createOnboarding() {
        return mockInstance(new Onboarding());
    }

    private static Institution createInstitutionWithoutLocation(String institutionId, Onboarding onboarding) {
        Institution institution = mockInstance(new Institution(), "setCity", "setCounty", "setCountry");
        institution.setId(institutionId);
        institution.setOrigin("IPA");
        institution.setOnboarding(List.of(onboarding));
        return institution;
    }


    private static Onboarding createOnboarding(String tokenId, String productId) {
        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId(productId);
        onboarding.setTokenId(tokenId);
        return onboarding;
    }

    private static Token createToken(String institutionId, String tokenId, InstitutionUpdate institutionUpdate,
                                     RelationshipState status,
                                     OffsetDateTime createdAt, OffsetDateTime activatedAt,
                                     OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        TokenUser tokenUser1 = new TokenUser("tokenUserId1", PartyRole.MANAGER);
        TokenUser tokenUser2 = new TokenUser("tokenUserId2", PartyRole.DELEGATE);

        Token token = mockInstance(new Token());
        token.setId(tokenId);
        token.setInstitutionId(institutionId);
        token.setProductId("prod");
        token.setStatus(status);
        token.setInstitutionUpdate(institutionUpdate);
        token.setCreatedAt(createdAt);
        token.setActivatedAt(activatedAt);
        token.setUpdatedAt(updatedAt);
        token.setDeletedAt(deletedAt);
        token.setUsers(List.of(tokenUser1, tokenUser2));
        token.setContractSigned("ContractPath".concat("/").concat(token.getId()).concat("/").concat("fileName.pdf"));
        token.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return token;
    }

    private static void mockPartyRegistryProxy(PartyRegistryProxyConnector partyRegistryProxyConnector, Institution institution) {
        InstitutionProxyInfo institutionProxyInfoMock = mockInstance(new InstitutionProxyInfo(), "setCity", "setCounty", "setCountry");
        institutionProxyInfoMock.setTaxCode(institution.getExternalId());

        GeographicTaxonomies geographicTaxonomiesMock = mockInstance(new GeographicTaxonomies());
        geographicTaxonomiesMock.setIstatCode(institutionProxyInfoMock.getIstatCode());

        when(partyRegistryProxyConnector.getInstitutionById(any()))
                .thenReturn(institutionProxyInfoMock);
        when(partyRegistryProxyConnector.getExtByCode(any())).thenReturn(geographicTaxonomiesMock);
    }

    private WorkContact createWorkContact(String workContactEmail) {
        WorkContact workContact = new WorkContact();
        workContact.setEmail(createCertifiedField_certified(workContactEmail));
        return workContact;
    }

    private CertifiedField<String> createCertifiedField_certified(String fieldValue) {
        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.SPID);
        certifiedField.setValue(fieldValue);
        return certifiedField;
    }

    private CertifiedField<String> createCertifiedField_uncertified(String fieldValue) {
        CertifiedField<String> certifiedField = new CertifiedField<>();
        certifiedField.setCertification(Certification.NONE);
        certifiedField.setValue(fieldValue);
        return certifiedField;
    }
}
