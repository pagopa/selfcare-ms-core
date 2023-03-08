package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.UtilEnumList;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.onboarding.*;
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

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.constant.CustomError.DOCUMENT_NOT_FOUND;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

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
        OnboardingInstitutionUtils.validateOverridingData(request.getInstitutionUpdate(), institution);
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);

        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        if (InstitutionType.PG == institution.getInstitutionType()) {
            if (request.getUsers().size() == 1) {
                verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER));
            } else {
                throw new InvalidRequestException(CustomError.USERS_SIZE_NOT_ADMITTED.getMessage(), CustomError.USERS_SIZE_NOT_ADMITTED.getCode());
            }
            onboardingDao.persist(toUpdate, toDelete, request, institution, geographicTaxonomies, null);
        } else {
            User user = userService.getUserFromUserRegistry(principal.getId(), EnumSet.allOf(User.Fields.class));
            verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
            List<String> validManagerList = getValidManagerToOnboard(request.getUsers(), null);
            User manager = userService.getUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

            List<User> delegate = request.getUsers()
                    .stream()
                    .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                    .map(userToOnboard -> userService.getUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

            String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
            File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, geographicTaxonomies);
            String digest = createDigest(pdf);
            OnboardingRollback rollback = onboardingDao.persist(toUpdate, toDelete, request, institution, geographicTaxonomies, digest);
            log.info("{} - Digest {}", rollback.getTokenId(), digest);
            try {
                emailService.sendMail(pdf, institution, user, request, false);
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(toUpdate, toDelete, institution.getId(), rollback.getTokenId(), rollback.getOnboarding(), rollback.getProductMap());
            }
        }
    }

    @Override
    public void completeOboarding(Token token, MultipartFile contract) {
        checkAndHandleExpiring(token);
        var ids = token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList());
        List<OnboardedUser> onboardedUsers = userService.findAllByIds(ids);
        List<String> managerList = getOnboardedValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
        List<User> managersData = managerList
                .stream()
                .map(user -> userService.getUserFromUserRegistry(user, EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        Product product = onboardingDao.getProductById(token.getProductId());
        contractService.verifySignature(contract, token, managersData);
        File logoFile = contractService.getLogoFile();
        emailService.sendCompletedEmail(contract, token, managersData, institution, product, logoFile);
        onboardingDao.persistForUpdate(token, institution, RelationshipState.ACTIVE, null);
    }

    @Override
    public void approveOnboarding(Token token, SelfCareUser selfCareUser) {
        checkAndHandleExpiring(token);
        User currentUser = userService.getUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));

        List<OnboardedUser> onboardedUsers = userService.findAllByIds(token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList()));

        List<String> validManagerList = getOnboardedValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
        User manager = userService.getUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));
        List<User> delegate = onboardedUsers
                .stream()
                .filter(onboardedUser -> validManagerList.contains(onboardedUser.getId()))
                .map(onboardedUser -> userService.getUserFromUserRegistry(onboardedUser.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        OnboardingRequest request = constructOnboardingRequest(token, institution);
        Product product = onboardingDao.getProductById(token.getProductId());
        String contractTemplate = contractService.extractTemplate(product.getContractTemplatePath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null);
        String digest = createDigest(pdf);
        log.info("Digest {}", digest);
        onboardingDao.persistForUpdate(token, institution, RelationshipState.PENDING, digest);
        try {
            emailService.sendMail(pdf, institution, currentUser, request, true);
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
        }catch (Exception e){
            onboardingDao.rollbackSecondStepOfUpdate((token.getUsers().stream().map(TokenUser::getUserId).collect(Collectors.toList())), institution, token);
        }
    }


    private void invalidateToken(Token token, Institution institution) {
        log.info("START - invalidate token {}", token.getId());
        onboardingDao.persistForUpdate(token, institution, RelationshipState.REJECTED, null);
    }

    private OnboardedUser getUser(String userId) {
        log.info("START - getUser with id: {}", userId);
        OnboardedUser user = userService.findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
        }
        return user;
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
        if (request.getInstitutionUpdate().getGeographicTaxonomyCodes() != null &&
                !request.getInstitutionUpdate().getGeographicTaxonomyCodes().isEmpty()) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomyCodes().stream
                    ().map(institutionService::getGeoTaxonomies).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty()) {
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomyCodes()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
            }
        }
        return geographicTaxonomies;
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest onboardingOperatorRequest, PartyRole role) {
        verifyUsers(onboardingOperatorRequest.getUsers(), List.of(role));
        Institution institution = institutionService.retrieveInstitutionById(onboardingOperatorRequest.getInstitutionId());
        return onboardingDao.onboardOperator(onboardingOperatorRequest, institution);
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

    @Override
    public void onboardingLegals(OnboardingLegalsRequest onboardingLegalsRequest, SelfCareUser selfCareUser, Token token) {
        Institution institution = institutionService.retrieveInstitutionById(onboardingLegalsRequest.getInstitutionId());
        OnboardingRequest request = toOnboardingRequest(onboardingLegalsRequest);
        request.setTokenType(TokenType.INSTITUTION);

        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        User user = userService.getUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));
        verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
        List<String> validManagerList = getValidManagerToOnboard(request.getUsers(), token);
        User manager = userService.getUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

        List<User> delegate = request.getUsers()
                .stream()
                .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                .map(userToOnboard -> userService.getUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

        String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
        File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, null);
        String digest = createDigest(pdf);
        OnboardingRollback rollback = onboardingDao.persistLegals(toUpdate, toDelete, request, institution, digest);
        log.info("{} - Digest {}", rollback.getTokenId(), digest);
        try {
            emailService.sendMail(pdf, institution, user, request, false);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStep(toUpdate, toDelete, institution.getId(), rollback.getTokenId(), rollback.getOnboarding(), rollback.getProductMap());
        }
    }

    private OnboardingRequest toOnboardingRequest(OnboardingLegalsRequest onboardingLegalsRequest) {
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId(onboardingLegalsRequest.getProductId());
        request.setProductName(onboardingLegalsRequest.getProductName());
        request.setUsers(onboardingLegalsRequest.getUsers());
        request.setInstitutionExternalId(onboardingLegalsRequest.getInstitutionExternalId());
        request.setContract(onboardingLegalsRequest.getContract());
        request.setSignContract(true);
        request.setTokenType(TokenType.LEGALS);
        return request;
    }

    public void checkAndHandleExpiring(Token token) {
        var now = OffsetDateTime.now();
        if (token.getExpiringDate() != null && (now.isEqual(token.getExpiringDate()) || now.isAfter(token.getExpiringDate()))) {
            log.info("token {} is expired at {} and now is {}", token.getId(), token.getExpiringDate(), now);
            var institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
            onboardingDao.persistForUpdate(token, institution, RelationshipState.DELETED, null);
            throw new InvalidRequestException(String.format(TOKEN_EXPIRED.getMessage(), token.getId(), token.getExpiringDate()), TOKEN_EXPIRED.getCode());
        }
    }

}
