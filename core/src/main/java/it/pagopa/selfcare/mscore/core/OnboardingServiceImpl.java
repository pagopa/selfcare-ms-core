package it.pagopa.selfcare.mscore.core;

import feign.FeignException;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.config.MailTemplateConfig;
import it.pagopa.selfcare.mscore.config.PagoPaSignatureConfig;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
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
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.constant.CustomError.CONTRACT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.TokenUtils.createDigest;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {
    protected static final String REQUIRED_ADDITIONAL_INFORMATIONS_MESSAGE = "AdditionalInformations is required";
    protected static final String REQUIRED_OTHER_NOTE_MESSAGE = "Other note is required";

    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserService userService;
    private final UserRelationshipService userRelationshipService;
    private final UserEventService userEventService;
    private final ContractService contractService;
    private final ContractEventNotificationService contractEventNotification;
    private final MailNotificationService notificationService;
    private final UserNotificationService userNotificationService;
    private final InstitutionConnector institutionConnector;
    private final ProductService productService;

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        institutionService.retrieveInstitutionsWithFilter(externalId, productId, UtilEnumList.VALID_RELATIONSHIP_STATES);
    }

    @Override
    public void verifyOnboardingInfoSubunit(String taxCode, String subunitCode, String productId) {
        Boolean existsOnboardingValid = institutionConnector.existsByTaxCodeAndSubunitCodeAndProductAndStatusList(taxCode,
                subunitCode, Optional.ofNullable(productId), UtilEnumList.VALID_RELATIONSHIP_STATES);
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
    public Institution persistOnboarding(String institutionId, String productId,List<UserToOnboard> users, Onboarding onboarding) {

        log.trace("persistForUpdate start");
        log.debug("persistForUpdate institutionId = {}, productId = {}, users = {}", institutionId, productId, users);
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setProductId(productId);
        onboarding.setCreatedAt(OffsetDateTime.now());

        //Verify if onboarding exists, in case onboarding must fail
        final Institution institution = institutionConnector.findById(institutionId);

        if(Optional.ofNullable(institution.getOnboarding()).flatMap(onboardings -> onboardings.stream()
                .filter(item -> item.getProductId().equals(productId) && UtilEnumList.VALID_RELATIONSHIP_STATES.contains(item.getStatus()))
                .findAny()).isPresent()){
            throw new InvalidRequestException(String.format(CustomError.PRODUCT_ALREADY_ONBOARDED.getMessage(), institution.getTaxCode(), productId),
                        CustomError.PRODUCT_ALREADY_ONBOARDED.getCode());
        }

        try {
            //If not exists, persist a new onboarding for product
            final Institution institutionUpdated = institutionConnector.findAndUpdate(institutionId, onboarding, List.of(), null);


            //fillUserIdAndCreateIfNotExist is for adding mail institution to pdv because user already exists there
            users.forEach(userToOnboard -> fillUserIdAndCreateIfNotExist(userToOnboard, institutionId));

            //Add users to onboarding adding to collection users
            onboardingDao.onboardOperator(institution, productId, users);

            log.trace("persistForUpdate end");

            //Prepare data for sending to queue ScContract and ScUsers using method exists
            //using Token pojo as temporary solution, these methods will be refactored or moved as CDC of institution
            //https://pagopa.atlassian.net/browse/SELC-3571
            Token token = new Token();
            token.setId(onboarding.getTokenId());
            token.setInstitutionId(institutionId);
            token.setProductId(productId);
            token.setUsers(users.stream().map(this::toTokenUser).toList());
            token.setCreatedAt(onboarding.getCreatedAt());
            token.setUpdatedAt(onboarding.getUpdatedAt());
            token.setStatus(onboarding.getStatus());
            token.setContractSigned(onboarding.getContract());
            institution.setOnboarding(List.of(onboarding));
            contractEventNotification.sendDataLakeNotification(institution, token, QueueEvent.ADD);
            userEventService.sendLegalTokenUserNotification(token);

            return institutionUpdated;
        } catch (Exception e) {
            onboardingDao.rollbackPersistOnboarding(institutionId, onboarding, users);
            log.info("rollbackPersistOnboarding completed for institution {} and product {}", institutionId, productId);
            throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage() + " " + e.getMessage(),
                    ONBOARDING_OPERATION_ERROR.getCode());
        }
    }

    private TokenUser toTokenUser(UserToOnboard user) {
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(user.getId());
        tokenUser.setRole(user.getRole());
        return tokenUser;
    }

    @Override
    public void invalidateOnboarding(Token token) {
        checkAndHandleExpiring(token);
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
    }

    @Override
    public List<RelationshipInfo> onboardingUsers(OnboardingUsersRequest request, String loggedUserName, String loggedUserSurname) {


        Product product = Optional.ofNullable(productService.getProductIsValid(request.getProductId()))
                .orElseThrow(() -> new InvalidRequestException("Product not found or is not valid!", ""));

        Institution institution = institutionService.getInstitutions(request.getInstitutionTaxCode(), request.getInstitutionSubunitCode()).stream()
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Institution not found!", ""));

        List<String> roleLabels = request.getUsers().stream()
                .map(UserToOnboard::getRoleLabel).collect(Collectors.toList());

        request.getUsers().forEach(userToOnboard -> fillUserIdAndCreateIfNotExist(userToOnboard, institution.getId()));

        List<RelationshipInfo> relationshipInfoList = onboardingDao.onboardOperator(institution, request.getProductId(), request.getUsers());

        if (request.getSendCreateUserNotificationEmail()) {
            request.getUsers().forEach(userToOnboard -> userNotificationService.sendCreateUserNotification(institution.getDescription(),
                    product.getTitle(), userToOnboard.getEmail(), roleLabels, loggedUserName, loggedUserSurname));
        }

        relationshipInfoList.forEach(relationshipInfo -> userEventService.sendOperatorUserNotification(relationshipInfo, QueueEvent.ADD));

        return relationshipInfoList;
    }

    private void fillUserIdAndCreateIfNotExist(UserToOnboard user, String institutionId) {
        User userRegistry;
        try {
            userRegistry = userService.retrieveUserFromUserRegistryByFiscalCode(user.getTaxCode());

            //We must save mail institution if it is not found on WorkContracts
            if(Objects.nonNull(user.getEmail()) &&
                    (Objects.isNull(userRegistry.getWorkContacts()) || !userRegistry.getWorkContacts().containsKey(institutionId))) {
                userRegistry = userService.persistWorksContractToUserRegistry(user.getTaxCode(), user.getEmail(), institutionId);
            }
        } catch (FeignException.NotFound e) {
            userRegistry = userService.persistUserRegistry(user.getName(), user.getSurname(), user.getTaxCode(), user.getEmail(), institutionId);
        }
        user.setId(userRegistry.getId());
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest onboardingOperatorRequest, PartyRole role, String loggedUserName, String loggedUserSurname) {
        OnboardingInstitutionUtils.verifyUsers(onboardingOperatorRequest.getUsers(), List.of(role));
        Institution institution = institutionService.retrieveInstitutionById(onboardingOperatorRequest.getInstitutionId());

        // Verify if exists an onboarding ACTIVE for productId
        Optional.ofNullable(institution.getOnboarding())
                .flatMap(onboardings -> onboardings.stream()
                        .filter(onboarding -> RelationshipState.ACTIVE.equals(onboarding.getStatus()) && onboardingOperatorRequest.getProductId().equals(onboarding.getProductId()))
                        .findAny())
                .orElseThrow(() -> new InvalidRequestException(String.format(CONTRACT_NOT_FOUND.getMessage(), institution.getId(), onboardingOperatorRequest.getProductId()), CONTRACT_NOT_FOUND.getCode()));

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
    public void onboardingLegals(OnboardingLegalsRequest onboardingLegalsRequest, SelfCareUser selfCareUser) {
        Institution institution = institutionService.retrieveInstitutionById(onboardingLegalsRequest.getInstitutionId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(onboardingLegalsRequest);
        InstitutionType institutionType = institution.getInstitutionType();
        request.setTokenType(TokenType.LEGALS);

        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        User user = userService.retrieveUserFromUserRegistry(selfCareUser.getId());
        OnboardingInstitutionUtils.verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
        List<String> validManagerList = OnboardingInstitutionUtils.getValidManagerToOnboard(request.getUsers(), null);
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
