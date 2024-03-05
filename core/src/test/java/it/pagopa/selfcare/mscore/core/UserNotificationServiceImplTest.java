package it.pagopa.selfcare.mscore.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.api.NotificationServiceConnector;
import it.pagopa.selfcare.mscore.model.CertifiedField;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.MailTemplate;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.onboarding.common.PartyRole;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.entity.ProductRole;
import it.pagopa.selfcare.product.entity.ProductRoleInfo;
import it.pagopa.selfcare.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailPreparationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNotificationServiceImplTest {

    @InjectMocks
    private UserNotificationServiceImpl userNotificationService;

    @Mock
    private Configuration freemarkerConfig;

    @Mock
    private NotificationServiceConnector notificationConnector;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private InstitutionService institutionService;

    @Mock
    private FileStorageConnector fileStorageConnector;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    void sendAddedProductRoleNotification() throws IOException {
        Institution institution = new Institution();
        institution.setId("id");
        List<String> labels = new ArrayList<>();
        labels.add("label");

        User user1 = TestUtils.createSimpleUser();

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);
        when(userService.retrieveUserFromUserRegistry(any())).thenReturn(user1);
        Template template = mock(Template.class);
        when(freemarkerConfig.getTemplate(any())).thenReturn(template);
        assertDoesNotThrow(() -> userNotificationService.sendAddedProductRoleNotification("id",institution,"product", labels, "name","surname"));
    }

    @Test
    void sendDelegationUserNotification() throws JsonProcessingException {
        when(fileStorageConnector.getTemplateFile(any())).thenReturn("template");
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.setBody(Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8)));
        mailTemplate.setSubject(Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8)));
        when(objectMapper.readValue(anyString(),(Class) any())).thenReturn(mailTemplate);
        assertDoesNotThrow(() -> userNotificationService.sendDelegationUserNotification(List.of("id"),"institution","product", new HashMap<>()));
    }


    @Test
    void sendAddedProductRoleNotification2() throws IOException {
        Institution institution = new Institution();
        institution.setId("id");
        List<String> labels = new ArrayList<>();
        labels.add("label");
        labels.add("la");
        labels.add("Lab");

        User user1 = TestUtils.createSimpleUser();

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);
        when(userService.retrieveUserFromUserRegistry(any())).thenReturn(user1);
        Template template = mock(Template.class);
        when(freemarkerConfig.getTemplate(any())).thenReturn(template);
        assertDoesNotThrow(() -> userNotificationService.sendAddedProductRoleNotification("id",institution,"product", labels, "name","surname"));
    }

    @Test
    void sendDeletedUserNotification(){
        User user1 = TestUtils.createSimpleUser();

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);
        when(userService.retrieveUserFromUserRegistry(any())).thenReturn(user1);

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("id");

        Institution institution = new Institution();
        institution.setId("id");
        institution.setDescription("desc");

        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("id");
        onboardedProduct.setRelationshipId("id");
        onboardedProduct.setProductRole("MANAGER");
        List<OnboardedProduct> onboardedProducts = new ArrayList<>();
        onboardedProducts.add(onboardedProduct);

        userBinding.setProducts(onboardedProducts);

        Product product = new Product();
        product.setId("id");
        product.setTitle("id");
        EnumMap<PartyRole, ProductRoleInfo> map = new EnumMap<>(PartyRole.class);
        ProductRoleInfo productRoleInfo = new ProductRoleInfo();
        List<ProductRole> productRoles = new ArrayList<>();
        ProductRole productRole = new ProductRole();
        productRole.setLabel("MANAGER");
        productRole.setCode("MANAGER");
        productRoles.add(productRole);
        productRoleInfo.setRoles(productRoles);
        map.put(PartyRole.MANAGER, productRoleInfo);

        product.setRoleMappings(map);

        when(productService.getProduct(any())).thenReturn(product);

        assertThrows(MailPreparationException.class, () -> userNotificationService.sendDeletedUserNotification("id","userid",userBinding, "name","surname"));
    }

    @Test
    void sendActivatedUserNotification(){
        User user1 = TestUtils.createSimpleUser();

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);
        when(userService.retrieveUserFromUserRegistry(any())).thenReturn(user1);

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("id");

        Institution institution = new Institution();
        institution.setId("id");
        institution.setDescription("desc");

        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("id");
        onboardedProduct.setRelationshipId("id");
        onboardedProduct.setProductRole("MANAGER");
        List<OnboardedProduct> onboardedProducts = new ArrayList<>();
        onboardedProducts.add(onboardedProduct);

        userBinding.setProducts(onboardedProducts);

        Product product = new Product();
        product.setId("id");
        product.setTitle("id");
        EnumMap<PartyRole, ProductRoleInfo> map = new EnumMap<>(PartyRole.class);
        ProductRoleInfo productRoleInfo = new ProductRoleInfo();
        List<ProductRole> productRoles = new ArrayList<>();
        ProductRole productRole = new ProductRole();
        productRole.setLabel("MANAGER");
        productRole.setCode("MANAGER");
        productRoles.add(productRole);
        productRoleInfo.setRoles(productRoles);
        map.put(PartyRole.MANAGER, productRoleInfo);

        product.setRoleMappings(map);

        when(productService.getProduct(any())).thenReturn(product);

        assertThrows(MailPreparationException.class, () -> userNotificationService.sendActivatedUserNotification("id","userid",userBinding,"name","surname"));
    }


    @Test
    void sendSuspendedUserNotification() throws JsonProcessingException {
        User user1 = TestUtils.createSimpleUser();

        Map<String, WorkContact> workContacts1 = new HashMap<>();
        WorkContact workContact = new WorkContact();
        CertifiedField<String> email = new CertifiedField<>();
        email.setValue("email");
        workContact.setEmail(email);
        workContacts1.put("id",workContact);

        user1.setWorkContacts(workContacts1);
        when(userService.retrieveUserFromUserRegistry(any())).thenReturn(user1);

        UserBinding userBinding = new UserBinding();
        userBinding.setInstitutionId("id");

        Institution institution = new Institution();
        institution.setId("id");
        institution.setDescription("desc");

        when(institutionService.retrieveInstitutionById(any())).thenReturn(institution);

        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId("id");
        onboardedProduct.setRelationshipId("id");
        onboardedProduct.setProductRole("MANAGER");
        List<OnboardedProduct> onboardedProducts = new ArrayList<>();
        onboardedProducts.add(onboardedProduct);

        userBinding.setProducts(onboardedProducts);

        Product product = new Product();
        product.setId("id");
        product.setTitle("id");
        EnumMap<PartyRole, ProductRoleInfo> map = new EnumMap<>(PartyRole.class);
        ProductRoleInfo productRoleInfo = new ProductRoleInfo();
        List<ProductRole> productRoles = new ArrayList<>();
        ProductRole productRole = new ProductRole();
        productRole.setLabel("MANAGER");
        productRole.setCode("MANAGER");
        productRoles.add(productRole);
        productRoleInfo.setRoles(productRoles);
        map.put(PartyRole.MANAGER, productRoleInfo);

        product.setRoleMappings(map);

        when(productService.getProduct(any())).thenReturn(product);

        assertThrows(MailPreparationException.class, () -> userNotificationService.sendSuspendedUserNotification("id","userid",userBinding, "name","surname"));
    }
}
