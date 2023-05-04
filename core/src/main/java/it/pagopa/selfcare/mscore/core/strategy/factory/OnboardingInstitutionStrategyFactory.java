package it.pagopa.selfcare.mscore.core.strategy.factory;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.*;
import it.pagopa.selfcare.mscore.core.strategy.OnboardingInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.input.OnboardingInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRollback;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.user.User;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class OnboardingInstitutionStrategyFactory {


    private final OnboardingDao onboardingDao;
    private final ContractService contractService;
    private final UserService userService;
    private final EmailService emailService;
    private final InstitutionService institutionService;
    private final CoreConfig coreConfig;


    public OnboardingInstitutionStrategyFactory(OnboardingDao onboardingDao, ContractService contractService, UserService userService,
                                                EmailService emailService, InstitutionService institutionService, CoreConfig coreConfig) {
        this.onboardingDao = onboardingDao;
        this.contractService = contractService;
        this.userService = userService;
        this.emailService = emailService;
        this.institutionService = institutionService;
        this.coreConfig = coreConfig;
    }

    public OnboardingInstitutionStrategy retrieveOnboardingInstitutionStrategy(InstitutionType institutionType) {

        Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy =
                verifyAndFillInstitutionAttributeStrategy();

        Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy;
        Consumer<OnboardingInstitutionStrategyInput> persitOnboardingInstitutionStrategy;
        Consumer<OnboardingInstitutionStrategyInput> emailsOnboardingInstitutionStrategy;

        if (InstitutionType.PG == institutionType) {

            digestOnboardingInstitutionStrategy = ignore -> {};
            persitOnboardingInstitutionStrategy = verifyManagerAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = ignore -> {};

        } else {

            digestOnboardingInstitutionStrategy = uploadContractAndPerformDigest();
            persitOnboardingInstitutionStrategy = verifyManagerAndDelegateAndPersistWithDigest();
            emailsOnboardingInstitutionStrategy = sendEmailWithDigestOrRollback();
        }

        return new OnboardingInstitutionStrategy(verifyAndFillInstitutionAttributeStrategy,
                digestOnboardingInstitutionStrategy,
                persitOnboardingInstitutionStrategy,
                emailsOnboardingInstitutionStrategy);
    }

    public OnboardingInstitutionStrategy retrieveOnboardingInstitutionStrategyWithoutContractAndComplete(InstitutionType institutionType) {

        Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy = verifyAndFillInstitutionAttributeStrategy();
        Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy = ignore -> {};

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

            if (strategyInput.getRequest().getUsers().size() != 1) {
                throw new InvalidRequestException(CustomError.USERS_SIZE_NOT_ADMITTED.getMessage(), CustomError.USERS_SIZE_NOT_ADMITTED.getCode());
            }

            OnboardingInstitutionUtils.verifyUsers(strategyInput.getRequest().getUsers(), List.of(PartyRole.MANAGER));
            OnboardingRollback onboardingRollback = onboardingDao.persist(strategyInput.getToUpdate(),
                    strategyInput.getToDelete(), strategyInput.getRequest(), strategyInput.getInstitution(),
                    strategyInput.getInstitutionGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }




    private Consumer<OnboardingInstitutionStrategyInput> verifyManagerAndDelegateAndPersistWithDigest() {
        return strategyInput -> {

            OnboardingInstitutionUtils.validatePaOnboarding(strategyInput.getRequest());

            OnboardingInstitutionUtils.verifyUsers(strategyInput.getRequest().getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));

            OnboardingRollback onboardingRollback = onboardingDao.persist(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getRequest(), strategyInput.getInstitution(), strategyInput.getInstitutionGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }


    private Consumer<OnboardingInstitutionStrategyInput> verifyManagerAndDelegateAndPersistWithContractComplete() {
        return strategyInput -> {

            OnboardingInstitutionUtils.validatePaOnboarding(strategyInput.getRequest());

            OnboardingInstitutionUtils.verifyUsers(strategyInput.getRequest().getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));

            OnboardingRollback onboardingRollback = onboardingDao.persistComplete(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getRequest(), strategyInput.getInstitution(), strategyInput.getInstitutionGeographicTaxonomies(), strategyInput.getDigest());
            strategyInput.setOnboardingRollback(onboardingRollback);
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> uploadContractAndPerformDigest() {
        return strategyInput -> {

            List<String> validManagerList = OnboardingInstitutionUtils.getValidManagerToOnboard(strategyInput.getRequest().getUsers(), null);
            User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

            List<User> delegates = strategyInput.getRequest().getUsers()
                    .stream()
                    .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                    .map(userToOnboard -> userService.retrieveUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

            String contractTemplate = contractService.extractTemplate(strategyInput.getRequest().getContract().getPath());
            File pdf = contractService.createContractPDF(contractTemplate, manager, delegates, strategyInput.getInstitution(), strategyInput.getRequest(), strategyInput.getInstitutionGeographicTaxonomies(), strategyInput.getInstitution().getInstitutionType());
            String digest = TokenUtils.createDigest(pdf);

            strategyInput.setDigest(digest);
            strategyInput.setPdf(pdf);
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendEmailWithDigestOrRollback() {
        return strategyInput -> {
            try {
                User user = userService.retrieveUserFromUserRegistry(strategyInput.getPrincipal().getId(), EnumSet.allOf(User.Fields.class));
                emailService.sendMail(strategyInput.getPdf(), strategyInput.getInstitution(), user, strategyInput.getRequest(), strategyInput.getOnboardingRollback().getToken().getId(), false, strategyInput.getInstitution().getInstitutionType());
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getInstitution().getId(),
                        strategyInput.getOnboardingRollback().getToken(), strategyInput.getOnboardingRollback().getOnboarding(), strategyInput.getOnboardingRollback().getProductMap());
            }
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> sendEmailWithLogoOrRollback() {
        return strategyInput -> {
            try {

                if(strategyInput.getRequest().getInstitutionUpdate().isImported()) {

                    List<String> destinationMails = Objects.nonNull(coreConfig.getDestinationMails())
                            ? coreConfig.getDestinationMails()
                            : List.of(strategyInput.getInstitution().getDigitalAddress());

                    File logoFile = contractService.getLogoFile();

                    emailService.sendAutocompleteMail(destinationMails, new HashMap<>(), logoFile, EmailService.PAGOPA_LOGO_FILENAME);
                }
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(strategyInput.getToUpdate(), strategyInput.getToDelete(), strategyInput.getInstitution().getId(),
                        strategyInput.getOnboardingRollback().getToken(), strategyInput.getOnboardingRollback().getOnboarding(), strategyInput.getOnboardingRollback().getProductMap());
            }
        };
    }

    private Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy() {
        return strategyInput -> {
            strategyInput.getRequest().setTokenType(TokenType.INSTITUTION);
            Institution institution = institutionService.retrieveInstitutionByExternalId(strategyInput.getRequest().getInstitutionExternalId());
            OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, strategyInput.getRequest());
            checkIncompleteOnboarding(institution, strategyInput.getRequest());
            OnboardingInstitutionUtils.validateOverridingData(strategyInput.getRequest().getInstitutionUpdate(), institution);
            List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(strategyInput.getRequest());
            List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomies = new ArrayList<>();
            if (!geographicTaxonomies.isEmpty()) {
                institutionGeographicTaxonomies = geographicTaxonomies.stream()
                        .map(geo -> new InstitutionGeographicTaxonomies(geo.getGeotaxId(), geo.getDescription()))
                        .collect(Collectors.toList());
            }

            strategyInput.setInstitution(institution);
            strategyInput.setInstitutionGeographicTaxonomies(institutionGeographicTaxonomies);
        };
    }

    private void checkIncompleteOnboarding(Institution institution, OnboardingRequest request) {
        List<Token> tokens = new ArrayList<>();
        if (institution.getOnboarding() != null) {
            tokens = institution.getOnboarding().stream()
                    .filter(o -> o.getProductId().equalsIgnoreCase(request.getProductId())
                            && (o.getStatus() == RelationshipState.PENDING || o.getStatus() == RelationshipState.TOBEVALIDATED))
                    .map(o -> onboardingDao.getTokenById(o.getTokenId()))
                    .collect(Collectors.toList());
        }
        var now = OffsetDateTime.now();
        boolean isIncomplete = false;
        
        /* set state DELETE for tokens expired and throw an exception if there are token not expired PENDING or TOBEVALIDATED for the product */
        for (var token : tokens) {
            if (TokenUtils.isTokenExpired(token, now)) {
                onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, null);
            } else {
                isIncomplete = true;
            }
        }
        if (isIncomplete) {
            throw new InvalidRequestException(String.format(CustomError.ONBOARDING_PENDING.getMessage(), request.getProductId()), CustomError.ONBOARDING_PENDING.getCode());
        }
    }

    private List<GeographicTaxonomies> getGeographicTaxonomy(OnboardingRequest request) {
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        if (request.getInstitutionUpdate().getGeographicTaxonomies() != null &&
                !request.getInstitutionUpdate().getGeographicTaxonomies().isEmpty()) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomies().stream
                    ().map(institutionGeographicTaxonomies -> institutionService.retrieveGeoTaxonomies(institutionGeographicTaxonomies.getCode())).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty()) {
                throw new ResourceNotFoundException(String.format(CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomies()),
                        CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
            }
        }
        return geographicTaxonomies;
    }

}
