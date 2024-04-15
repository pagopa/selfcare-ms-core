package it.pagopa.selfcare.mscore.core;

import feign.FeignException;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoTypeVerifier;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionFilter;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {
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
    private final ProductConnector productConnector;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 UserRelationshipService userRelationshipService,
                                 ContractService contractService,
                                 UserEventService userEventService,
                                 ContractEventNotificationService contractEventNotification, MailNotificationService notificationService,
                                 UserNotificationService userNotificationService,
                                 InstitutionConnector institutionConnector,
                                 ProductConnector productConnector) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.userRelationshipService = userRelationshipService;
        this.userEventService = userEventService;
        this.contractService = contractService;
        this.contractEventNotification = contractEventNotification;
        this.notificationService = notificationService;
        this.userNotificationService = userNotificationService;
        this.institutionConnector = institutionConnector;
        this.productConnector = productConnector;
    }

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
    public void verifyOnboardingInfoOrigin(String productId, String origin, String originId) {
        Boolean existsOnboardingValid = institutionConnector.existsByOrigin(productId, origin, originId, UtilEnumList.VALID_RELATIONSHIP_STATES);
        if (Boolean.FALSE.equals(existsOnboardingValid)) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_ONBOARDED_FOR_ORIGIN.getMessage(), origin, originId, productId),
                    CustomError.INSTITUTION_NOT_ONBOARDED_FOR_ORIGIN.getCode());
        }
    }

    @Override
    public void verifyOnboardingInfoByFilters(String productId, String externalId, String taxCode, String origin, String originId, String subunitCode) {
        OnboardingInfoTypeVerifier onboardingInfoTypeVerifier = this.inferTypeVerifierFromGivenFilters(externalId, taxCode, origin, originId, subunitCode);
        switch (onboardingInfoTypeVerifier) {
            case EXTERNAL_ID_VERIFIER:
                verifyOnboardingInfo(externalId, productId);
                break;
            case TAX_CODE_AND_SUBUNIT_VERIFIER:
                verifyOnboardingInfoSubunit(taxCode, subunitCode, productId);
                break;
            case ORIGIN_VERIFIER:
                verifyOnboardingInfoOrigin(productId, origin, originId);
                break;
        }
    }

    private OnboardingInfoTypeVerifier inferTypeVerifierFromGivenFilters(String externalId, String taxCode, String origin, String originId, String subunitCode) {
        if (StringUtils.hasText(externalId) && !StringUtils.hasText(taxCode) && !StringUtils.hasText(origin) && !StringUtils.hasText(originId) && !StringUtils.hasText(subunitCode)) {
            return OnboardingInfoTypeVerifier.EXTERNAL_ID_VERIFIER;
        } else if (StringUtils.hasText(taxCode) && !StringUtils.hasText(origin) && !StringUtils.hasText(originId) && !StringUtils.hasText(externalId)) {
            return OnboardingInfoTypeVerifier.TAX_CODE_AND_SUBUNIT_VERIFIER;
        } else if (StringUtils.hasText(origin) && StringUtils.hasText(originId) && !StringUtils.hasText(taxCode) && !StringUtils.hasText(subunitCode) && !StringUtils.hasText(externalId)) {
            return OnboardingInfoTypeVerifier.ORIGIN_VERIFIER;
        }
        throw new InvalidRequestException(CustomError.ONBOARDING_INFO_FILTERS_ERROR.getMessage(), CustomError.ONBOARDING_INFO_FILTERS_ERROR.getCode());
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[]
            states, String userId) {

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
    public Institution persistOnboarding(String institutionId, String
            productId, List<UserToOnboard> users, Onboarding onboarding) {

        log.trace("persistForUpdate start");
        log.debug("persistForUpdate institutionId = {}, productId = {}, users = {}", institutionId, productId, users);
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setProductId(productId);

        if (Objects.isNull(onboarding.getCreatedAt())) {
            onboarding.setCreatedAt(OffsetDateTime.now());
        }

        //Verify if onboarding exists, in case onboarding must fail
        final Institution institution = institutionConnector.findById(institutionId);

        if (Optional.ofNullable(institution.getOnboarding()).flatMap(onboardings -> onboardings.stream()
                .filter(item -> item.getProductId().equals(productId) && UtilEnumList.VALID_RELATIONSHIP_STATES.contains(item.getStatus()))
                .findAny()).isPresent()) {
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
    public List<RelationshipInfo> onboardingUsers(OnboardingUsersRequest request, String loggedUserName, String
            loggedUserSurname) {

        Institution institution = institutionService.getInstitutions(request.getInstitutionTaxCode(), request.getInstitutionSubunitCode()).stream()
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Institution not found!", ""));

        Product product = Optional.ofNullable(productConnector.getProductValidById(request.getProductId()))
                .orElseThrow(() -> new InvalidRequestException("Product not found or is not valid!", ""));

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
            if (Objects.nonNull(user.getEmail()) &&
                    (Objects.isNull(userRegistry.getWorkContacts()) || !userRegistry.getWorkContacts().containsKey(institutionId))) {
                userRegistry = userService.persistWorksContractToUserRegistry(user.getTaxCode(), user.getEmail(), institutionId);
            }
        } catch (FeignException.NotFound e) {
            userRegistry = userService.persistUserRegistry(user.getName(), user.getSurname(), user.getTaxCode(), user.getEmail(), institutionId);
        }
        user.setId(userRegistry.getId());
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest
                                                              onboardingOperatorRequest, PartyRole role, String loggedUserName, String loggedUserSurname) {
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
    public ResourceResponse retrieveDocument(String relationshipId) {
        RelationshipInfo relationship = userRelationshipService.retrieveRelationship(relationshipId);
        if (relationship.getOnboardedProduct() != null &&
                StringUtils.hasText(relationship.getOnboardedProduct().getContract())) {
            return contractService.getFile(relationship.getOnboardedProduct().getContract());
        } else {
            throw new ResourceNotFoundException(String.format(DOCUMENT_NOT_FOUND.getMessage(), relationshipId), DOCUMENT_NOT_FOUND.getCode());
        }
    }

    private List<UserInstitutionAggregation> getUserInstitutionAggregation(String userId, String
            institutionId, String externalId, List<RelationshipState> relationshipStates) {
        List<String> states = relationshipStates.stream().map(Enum::name).collect(Collectors.toList());
        UserInstitutionFilter filter = new UserInstitutionFilter(userId, institutionId, externalId, states);
        List<UserInstitutionAggregation> userInstitutionAggregation = userService.findUserInstitutionAggregation(filter);
        if (userInstitutionAggregation == null || userInstitutionAggregation.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), userId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
        }
        return userInstitutionAggregation;
    }

}
