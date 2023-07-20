package it.pagopa.selfcare.mscore.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.notification.MessageRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductRoleInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailPreparationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserNotificationServiceImpl implements UserNotificationService {

    private static final String ACTIVATE_SUBJECT = "User has been activated";
    private static final String DELETE_SUBJECT = "User had been deleted";
    private static final String SUSPEND_SUBJECT = "User has been suspended";
    private static final String CREATE_SUBJECT = "A new user has been added";
    private static final String ACTIVATE_TEMPLATE = "user_activated.ftlh";
    private static final String DELETE_TEMPLATE = "user_deleted.ftlh";
    private static final String SUSPEND_TEMPLATE = "user_suspended.ftlh";
    private static final String CREATE_TEMPLATE_SINGLE_ROLE = "user_added_single_role.ftlh";
    private static final String CREATE_TEMPLATE_MULTIPLE_ROLE = "user_added_multi_role.ftlh";
    private static final String A_PRODUCT_TITLE_IS_REQUIRED = "A product Title is required";
    private static final String INSTITUTION_ID_IS_REQUIRED = "An institution id is required";
    private static final String PRODUCT_ROLES_ARE_REQUIRED = "ProductRoles are required";

    private final Configuration freemarkerConfig;
    private final NotificationServiceConnector notificationConnector;
    private final ProductConnector productsConnector;
    private final UserService userService;
    private final InstitutionService institutionService;

    @Autowired
    public UserNotificationServiceImpl(Configuration freemarkerConfig,
                                       NotificationServiceConnector notificationConnector,
                                       ProductConnector productsConnector,
                                       UserService userService,
                                       InstitutionService institutionService) {
        this.freemarkerConfig = freemarkerConfig;
        this.notificationConnector = notificationConnector;
        this.productsConnector = productsConnector;
        this.userService = userService;
        this.institutionService = institutionService;
    }


    @Override
    @Async
    public void sendAddedProductRoleNotification(String userId, Institution institution, String productTitle, List<String> roleLabels) {
        log.trace("sendAddedProductRoleNotification start");
        log.debug("sendAddedProductRoleNotification institutionId = {}, productTitle = {}, userId = {}, productRoles = {}", institution.getId(), productTitle, userId, roleLabels);
        Assert.notNull(institution.getId(), INSTITUTION_ID_IS_REQUIRED);
        Assert.notNull(productTitle, A_PRODUCT_TITLE_IS_REQUIRED);
        Assert.notEmpty(roleLabels, PRODUCT_ROLES_ARE_REQUIRED);
        User user = userService.retrieveUserFromUserRegistry(userId, EnumSet.allOf(User.Fields.class));
        Assert.notNull(user.getWorkContacts(), "WorkContacts is required to get email");
        Assert.notNull(user.getWorkContacts().get(institution.getId()), String.format("WorkContacts for institution %s is required to get email", institution.getId()));

        String email = user.getWorkContacts().get(institution.getId()).getEmail();
        Assert.isTrue(StringUtils.hasText(email), "Email is required");

        Assert.notNull(user.getName(), "RequesterName is required");
        Assert.notNull(user.getFamilyName(), "RequesterSurname is required");

        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("requesterName", user.getName());
        dataModel.put("requesterSurname", user.getFamilyName());

        sendCreateNotification(institution.getDescription(), productTitle, email, roleLabels, dataModel);
        log.trace("sendAddedProductRoleNotification start");
    }

    private void sendCreateNotification(String description, String productTitle, String email, List<String> roleLabels, Map<String, String> dataModel) {
        log.debug("sendCreateNotification start");
        log.debug("sendCreateNotification institution = {}, productTitle = {}, email = {}", description, productTitle, email);

        dataModel.put("productName", productTitle);
        dataModel.put("institutionName", description);
        if (roleLabels.size() > 1) {
            String roleLabel = roleLabels.stream()
                    .limit(roleLabels.size() - 1L)
                    .collect(Collectors.joining(", "));

            dataModel.put("productRoles", roleLabel);
            dataModel.put("lastProductRole", roleLabels.get(roleLabels.size() - 1));
            sendNotification(email, CREATE_TEMPLATE_MULTIPLE_ROLE, CREATE_SUBJECT, dataModel);
        } else {
            String roleLabel = roleLabels.get(0);
            dataModel.put("productRole", roleLabel);
            sendNotification(email, CREATE_TEMPLATE_SINGLE_ROLE, CREATE_SUBJECT, dataModel);
        }
        log.debug("sendCreateNotification end");
    }

    @Override
    @Async
    public void sendActivatedUserNotification(String relationshipId, String userId, UserBinding binding) {
        log.trace("sendActivatedUserNotification start");
        log.debug("sendActivatedUserNotification relationshipId = {}", relationshipId);
        sendRelationshipBasedNotification(relationshipId, userId, binding, ACTIVATE_TEMPLATE, ACTIVATE_SUBJECT);
        log.debug("sendActivatedUserNotification end");
    }


    @Override
    @Async
    public void sendDeletedUserNotification(String relationshipId, String userId, UserBinding binding) {
        log.trace("sendDeletedUserNotification start");
        log.debug("sendDeletedUserNotification relationshipId = {}", relationshipId);
        sendRelationshipBasedNotification(relationshipId, userId, binding, DELETE_TEMPLATE, DELETE_SUBJECT);
        log.debug("sendDeletedUserNotification end");
    }


    @Override
    @Async
    public void sendSuspendedUserNotification(String relationshipId, String userId, UserBinding binding) {
        log.trace("sendSuspendedUserNotification start");
        log.debug("sendSuspendedUserNotification relationshipId = {}", relationshipId);
        sendRelationshipBasedNotification(relationshipId, userId, binding, SUSPEND_TEMPLATE, SUSPEND_SUBJECT);
        log.debug("sendSuspendedUserNotification end");
    }


    private void sendRelationshipBasedNotification(String relationshipId, String userId, UserBinding binding, String templateName, String subject) {
        Assert.notNull(relationshipId, "A relationship Id is required");

        User user = userService.retrieveUserFromUserRegistry(userId, EnumSet.allOf(User.Fields.class));
        Assert.notNull(user.getWorkContacts(), "WorkContacts is required to get email");
        Assert.notNull(user.getWorkContacts().get(binding.getInstitutionId()), String.format("WorkContacts for institution %s is required to get email", binding.getInstitutionId()));

        String email = user.getWorkContacts().get(binding.getInstitutionId()).getEmail();
        Assert.isTrue(StringUtils.hasText(email), "Email is required");

        Assert.notNull(user.getName(), "RequesterName is required");
        Assert.notNull(user.getFamilyName(), "RequesterSurname is required");

        Institution institution = institutionService.retrieveInstitutionById(binding.getInstitutionId());
        Assert.notNull(institution.getDescription(), "An institution description is required");

        OnboardedProduct onboardedProduct = binding.getProducts().stream().filter(product -> relationshipId.equalsIgnoreCase(product.getRelationshipId()))
                        .findFirst().orElseThrow();
        Assert.notNull(onboardedProduct.getProductId(), "A product Id is required");
        Assert.notNull(institution.getDescription(), "An institution description is required");
        Product product = productsConnector.getProductById(onboardedProduct.getProductId());
        Assert.notNull(product.getTitle(), A_PRODUCT_TITLE_IS_REQUIRED);
        Optional<String> roleLabel = product.getRoleMappings().values().stream()
                .flatMap(productRoleInfo -> productRoleInfo.getRoles().stream())
                .filter(productRole -> productRole.getCode().equals(onboardedProduct.getProductRole()))
                .map(ProductRoleInfo.ProductRole::getLabel)
                .findAny();

        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("productName", product.getTitle());
        dataModel.put("productRole", roleLabel.orElse("no_role_found"));
        dataModel.put("institutionName", institution.getDescription());
        dataModel.put("requesterName", user.getName());
        dataModel.put("requesterSurname", user.getFamilyName());

        sendNotification(email, templateName, subject, dataModel);
    }

    private void sendNotification(String email, String templateName, String subject, Map<String, String> dataModel) {
        try {
            Template template = freemarkerConfig.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataModel);
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setContent(html);
            messageRequest.setReceiverEmail(email);
            messageRequest.setSubject(subject);
            notificationConnector.sendNotificationToUser(messageRequest);
        } catch (Exception e) {
            throw new MailPreparationException(e);
        }
    }
}
