package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
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
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.DOCUMENT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.CustomError.ONBOARDING_INFO_INSTITUTION_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {
    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserService userService;
    private final UserRelationshipService userRelationshipService;
    private final ContractService contractService;
    private final ContractEventNotificationService contractEventNotification;
    private final InstitutionConnector institutionConnector;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 UserRelationshipService userRelationshipService,
                                 ContractService contractService,
                                 ContractEventNotificationService contractEventNotification,
                                 InstitutionConnector institutionConnector) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.userRelationshipService = userRelationshipService;
        this.contractService = contractService;
        this.contractEventNotification = contractEventNotification;
        this.institutionConnector = institutionConnector;
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
    public void verifyOnboardingInfoByFilters(VerifyOnboardingFilters filters) {
        filters.setValidRelationshipStates(UtilEnumList.VALID_RELATIONSHIP_STATES);

        Boolean existsOnboardingValid = institutionConnector.existsOnboardingByFilters(filters);
        if (Boolean.FALSE.equals(existsOnboardingValid)) {
            throw new ResourceNotFoundException(CustomError.INSTITUTION_NOT_ONBOARDED_BY_FILTERS.getMessage(),
                    CustomError.INSTITUTION_NOT_ONBOARDED_BY_FILTERS.getCode());
        }
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

            log.trace("persistForUpdate end");

            //Prepare data for sending to queue ScContract and ScUsers using method exists
            //using Token pojo as temporary solution, these methods will be refactored or moved as CDC of institution
            //https://pagopa.atlassian.net/browse/SELC-3571
            Token token = TokenMapper.toToken(onboarding, institutionId, productId);
            token.setUsers(users.stream().map(this::toTokenUser).toList());
            institution.setOnboarding(List.of(onboarding));
            contractEventNotification.sendDataLakeNotification(institution, token, QueueEvent.ADD);

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
