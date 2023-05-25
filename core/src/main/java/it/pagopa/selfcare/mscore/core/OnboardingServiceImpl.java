package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
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
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
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
    private final ContractService contractService;
    private final EmailService emailService;
    private final PagoPaSignatureConfig pagoPaSignatureConfig;

    private final OnboardingInstitutionStrategyFactory institutionStrategyFactory;

    private final InstitutionConnector institutionConnector;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 UserRelationshipService userRelationshipService,
                                 ContractService contractService,
                                 EmailService emailService,
                                 PagoPaSignatureConfig pagoPaSignatureConfig,
                                 OnboardingInstitutionStrategyFactory institutionStrategyFactory,
                                 InstitutionConnector institutionConnector) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.userRelationshipService = userRelationshipService;
        this.contractService = contractService;
        this.emailService = emailService;
        this.pagoPaSignatureConfig = pagoPaSignatureConfig;
        this.institutionStrategyFactory = institutionStrategyFactory;
        this.institutionConnector = institutionConnector;
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        institutionService.retrieveInstitutionsWithFilter(externalId, productId, UtilEnumList.VALID_RELATIONSHIP_STATES);
    }

    @Override
    public void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId) {
        Boolean existsOnboardingValid = institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(taxCode,
                Optional.ofNullable(subunitCode),  Optional.ofNullable(productId), UtilEnumList.VALID_RELATIONSHIP_STATES);
        if (!existsOnboardingValid) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_ONBOARDED.getMessage(), taxCode, productId),
                    CustomError.INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {

        List<RelationshipState> relationshipStateList = OnboardingInfoUtils.getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        try {
            UserInstitutionAggregation userInstitutionAggregation = getUserInstitutionAggregation(userId);
            List<UserBinding> userBindings = OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(userInstitutionAggregation.getBindings(), relationshipStateList);

            if (StringUtils.hasText(institutionId) || StringUtils.hasText(institutionExternalId)) {
                userInstitutionAggregation.getInstitutions().stream().filter(institution ->
                                institution.getId().equalsIgnoreCase(institutionId) || institution.getExternalId().equalsIgnoreCase(institutionExternalId))
                        .findFirst().ifPresent(institution -> userBindings.removeIf(userBinding -> !institution.getId().equalsIgnoreCase(userBinding.getInstitutionId())));
            }

            userBindings.forEach(userBinding -> userInstitutionAggregation.getInstitutions()
                    .stream()
                    .filter(institution -> institution.getId().equalsIgnoreCase(userBinding.getInstitutionId()))
                    .findFirst()
                    .ifPresent(institution -> {
                        OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(userBinding, institution, relationshipStateList);
                        onboardingInfoList.add(new OnboardingInfo(institution, userBinding.getProducts().stream().collect(Collectors.toMap(OnboardedProduct::getProductId, Function.identity(), (x, y) -> y))));
                    }));

            if (onboardingInfoList.isEmpty()) {
                throw new InvalidRequestException(CustomError.ONBOARDING_INFO_ERROR.getMessage(), CustomError.ONBOARDING_INFO_ERROR.getCode());
            }
            return onboardingInfoList;
        } catch (ResourceNotFoundException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {

        institutionStrategyFactory
                .retrieveOnboardingInstitutionStrategy(request.getInstitutionUpdate().getInstitutionType())
                .onboardingInstitution(request, principal);
    }

    @Override
    public void onboardingInstitutionComplete(OnboardingRequest request, SelfCareUser principal) {

        institutionStrategyFactory
                .retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(request.getInstitutionUpdate().getInstitutionType())
                .onboardingInstitution(request, principal);
    }

    @Override
    public void completeOboarding(Token token, MultipartFile contract) {
        log.trace("completeOnboarding start");
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "completeOboarding token = {} contract = {}", token, contract);
        checkAndHandleExpiring(token);
        List<String> managerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);
        List<User> managersData = managerList.stream()
                .map(user -> userService.retrieveUserFromUserRegistry(user, EnumSet.allOf(User.Fields.class)))
                .collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        Product product = onboardingDao.getProductById(token.getProductId());
        if (pagoPaSignatureConfig.isVerifyEnabled()) {
            contractService.verifySignature(contract, token, managersData);
        }
        File logoFile = contractService.getLogoFile();
        String fileName = contractService.uploadContract(token.getId(), contract);
        token.setContractSigned(fileName);
        token.setContentType(contract.getContentType());
        OnboardingUpdateRollback rollback = onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, null);
        try {
            emailService.sendCompletedEmail(managersData, institution, product, logoFile);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate(rollback.getUserList(), rollback.getUpdatedInstitution(), rollback.getToken());
            contractService.deleteContract(fileName, token.getId());
        }
        contractService.sendDataLakeNotification(rollback.getUpdatedInstitution(), token, QueueEvent.ADD);
        log.trace("completeOboarding end");
    }

    @Override
    public void approveOnboarding(Token token, SelfCareUser selfCareUser) {

        checkAndHandleExpiring(token);
        User currentUser = userService.retrieveUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));

        List<OnboardedUser> onboardedUsers = userService.findAllByIds(token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList()));

        List<String> validManagerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);
        User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));
        List<User> delegate = onboardedUsers
                .stream()
                .filter(onboardedUser -> !validManagerList.contains(onboardedUser.getId()))
                .map(onboardedUser -> userService.retrieveUserFromUserRegistry(onboardedUser.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(token, institution);
        InstitutionType institutionType = request.getInstitutionUpdate().getInstitutionType();
        Product product = onboardingDao.getProductById(token.getProductId());
        String contractTemplate = contractService.extractTemplate(product.getContractTemplatePath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null, institutionType);
        String digest = TokenUtils.createDigest(pdf);
        log.info("Digest {}", digest);
        onboardingDao.persistForUpdate(token, institution, RelationshipState.PENDING, digest);
        try {
            emailService.sendMail(pdf, institution, currentUser, request, token.getId(), true, institutionType);
        } catch (Exception e) {
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
            emailService.sendRejectMail(logo, institution, product);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate((token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList())), institution, token);
        }
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest onboardingOperatorRequest, PartyRole role) {
        OnboardingInstitutionUtils.verifyUsers(onboardingOperatorRequest.getUsers(), List.of(role));
        Institution institution = institutionService.retrieveInstitutionById(onboardingOperatorRequest.getInstitutionId());
        return onboardingDao.onboardOperator(onboardingOperatorRequest, institution);
    }

    @Override
    public void onboardingLegals(OnboardingLegalsRequest onboardingLegalsRequest, SelfCareUser selfCareUser, Token token) {
        Institution institution = institutionService.retrieveInstitutionById(onboardingLegalsRequest.getInstitutionId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(onboardingLegalsRequest);
        InstitutionType institutionType = institution.getInstitutionType();
        request.setTokenType(TokenType.LEGALS);

        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        User user = userService.retrieveUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));
        OnboardingInstitutionUtils.verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
        List<String> validManagerList = OnboardingInstitutionUtils.getValidManagerToOnboard(request.getUsers(), token);
        User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

        List<User> delegate = request.getUsers().stream()
                .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                .map(userToOnboard -> userService.retrieveUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class)))
                .collect(Collectors.toList());

        String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null, institutionType);
        String digest = createDigest(pdf);
        OnboardingRollback rollback = onboardingDao.persistLegals(toUpdate, toDelete, request, institution, digest);
        log.info("{} - Digest {}", rollback.getToken().getId(), digest);
        try {
            emailService.sendMail(pdf, institution, user, request, rollback.getToken().getId(), false, institutionType);
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

    private UserInstitutionAggregation getUserInstitutionAggregation(String userId) {
        UserInstitutionAggregation userInstitutionAggregation = userService.findUserInstitutionAggregation(userId);
        if (userInstitutionAggregation == null) {
            throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), userId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
        }
        return userInstitutionAggregation;
    }

    public void checkAndHandleExpiring(Token token) {
        var now = OffsetDateTime.now();
        if (TokenUtils.isTokenExpired(token, now)) {
            log.info("token {} is expired at {} and now is {}", token.getId(), token.getExpiringDate(), now);
            var institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
            throw new InvalidRequestException(String.format(CustomError.TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), CustomError.TOKEN_EXPIRED.getCode());
        }
    }

}
