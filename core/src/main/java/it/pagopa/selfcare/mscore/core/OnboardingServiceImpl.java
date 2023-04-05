package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingInfo;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingLegalsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingOperatorsRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRollback;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingUpdateRollback;
import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.constant.TokenType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.DOCUMENT_NOT_FOUND;
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

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 UserRelationshipService userRelationshipService,
                                 ContractService contractService,
                                 EmailService emailService) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.userRelationshipService = userRelationshipService;
        this.contractService = contractService;
        this.emailService = emailService;
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        institutionService.retrieveInstitutionsWithFilter(externalId, productId, UtilEnumList.VALID_RELATIONSHIP_STATES);
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {

        List<RelationshipState> relationshipStateList = OnboardingInfoUtils.getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardedUser currentUser = getUser(userId);
        List<UserBinding> userBindings = OnboardingInfoUtils.getUserInstitutionsWithProductStatusIn(currentUser.getBindings(), relationshipStateList);
        if (StringUtils.hasText(institutionId) || StringUtils.hasText(institutionExternalId)) {
            Institution onboardedInstitution = findInstitutionByOptionalId(institutionId, institutionExternalId);
            UserBinding institutionUserBinding = userBindings.stream().filter(userBinding -> onboardedInstitution.getId().equalsIgnoreCase(userBinding.getInstitutionId()))
                    .findAny().orElseThrow(() -> new InvalidRequestException(CustomError.ONBOARDING_INFO_ERROR.getMessage(), CustomError.ONBOARDING_INFO_ERROR.getCode()));
            OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(institutionUserBinding, onboardedInstitution, relationshipStateList);
            Map<String, OnboardedProduct> productMap = institutionUserBinding.getProducts().stream().collect(Collectors.toMap(OnboardedProduct::getProductId, Function.identity(), (x, y) -> y));
            onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, productMap));
        } else {
            userBindings.forEach(userBinding -> {
                Institution onboardedInstitution = institutionService.retrieveInstitutionById(userBinding.getInstitutionId());
                OnboardingInfoUtils.findOnboardingLinkedToProductWithStateIn(userBinding, onboardedInstitution, relationshipStateList);
                onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, userBinding.getProducts().stream().collect(Collectors.toMap(OnboardedProduct::getProductId, Function.identity(), (x, y) -> y))));
            });
        }
        if (onboardingInfoList.isEmpty()) {
            throw new InvalidRequestException(CustomError.ONBOARDING_INFO_ERROR.getMessage(), CustomError.ONBOARDING_INFO_ERROR.getCode());
        }
        return onboardingInfoList;
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {
        request.setTokenType(TokenType.INSTITUTION);
        Institution institution = institutionService.retrieveInstitutionByExternalId(request.getInstitutionExternalId());
        OnboardingInstitutionUtils.checkIfProductAlreadyOnboarded(institution, request);
        checkIncompleteOnboarding(institution, request);
        OnboardingInstitutionUtils.validateOverridingData(request.getInstitutionUpdate(), institution);
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);
        List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomies = new ArrayList<>();
        if (!geographicTaxonomies.isEmpty()) {
            institutionGeographicTaxonomies = geographicTaxonomies.stream()
                    .map(geo -> new InstitutionGeographicTaxonomies(geo.getCode(), geo.getDesc()))
                    .collect(Collectors.toList());
        }
        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        if (InstitutionType.PG == institution.getInstitutionType()) {
            if (request.getUsers().size() == 1) {
                OnboardingInstitutionUtils.verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER));
            } else {
                throw new InvalidRequestException(CustomError.USERS_SIZE_NOT_ADMITTED.getMessage(), CustomError.USERS_SIZE_NOT_ADMITTED.getCode());
            }
            onboardingDao.persist(toUpdate, toDelete, request, institution, institutionGeographicTaxonomies, null);
        } else {
            OnboardingInstitutionUtils.validatePaOnboarding(request);

            User user = userService.retrieveUserFromUserRegistry(principal.getId(), EnumSet.allOf(User.Fields.class));
            OnboardingInstitutionUtils.verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
            List<String> validManagerList = OnboardingInstitutionUtils.getValidManagerToOnboard(request.getUsers(), null);
            User manager = userService.retrieveUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

            List<User> delegate = request.getUsers()
                    .stream()
                    .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                    .map(userToOnboard -> userService.retrieveUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

            String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
            File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, institutionGeographicTaxonomies);
            String digest = TokenUtils.createDigest(pdf);
            OnboardingRollback rollback = onboardingDao.persist(toUpdate, toDelete, request, institution, institutionGeographicTaxonomies, digest);
            log.info("{} - Digest {}", rollback.getToken().getId(), digest);
            try {
                emailService.sendMail(pdf, institution, user, request, rollback.getToken().getId(), false);
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(toUpdate, toDelete, institution.getId(), rollback.getToken(), rollback.getOnboarding(), rollback.getProductMap());
            }
        }
    }

    @Override
    public void completeOboarding(Token token, MultipartFile contract) {
        checkAndHandleExpiring(token);
        List<String> managerList = OnboardingInstitutionUtils.getOnboardedValidManager(token);
        List<User> managersData = managerList.stream()
                .map(user -> userService.retrieveUserFromUserRegistry(user, EnumSet.allOf(User.Fields.class)))
                .collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        Product product = onboardingDao.getProductById(token.getProductId());
        contractService.verifySignature(contract, token, managersData);
        File logoFile = contractService.getLogoFile();
        String fileName = contractService.uploadContract(token.getId(), contract);
        token.setContractSigned(fileName);
        OnboardingUpdateRollback rollback = onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, null);
        try {
            emailService.sendCompletedEmail(managersData, institution, product, logoFile);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate(rollback.getUserList(), rollback.getUpdatedInstitution(), rollback.getToken());
            contractService.deleteContract(fileName, token.getId());
        }
        contractService.sendDataLakeNotification(rollback.getUpdatedInstitution(), token);
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
                .filter(onboardedUser -> validManagerList.contains(onboardedUser.getId()))
                .map(onboardedUser -> userService.retrieveUserFromUserRegistry(onboardedUser.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        OnboardingRequest request = OnboardingInstitutionUtils.constructOnboardingRequest(token, institution);
        Product product = onboardingDao.getProductById(token.getProductId());
        String contractTemplate = contractService.extractTemplate(product.getContractTemplatePath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null);
        String digest = TokenUtils.createDigest(pdf);
        log.info("Digest {}", digest);
        onboardingDao.persistForUpdate(token, institution, RelationshipState.PENDING, digest);
        try {
            emailService.sendMail(pdf, institution, currentUser, request, token.getId(), true);
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
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null);
        String digest = createDigest(pdf);
        OnboardingRollback rollback = onboardingDao.persistLegals(toUpdate, toDelete, request, institution, digest);
        log.info("{} - Digest {}", rollback.getToken().getId(), digest);
        try {
            emailService.sendMail(pdf, institution, user, request, rollback.getToken().getId(), false);
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
            throw new InvalidRequestException(String.format(DOCUMENT_NOT_FOUND.getMessage(), relationshipId), DOCUMENT_NOT_FOUND.getCode());
        }
    }

    private void invalidateToken(Token token, Institution institution) {
        log.info("START - invalidate token {}", token.getId());
        onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
    }

    private OnboardedUser getUser(String userId) {
        return userService.findByUserId(userId);
    }

    private Institution findInstitutionByOptionalId(String institutionId, String institutionExternalId) {
        if (StringUtils.hasText(institutionId)) {
            return institutionService.retrieveInstitutionById(institutionId);
        } else {
            return institutionService.retrieveInstitutionByExternalId(institutionExternalId);
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

    public void checkAndHandleExpiring(Token token) {
        var now = OffsetDateTime.now();
        if (isTokenExpired(token, now)) {
            log.info("token {} is expired at {} and now is {}", token.getId(), token.getExpiringDate(), now);
            var institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, null);
            throw new InvalidRequestException(String.format(CustomError.TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), CustomError.TOKEN_EXPIRED.getCode());
        }
    }

    private boolean isTokenExpired(Token token, OffsetDateTime now) {
        return token.getExpiringDate() != null && (now.isEqual(token.getExpiringDate()) || now.isAfter(token.getExpiringDate()));
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
        for (var token : tokens) {
            if (isTokenExpired(token, now)) {
                onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, null);
            } else {
                isIncomplete = true;
            }
        }
        if (isIncomplete) {
            log.warn("institution {}, there is a pending onboarding request for product {}", institution.getId(), request.getProductId());
            throw new InvalidRequestException(String.format(CustomError.ONBOARDING_PENDING.getMessage(), request.getProductId()), CustomError.ONBOARDING_PENDING.getCode());
        }
    }
}
