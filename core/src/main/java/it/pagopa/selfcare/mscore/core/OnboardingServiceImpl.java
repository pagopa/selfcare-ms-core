package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PecNotificationConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.VerifyOnboardingFilters;
import it.pagopa.selfcare.mscore.model.pecnotification.PecNotification;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static it.pagopa.selfcare.mscore.constant.GenericError.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {
    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final InstitutionConnector institutionConnector;
    private final PecNotificationConnector pecNotificationConnector;
    private Integer sendingFrequencyPecNotification;
    private String epochDatePecNotification;
    private LocalDate currentDate = LocalDate.now();

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 InstitutionConnector institutionConnector,
                                 PecNotificationConnector pecNotificationConnector,
                                 @Value("${mscore.sending-frequency-pec-notification}") Integer sendingFrequencyPecNotification,
                                 @Value("${mscore.epoch-date-pec-notification}") String epochDatePecNotification) {

        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.institutionConnector = institutionConnector;
        this.pecNotificationConnector = pecNotificationConnector;
        this.sendingFrequencyPecNotification = sendingFrequencyPecNotification;
        this.epochDatePecNotification = epochDatePecNotification;
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

    public void insertPecNotification(String institutionId, String productId, String digitalAddress) {

        PecNotification pecNotification = new PecNotification();
        pecNotification.setId(UUID.randomUUID().toString());
        pecNotification.setCreatedAt(OffsetDateTime.now());
        pecNotification.setProductId(productId);
        pecNotification.setInstitutionId(institutionId);
        pecNotification.setModuleDayOfTheEpoch(calculateModuleDayOfTheEpoch());
        pecNotification.setDigitalAddress(digitalAddress);

        if (!pecNotificationConnector.insertPecNotification(pecNotification)){
            throw new InvalidRequestException(INVALID_INSERT_PEC_NOTIFICATION_ERROR.getMessage(), INVALID_INSERT_PEC_NOTIFICATION_ERROR.getCode());
        }

    }

    public int calculateModuleDayOfTheEpoch() {
        LocalDate epochStart = LocalDate.parse(this.epochDatePecNotification);
        long daysDiff = ChronoUnit.DAYS.between(epochStart, this.currentDate);
        int moduleDayOfTheEpoch = (int) (daysDiff % this.sendingFrequencyPecNotification);
        return moduleDayOfTheEpoch;
    }

    @Override
    public Institution persistOnboarding(String institutionId, String
            productId, Onboarding onboarding, StringBuilder httpStatus) {

        Institution institution = persistAndGetInstitution(institutionId, productId, onboarding, httpStatus);
        this.insertPecNotification(institutionId, productId, institution.getDigitalAddress());
        return institution;
    }

    private Institution persistAndGetInstitution(String institutionId, String productId, Onboarding onboarding, StringBuilder httpStatus) {
        log.trace("persistForUpdate start");
        log.debug("persistForUpdate institutionId = {}, productId = {}", institutionId, productId);
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

        	httpStatus.append(HttpStatus.OK.value());
            return institution;
        }

        try {
            //If not exists, persist a new onboarding for product
            final Institution institutionUpdated = institutionConnector.findAndUpdate(institutionId, onboarding, List.of(), null);

            log.trace("persistForUpdate end");
            httpStatus.append(HttpStatus.CREATED.value());
            return institutionUpdated;
        } catch (Exception e) {
            onboardingDao.rollbackPersistOnboarding(institutionId, onboarding);
            log.info("rollbackPersistOnboarding completed for institution {} and product {}", institutionId, productId);
            throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage() + " " + e.getMessage(),
                    ONBOARDING_OPERATION_ERROR.getCode());
        }
    }

    @Override
    public void deleteOnboardedInstitution(String institutionId, String productId) {

        log.trace("persist logic delete Onboarding - start");
        institutionConnector.findAndDeleteOnboarding(institutionId, productId);
        log.trace("persist logic delete Onboarding - end");

        log.trace("persist delete PecNotification - start");
        boolean isDeleted = pecNotificationConnector.findAndDeletePecNotification(institutionId, productId);
        if (!isDeleted) {
            throw new InvalidRequestException(DELETE_NOTIFICATION_OPERATION_ERROR.getMessage(),
                    ONBOARDING_OPERATION_ERROR.getCode());
        }
        log.trace("persist delete PecNotification - end");

    }

}
