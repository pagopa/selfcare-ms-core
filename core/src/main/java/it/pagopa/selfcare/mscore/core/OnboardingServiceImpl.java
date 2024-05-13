package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.onboarding.VerifyOnboardingFilters;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {
    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final ContractEventNotificationService contractEventNotification;
    private final InstitutionConnector institutionConnector;
    private final TokenMapper tokenMapper;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 ContractEventNotificationService contractEventNotification,
                                 InstitutionConnector institutionConnector,
                                 TokenMapper tokenMapper) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.contractEventNotification = contractEventNotification;
        this.institutionConnector = institutionConnector;
        this.tokenMapper = tokenMapper;
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
            Token token = tokenMapper.toToken(onboarding, institutionId, productId);
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

}
