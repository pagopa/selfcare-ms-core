package it.pagopa.selfcare.mscore.core;

import feign.FeignException;
import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.strategy.factory.OnboardingInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.DOCUMENT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.CustomError.ONBOARDING_INFO_INSTITUTION_NOT_FOUND;
import static it.pagopa.selfcare.mscore.core.util.TokenUtils.createDigest;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserService userService;
    private final UserRelationshipService userRelationshipService;
    private final UserEventService userEventService;
    private final ContractService contractService;
    private final NotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final PagoPaSignatureConfig pagoPaSignatureConfig;
    private final MailTemplateConfig mailTemplateConfig;
    private final OnboardingInstitutionStrategyFactory institutionStrategyFactory;
    private final InstitutionConnector institutionConnector;
    private final ProductConnector productConnector;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 UserRelationshipService userRelationshipService,
                                 ContractService contractService,
                                 UserEventService userEventService,
                                 NotificationService notificationService,
                                 UserNotificationService userNotificationService,
                                 PagoPaSignatureConfig pagoPaSignatureConfig,
                                 OnboardingInstitutionStrategyFactory institutionStrategyFactory,
                                 InstitutionConnector institutionConnector,
                                 ProductConnector productConnector,
                                 MailTemplateConfig mailTemplateConfig) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.userRelationshipService = userRelationshipService;
        this.userEventService = userEventService;
        this.contractService = contractService;
        this.notificationService = notificationService;
        this.userNotificationService = userNotificationService;
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
        this.institutionStrategyFactory = institutionStrategyFactory;
        this.institutionConnector = institutionConnector;
        this.productConnector = productConnector;
        this.mailTemplateConfig = mailTemplateConfig;
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        institutionService.retrieveInstitutionsWithFilter(externalId, productId, UtilEnumList.VALID_RELATIONSHIP_STATES);
    }

    @Override
    public void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId) {
        Boolean existsOnboardingValid = institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(taxCode,
                Optional.ofNullable(subunitCode), Optional.ofNullable(productId), UtilEnumList.VALID_RELATIONSHIP_STATES);
        if (Boolean.FALSE.equals(existsOnboardingValid)) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_ONBOARDED.getMessage(), taxCode, productId),
                    CustomError.INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {

        List<RelationshipState> relationshipStateList = OnboardingInfoUtils.getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        try {
            List<UserInstitutionAggregation> userInstitutionAggregation = getUserInstitutionAggregation(userId, institutionId, institutionExternalId, relationshipStateList);
            userInstitutionAggregation.forEach(userBinding -> onboardingInfoList.add(new OnboardingInfo(userId, userBinding.getInstitutions().get(0), userBinding.getBindings())));

            if (onboardingInfoList.isEmpty()) {
                throw new InvalidRequestException(CustomError.ONBOARDING_INFO_ERROR.getMessage(), CustomError.ONBOARDING_INFO_ERROR.getCode());
            }
            return onboardingInfoList;
        } catch (ResourceNotFoundException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String userId) {
        return this.getOnboardingInfo(institutionId, null, null, userId);
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(request.getInstitutionExternalId());
        institutionStrategyFactory
                .retrieveOnboardingInstitutionStrategy(request.getInstitutionUpdate().getInstitutionType(), request.getProductId(), institution)
                .onboardingInstitution(request, principal);
    }

    @Override
    public void onboardingInstitutionComplete(OnboardingRequest request, SelfCareUser principal) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(request.getInstitutionExternalId());
        institutionStrategyFactory
                .retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(request.getInstitutionUpdate().getInstitutionType(), institution)
                .onboardingInstitution(request, principal);
    }
    @Override
    public void completeOnboarding(Token token, MultipartFile contract){
        Consumer<List<User>> verification = users -> contractService.verifySignature(contract, token, users);
        this.completeOnboarding(token, contract, verification);
    }

    @Override
    public void completeOnboardingWithoutSignatureVerification(Token token, MultipartFile contract){
        Consumer<List<User>> verification = ignored -> {};
        this.completeOnboarding(token, contract, verification);
    }

    public void completeOnboarding(Token token, MultipartFile contract, Consumer<List<User>> verification) {
        log.trace("completeOnboarding start");
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "completeOboarding token = {} contract = {}", token, contract);
        checkAndHandleExpiring(token);
        List<String> managerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);
        List<User> managersData = managerList.stream()
                .map(user -> userService.retrieveUserFromUserRegistry(user))
                .collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());

        /* check if onboarding ACTIVE already exists for product */
        List<Institution> list = institutionConnector.findWithFilter(institution.getExternalId(), token.getProductId(), UtilEnumList.VALID_RELATIONSHIP_STATES);
        if (list != null && !list.isEmpty()) {
            throw new InvalidRequestException(String.format(GenericError.INSTITUTION_NOT_ONBOARDED.getMessage(), institution.getExternalId(), token.getProductId()),
                    GenericError.INSTITUTION_NOT_ONBOARDED.getCode());
        }

        Product product = onboardingDao.getProductById(token.getProductId());
        if (pagoPaSignatureConfig.isVerifyEnabled()) {
            verification.accept(managersData);
        }
        File logoFile = contractService.getLogoFile();
        String fileName = contractService.uploadContract(token.getId(), contract);
        token.setContractSigned(fileName);
        token.setContentType(contract.getContentType());
        OnboardingUpdateRollback rollback = onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, null);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "completeOnboarding persistedOnboardingRollBack = {}", rollback);
        try {
            notificationService.sendCompletedEmail(managersData, institution, product, logoFile);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate(rollback.getUserList(), rollback.getUpdatedInstitution(), rollback.getToken());
            contractService.deleteContract(fileName, token.getId());
        }
        contractService.sendDataLakeNotification(rollback.getUpdatedInstitution(), rollback.getToken(), QueueEvent.ADD);
        userEventService.sendLegalTokenUserNotification(token);
        log.trace("completeOboarding end");
    }

    @Override
    public void approveOnboarding(Token token, SelfCareUser selfCareUser) {

        checkAndHandleExpiring(token);
        User currentUser = userService.retrieveUserFromUserRegistry(selfCareUser.getId());

        List<OnboardedUser> onboardedUsers = userService.findAllByIds(token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList()));

        List<String> validManagerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);
        User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0));
        List<User> delegate = onboardedUsers
                .stream()
                .filter(onboardedUser -> !validManagerList.contains(onboardedUser.getId()))
                .map(onboardedUser -> userService.retrieveUserFromUserRegistry(onboardedUser.getId())).collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        Product product = productConnector.getProductById(token.getProductId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(token, institution, product);
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        try {
            if(InstitutionType.PT.equals(institutionType)) {
                onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, null);
                File logoFile = contractService.getLogoFile();
                String templatePath = mailTemplateConfig.getCompletePathPt();
                notificationService.sendCompletedEmail(delegate, institution, product, logoFile, templatePath);
            } else {
                String contractTemplate = contractService.extractTemplate(token.getContractTemplate());
                File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null, institutionType);
                String digest = TokenUtils.createDigest(pdf);
                log.info("Digest {}", digest);
                onboardingDao.persistForUpdate(token, institution, RelationshipState.PENDING, digest);
                notificationService.sendMailWithContract(pdf, institution, currentUser, request, token.getId(), true);
            }
        }  catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate((token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList())), institution, token);
        }
    }

    @Override
    public void invalidateOnboarding(Token token) {
        checkAndHandleExpiring(token);
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
    }

    @Override
    public void onboardingReject(Token token) {
        checkAndHandleExpiring(token);
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
        File logo = contractService.getLogoFile();
        Product product = onboardingDao.getProductById(token.getProductId());
        try {
            notificationService.sendRejectMail(logo, institution, product);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate((token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList())), institution, token);
        }
    }

    @Override
    public List<RelationshipInfo> onboardingUsers(OnboardingUsersRequest request, String loggedUserName, String loggedUserSurname) {

        Institution institution = institutionService.getInstitutions(request.getInstitutionTaxCode(), request.getInstitutionSubunitCode()).stream()
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Institution not found!", ""));

        Product product = Optional.ofNullable(productConnector.getProductValidById(request.getProductId()))
                .orElseThrow(() -> new InvalidRequestException("Product not found or is not valid!", ""));

        List<String> roleLabels = request.getUsers().stream()
                .map(UserToOnboard::getRoleLabel).collect(Collectors.toList());

        request.getUsers().forEach(userToOnboard -> fillUserIdAndCreateIfNotExist(userToOnboard, institution.getId()));

        List<RelationshipInfo> relationshipInfoList = onboardingDao.onboardOperator(institution, request.getProductId(), request.getUsers());

        request.getUsers().forEach(userToOnboard -> userNotificationService.sendCreateUserNotification(institution.getDescription(),
                            product.getTitle(), userToOnboard.getEmail(), roleLabels, loggedUserName, loggedUserSurname));

        relationshipInfoList.forEach(relationshipInfo -> userEventService.sendOperatorUserNotification(relationshipInfo, QueueEvent.ADD));

        return relationshipInfoList;
    }

    private void fillUserIdAndCreateIfNotExist(UserToOnboard user, String institutionId){
        User userRegistry;
        try {
            userRegistry =  userService.retrieveUserFromUserRegistryByFiscalCode(user.getTaxCode());
        }
        catch (FeignException.NotFound e) {
            userRegistry = userService.persistUserRegistry(user.getName(), user.getSurname(), user.getTaxCode(), user.getEmail(), institutionId);
        }
        user.setId(userRegistry.getId());
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest onboardingOperatorRequest, PartyRole role, String loggedUserName, String loggedUserSurname) {
        OnboardingInstitutionUtils.verifyUsers(onboardingOperatorRequest.getUsers(), List.of(role));
        Institution institution = institutionService.retrieveInstitutionById(onboardingOperatorRequest.getInstitutionId());
        Map<String, List<UserToOnboard>> userMap = onboardingOperatorRequest.getUsers().stream()
                .collect(Collectors.groupingBy(UserToOnboard::getId));
        List<String> roleLabels = onboardingOperatorRequest.getUsers().stream()
                .map(UserToOnboard::getRoleLabel).collect(Collectors.toList());
        List<RelationshipInfo> relationshipInfoList = onboardingDao.onboardOperator(institution, onboardingOperatorRequest.getProductId(), onboardingOperatorRequest.getUsers());
        userMap.forEach((key, value) -> userNotificationService.sendAddedProductRoleNotification(key, institution,
                onboardingOperatorRequest.getProductTitle(), roleLabels, loggedUserName, loggedUserSurname));
        relationshipInfoList.forEach(relationshipInfo -> userEventService.sendOperatorUserNotification(relationshipInfo, QueueEvent.ADD));
        return relationshipInfoList;
    }

    @Override
    public void onboardingLegals(OnboardingLegalsRequest onboardingLegalsRequest, SelfCareUser selfCareUser, Token token) {
        Institution institution = institutionService.retrieveInstitutionById(onboardingLegalsRequest.getInstitutionId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(onboardingLegalsRequest);
        InstitutionType institutionType = institution.getInstitutionType();
        request.setTokenType(TokenType.LEGALS);

        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        User user = userService.retrieveUserFromUserRegistry(selfCareUser.getId());
        OnboardingInstitutionUtils.verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
        List<String> validManagerList = OnboardingInstitutionUtils.getValidManagerToOnboard(request.getUsers(), token);
        User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0));

        List<User> delegate = request.getUsers().stream()
                .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                .map(userToOnboard -> userService.retrieveUserFromUserRegistry(userToOnboard.getId()))
                .collect(Collectors.toList());

        String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null, institutionType);
        String digest = createDigest(pdf);
        OnboardingRollback rollback = onboardingDao.persistLegals(toUpdate, toDelete, request, institution, digest);
        log.info("{} - Digest {}", rollback.getToken().getId(), digest);
        try {
            notificationService.sendMailWithContract(pdf, institution, user, request, rollback.getToken().getId(), false);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStep(toUpdate, toDelete, institution.getId(), rollback.getToken(), rollback.getOnboarding(), rollback.getProductMap());
        }
    }

    @Override
    public ResourceResponse retrieveDocument(String relationshipId) {
        RelationshipInfo relationship = userRelationshipService.retrieveRelationship(relationshipId);
        if (relationship.getOnboardedProduct() != null &&
                StringUtils.hasText(relationship.getOnboardedProduct().getContract())) {
            return contractService.getFile(relationship.getOnboardedProduct().getContract());
        } else {
            throw new ResourceNotFoundException(String.format(DOCUMENT_NOT_FOUND.getMessage(), relationshipId), DOCUMENT_NOT_FOUND.getCode());
        }
    }

    private void invalidateToken(Token token, Institution institution) {
        log.info("START - invalidate token {}", token.getId());
        onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
    }

    private List<UserInstitutionAggregation> getUserInstitutionAggregation(String userId, String institutionId, String externalId, List<RelationshipState> relationshipStates) {
        List<String> states = relationshipStates.stream().map(Enum::name).collect(Collectors.toList());
        UserInstitutionFilter filter = new UserInstitutionFilter(userId, institutionId, externalId, states);
        List<UserInstitutionAggregation> userInstitutionAggregation = userService.findUserInstitutionAggregation(filter);
        if (userInstitutionAggregation == null || userInstitutionAggregation.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), userId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
        }
        return userInstitutionAggregation;
    }

    public void checkAndHandleExpiring(Token token) {

        if (TokenUtils.isTokenExpired(token)) {
            log.info("token {} is expired at {}", token.getId(), token.getExpiringDate());
            var institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
            throw new InvalidRequestException(String.format(CustomError.TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), CustomError.TOKEN_EXPIRED.getCode());
        }
    }

}
