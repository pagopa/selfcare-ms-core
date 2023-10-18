package it.pagopa.selfcare.mscore.core.strategy.factory;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.FileStorageConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.*;
import it.pagopa.selfcare.mscore.core.strategy.OnboardingInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.input.OnboardingInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRollback;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.ProductId.*;

@Component
@Slf4j
public class OnboardingInstitutionStrategyFactory {


    private final OnboardingDao onboardingDao;
    private final ContractService contractService;
    private final UserService userService;
    private final InstitutionService institutionService;
    private final CoreConfig coreConfig;
    private final FileStorageConnector fileStorageConnector;

    private final NotificationService notificationService;

    public OnboardingInstitutionStrategyFactory(OnboardingDao onboardingDao,
                                                ContractService contractService,
                                                UserService userService,
                                                InstitutionService institutionService,
                                                CoreConfig coreConfig,
                                                NotificationService notificationService, FileStorageConnector fileStorageConnector) {
        this.onboardingDao = onboardingDao;
        this.contractService = contractService;
        this.userService = userService;
        this.institutionService = institutionService;
        this.coreConfig = coreConfig;
        this.notificationService = notificationService;
        this.fileStorageConnector = fileStorageConnector;
    }

    public OnboardingInstitutionStrategy retrieveOnboardingInstitutionStrategy(InstitutionType institutionType, String productId, Institution institution) {
        Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy =
                verifyAndFillInstitutionAttributeStrategy(institution);

        Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy;
        Consumer<OnboardingInstitutionStrategyInput> persitOnboardingInstitutionStrategy;
        Consumer<OnboardingInstitutionStrategyInput> emailsOnboardingInstitutionStrategy;

        if (InstitutionType.PG == institutionType) {
            digestOnboardingInstitutionStrategy = ignore -> {};
            persitOnboardingInstitutionStrategy = verifyManagerAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = sendConfirmationMail();
        } else if (InstitutionType.PA == institutionType
                || checkIfGspProdInteropAndOriginIPA(institutionType, productId, institution.getOrigin())
                || InstitutionType.SA == institutionType
                || InstitutionType.AS == institutionType) {
            digestOnboardingInstitutionStrategy = createContractAndPerformDigest();
            persitOnboardingInstitutionStrategy = verifyManagerAndDelegateAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = sendEmailWithDigestOrRollback();
        } else {
            digestOnboardingInstitutionStrategy = ignore -> {};
            persitOnboardingInstitutionStrategy = verifyManagerAndDelegateAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = sendEmailWithoutDigestOrRollback();
        }

        return new OnboardingInstitutionStrategy(verifyAndFillInstitutionAttributeStrategy,
                digestOnboardingInstitutionStrategy,
                persitOnboardingInstitutionStrategy,
                emailsOnboardingInstitutionStrategy);
    }

    private boolean checkIfGspProdInteropAndOriginIPA(InstitutionType institutionType, String productId, String origin) {
        return InstitutionType.GSP == institutionType
                && productId.equals(PROD_INTEROP.getValue())
                && origin.equals(Origin.IPA.getValue());
    }

    public OnboardingInstitutionStrategy retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(InstitutionType institutionType, Institution institution) {

        Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy = verifyAndFillInstitutionAttributeStrategy(institution);
        Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy = ignore -> {
        };

        Consumer<OnboardingInstitutionStrategyInput> persitOnboardingInstitutionStrategy;
        Consumer<OnboardingInstitutionStrategyInput> emailsOnboardingInstitutionStrategy;

        if (InstitutionType.PG == institutionType) {

            persitOnboardingInstitutionStrategy = verifyManagerAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = ignore -> {};

        } else {

            persitOnboardingInstitutionStrategy = verifyManagerAndDelegateAndPersistWithContractComplete();
            emailsOnboardingInstitutionStrategy = sendEmailWithLogoOrRollback();
        }

        return new OnboardingInstitutionStrategy(verifyAndFillInstitutionAttributeStrategy,
                digestOnboardingInstitutionStrategy,
                persitOnboardingInstitutionStrategy,
                emailsOnboardingInstitutionStrategy);
    }

    private Consumer<OnboardingInstitutionStrategyInput> verifyManagerAndPersistWithDigest() {
        return strategyInput -> {

            if (strategyInput.getOnboardingRequest().getUsers().size() != 1) {
                throw new InvalidRequestException(CustomError.USERS_SIZE_NOT_ADMITTED.getMessage(), CustomError.USERS_SIZE_NOT_ADMITTED.getCode());
            }

            OnboardingInstitutionUtils.verifyUsers(strategyInput.getOnboardingRequest().getUsers(), List.of(PartyRole.MANAGER));
            OnboardingRollback onboardingRollback = onboardingDao.persist(strategyInput.getToUpdate(),
                    strategyInput.getToDelete(), strategyInput.getOnboardingRequest(), strategyInput.getInstitution(),
                    strategyInput.getInstitutionUpdateGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> verifyManagerAndDelegateAndPersistWithDigest() {
        return strategyInput -> {
            if(strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.SA)
                    || strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.PT)
                    || strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.AS)){
                OnboardingInstitutionUtils.validateOnboarding(strategyInput.getOnboardingRequest().getBillingRequest(), false);
            } else {
                OnboardingInstitutionUtils.validateOnboarding(strategyInput.getOnboardingRequest().getBillingRequest(), true);
            }
            OnboardingInstitutionUtils.verifyUsers(strategyInput.getOnboardingRequest().getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));

            OnboardingRollback onboardingRollback = onboardingDao.persist(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getOnboardingRequest(), strategyInput.getInstitution(), strategyInput.getInstitutionUpdateGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }


    private Consumer<OnboardingInstitutionStrategyInput> verifyManagerAndDelegateAndPersistWithContractComplete() {
        return strategyInput -> {
            if(strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.SA)
                    || strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.PT)
                    || strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType().equals(InstitutionType.AS)) {
                OnboardingInstitutionUtils.validateOnboarding(strategyInput.getOnboardingRequest().getBillingRequest(), false);
            } else {
                OnboardingInstitutionUtils.validateOnboarding(strategyInput.getOnboardingRequest().getBillingRequest(), true);
            }
            OnboardingInstitutionUtils.verifyUsers(strategyInput.getOnboardingRequest().getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));

            OnboardingRollback onboardingRollback = onboardingDao.persistComplete(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getOnboardingRequest(), strategyInput.getInstitution(), strategyInput.getInstitutionUpdateGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> createContractAndPerformDigest() {
        return strategyInput -> {

            String validManagerId = OnboardingInstitutionUtils.getValidManagerId(strategyInput.getOnboardingRequest().getUsers());
            User manager = userService.retrieveUserFromUserRegistry(validManagerId);

            List<User> delegates = strategyInput.getOnboardingRequest().getUsers()
                    .stream()
                    .filter(userToOnboard -> PartyRole.MANAGER != userToOnboard.getRole())
                    .map(userToOnboard -> userService.retrieveUserFromUserRegistry(userToOnboard.getId())).collect(Collectors.toList());

            String contractTemplate = contractService.extractTemplate(strategyInput.getOnboardingRequest().getContract().getPath());
            String productId = strategyInput.getOnboardingRequest().getProductId();
            File pdf = null;
            if (productId.equals(PROD_FD.getValue()) || productId.equals(PROD_FD_GARANTITO.getValue())) {
                    pdf = fileStorageConnector.getFileAsPdf(strategyInput.getOnboardingRequest().getContract().getPath());
            } else {
                pdf = contractService.createContractPDF(contractTemplate, manager, delegates, strategyInput.getInstitution(), strategyInput.getOnboardingRequest(), strategyInput.getInstitutionUpdateGeographicTaxonomies(), strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType());
            }
            String digest = TokenUtils.createDigest(pdf);

            strategyInput.setDigest(digest);
            strategyInput.setPdf(pdf);
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendConfirmationMail() {
        return strategyInput -> {
            try {
                notificationService.setCompletedPGOnboardingMail(strategyInput.getOnboardingRequest().getInstitutionUpdate().getDigitalAddress(), strategyInput.getInstitution().getDescription());
            } catch (Exception e) {
                log.warn("Error during send completed email for product: {}", PROD_PN);
            }
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendEmailWithDigestOrRollback() {
        return strategyInput -> {
            try {
                User user = userService.retrieveUserFromUserRegistry(strategyInput.getPrincipal().getId());
                notificationService.sendMailWithContract(strategyInput.getPdf(), strategyInput.getInstitution(), user, strategyInput.getOnboardingRequest(), strategyInput.getOnboardingRollback().getToken().getId(), false);
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getInstitution().getId(),
                        strategyInput.getOnboardingRollback().getToken(), strategyInput.getOnboardingRollback().getOnboarding(), strategyInput.getOnboardingRollback().getProductMap());
            }
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendEmailWithoutDigestOrRollback() {
        return strategyInput -> {
            try {
                User user = userService.retrieveUserFromUserRegistry(strategyInput.getPrincipal().getId());
                if(!InstitutionType.PT.equals(strategyInput.getOnboardingRequest().getInstitutionUpdate().getInstitutionType())) {
                    notificationService.sendMailForApprove(user, strategyInput.getOnboardingRequest(), strategyInput.getOnboardingRollback().getToken().getId());
                } else {
                    notificationService.sendMailForRegistration(user, strategyInput.getInstitution(), strategyInput.getOnboardingRequest());
                    notificationService.sendMailForRegistrationNotificationApprove(user, strategyInput.getOnboardingRequest(), strategyInput.getOnboardingRollback().getToken().getId());
                    }
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getInstitution().getId(),
                        strategyInput.getOnboardingRollback().getToken(), strategyInput.getOnboardingRollback().getOnboarding(), strategyInput.getOnboardingRollback().getProductMap());
            }
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendEmailWithLogoOrRollback() {
        return strategyInput -> {
            try {

                if(strategyInput.getOnboardingRequest().getInstitutionUpdate().isImported()) {

                    List<String> destinationMails = Objects.nonNull(coreConfig.getDestinationMails()) && !coreConfig.getDestinationMails().isEmpty()
                            ? coreConfig.getDestinationMails()
                            : List.of(strategyInput.getInstitution().getDigitalAddress());

                    File logoFile = contractService.getLogoFile();

                    notificationService.sendAutocompleteMail(destinationMails, new HashMap<>(), logoFile, NotificationServiceImpl.PAGOPA_LOGO_FILENAME, strategyInput.getOnboardingRequest().getProductName());
                }

                //[TODO https://pagopa.atlassian.net/wiki/spaces/SCP/pages/710901785/RFC+Proposta+per+gestione+asincrona+degli+eventi]
                contractService.sendDataLakeNotification(strategyInput.getOnboardingRollback().getUpdatedInstitution(), strategyInput.getOnboardingRollback().getToken(), QueueEvent.ADD);

            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getInstitution().getId(),
                        strategyInput.getOnboardingRollback().getToken(), strategyInput.getOnboardingRollback().getOnboarding(), strategyInput.getOnboardingRollback().getProductMap());
            }

        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy(Institution institution) {
        return strategyInput -> {
            strategyInput.getOnboardingRequest().setTokenType(TokenType.INSTITUTION);

            /* check onboaring validation */
            OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, strategyInput.getOnboardingRequest().getProductId());
            rejectTokenExpired(institution, strategyInput.getOnboardingRequest().getProductId());
            OnboardingInstitutionUtils.validateOverridingData(strategyInput.getOnboardingRequest().getInstitutionUpdate(), institution);

            strategyInput.setInstitution(institution);

            /* retrieve geographic taxonomies */
            List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomies =
                    retrieveGeographicTaxonomy(strategyInput.getOnboardingRequest().getInstitutionUpdate().getGeographicTaxonomies());
            strategyInput.setInstitutionUpdateGeographicTaxonomies(institutionGeographicTaxonomies);
        };
    }

    private void rejectTokenExpired(Institution institution, String productId) {
        if(Objects.nonNull(institution.getOnboarding())) {

            /* set state REJECTED for tokens expired and throw an exception if there are token not expired PENDING or TOBEVALIDATED for the product */
            institution.getOnboarding().stream()
                    .filter(onboarding -> onboarding.getProductId().equalsIgnoreCase(productId)
                            && (onboarding.getStatus() == RelationshipState.PENDING || onboarding.getStatus() == RelationshipState.TOBEVALIDATED))
                    .map(onboarding -> onboardingDao.getTokenById(onboarding.getTokenId()))
                    .filter(TokenUtils::isTokenExpired)
                        .forEach(token -> onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null));
        }
    }

    private List<InstitutionGeographicTaxonomies> retrieveGeographicTaxonomy(List<InstitutionGeographicTaxonomies> geographicTaxonomieDtos) {
        if (Objects.isNull(geographicTaxonomieDtos) || geographicTaxonomieDtos.isEmpty()) {
            return List.of();
        }

        List<InstitutionGeographicTaxonomies> geographicTaxonomies = geographicTaxonomieDtos.stream()
                    .map(InstitutionGeographicTaxonomies::getCode)
                    .map(institutionService::retrieveGeoTaxonomies)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(geo -> new InstitutionGeographicTaxonomies(geo.getGeotaxId(), geo.getDescription()))
                    .collect(Collectors.toList());

        if (geographicTaxonomies.size() != geographicTaxonomieDtos.size()) {
            log.error(String.format(CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), geographicTaxonomieDtos),
                    CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
            return List.of();
        }

        return geographicTaxonomies;
    }

}
