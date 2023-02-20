package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserService userService;
    private final ContractService contractService;
    private final EmailService emailService;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService,
                                 ContractService contractService,
                                 EmailService emailService) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
        this.contractService = contractService;
        this.emailService = emailService;
    }

    @Override
    public Resource getResourceByPath(String path){
        return contractService.getFile(path);
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        institutionService.retrieveInstitutionsWithFilter(externalId, productId, validRelationshipStates);
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {

        List<RelationshipState> relationshipStateList = getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardedUser currentUser = getUser(userId);
        List<UserBinding> userBindings = getUserInstitutionsWithProductStatusIn(currentUser.getBindings(), relationshipStateList);
        if (StringUtils.hasText(institutionId) || StringUtils.hasText(institutionExternalId)) {
            Institution onboardedInstitution = findInstitutionByOptionalId(institutionId, institutionExternalId);
            UserBinding institutionUserBinding = userBindings.stream().filter(userBinding -> onboardedInstitution.getId().equalsIgnoreCase(userBinding.getInstitutionId()))
                    .findAny().orElseThrow(() -> new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode()));
            findOnboardingLinkedToProductWithStateIn(institutionUserBinding, onboardedInstitution, relationshipStateList);
            Map<String, OnboardedProduct> productMap = institutionUserBinding.getProducts().stream().collect(Collectors.toMap(OnboardedProduct::getProductId, Function.identity(), (x, y) -> y));
            onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, productMap));
        } else {
            userBindings.forEach(userBinding -> {
                Institution onboardedInstitution = institutionService.retrieveInstitutionById(userBinding.getInstitutionId());
                findOnboardingLinkedToProductWithStateIn(userBinding, onboardedInstitution, relationshipStateList);
                onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, userBinding.getProducts().stream().collect(Collectors.toMap(OnboardedProduct::getProductId, Function.identity(), (x, y) -> y))));
            });
        }
        if (onboardingInfoList.isEmpty()) {
            throw new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode());
        }
        return onboardingInfoList;
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(request.getInstitutionExternalId());
        checkIfProductAlreadyOnboarded(institution, request);
        validateOverridingData(request.getInstitutionUpdate(), institution);
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);

        List<OnboardedUser> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        if (InstitutionType.PG == institution.getInstitutionType()) {
            if (request.getUsers().size() == 1) {
                verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER));
            } else {
                throw new InvalidRequestException(USERS_SIZE_NOT_ADMITTED.getMessage(), USERS_SIZE_NOT_ADMITTED.getCode());
            }
            onboardingDao.persist(toUpdate, toDelete, request, institution, geographicTaxonomies, null);
        } else {
            User user = userService.getUserFromUserRegistry(principal.getId(), EnumSet.allOf(User.Fields.class));
            verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER, PartyRole.DELEGATE));
            List<String> validManagerList = getOnboardingValidManager(request.getUsers());
            User manager = userService.getUserFromUserRegistry(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

            List<User> delegate = request.getUsers()
                    .stream()
                    .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                    .map(userToOnboard -> userService.getUserFromUserRegistry(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

            String contractTemplate = contractService.extractTemplate(request.getContract().getPath());
            File pdf = contractService.createContractPDF(contractTemplate, manager, delegate, institution, request, geographicTaxonomies);
            String digest = createDigest(pdf);
            String tokenId = onboardingDao.persist(toUpdate, toDelete, request, institution, geographicTaxonomies, digest);
            log.info("{} - Digest {}", tokenId, digest);
            try {
                emailService.sendMail(pdf, institution, user, request, false);
            } catch (Exception e) {
                onboardingDao.rollbackSecondStep(toUpdate, toDelete, institution, tokenId);
            }
        }
    }

    @Override
    public void completeOboarding(Token token, MultipartFile contract) {
        List<OnboardedUser> onboardedUsers = new ArrayList<>();
        if (token.getUsers() != null) {
            token.getUsers().forEach(s -> onboardedUsers.add(userService.findByUserId(s)));
        }
        List<String> managerList = getValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
        List<User> managersData = managerList
                .stream()
                .map(user -> userService.getUserFromUserRegistry(user, EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        Product product = onboardingDao.getProductById(token.getProductId());
        contractService.verifySignature(contract, token, managersData);
        File logoFile = contractService.getLogoFile();
        emailService.sendCompletedEmail(contract, token, managersData, institution, product, logoFile);
        onboardingDao.persistForUpdate(token, institution, onboardedUsers, RelationshipState.ACTIVE);
    }

    @Override
    public void approveOnboarding(Token token, SelfCareUser selfCareUser) {
        log.info("Onboarding Approve having tokenId {}", token.getId());
        User currentUser = userService.getUserFromUserRegistry(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));

        //LISTA SIA DI DELEGATI CHE DI MANAGER
        List<OnboardedUser> onboardedUsers = new ArrayList<>();
        if (token.getUsers() != null) {
            token.getUsers().forEach(s -> onboardedUsers.add(userService.findByUserId(s)));
        }

        List<String> validManagerList = getValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
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
        token.setChecksum(digest);
        onboardingDao.persistForUpdate(token, institution, onboardedUsers, RelationshipState.PENDING);
        try {
            emailService.sendMail(pdf, institution, currentUser, request, true);
        } catch (Exception e) {
            onboardingDao.rollbackSecondStepOfUpdate(onboardedUsers, institution, token);
        }
    }

    @Override
    public void invalidateOnboarding(Token token) {
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
    }

    @Override
    public void onboardingReject(Token token) {
        Institution institution = institutionService.retrieveInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
        File logo = contractService.getLogoFile();
        Product product = onboardingDao.getProductById(token.getProductId());
        emailService.sendRejectMail(logo, institution, product);
    }


    private void invalidateToken(Token token, Institution institution) {
        log.info("START - invalidate token {}", token.getId());
        List<OnboardedUser> userList = new ArrayList<>();
        token.getUsers().forEach(s -> {
            OnboardedUser user = userService.findByUserId(s);
            if (user != null) {
                userList.add(user);
            }
        });
        onboardingDao.persistForUpdate(token, institution, userList, RelationshipState.REJECTED);
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
    public Object onboardingOperators(OnboardingOperatorsRequest toOnboardingOperatorRequest, PartyRole role) {
        return null;
    }

    @Override
    public Object onboardingLegals(OnboardingLegalsRequest toOnboardingLegalsRequest, Token token, SelfCareUser selfCareUser) {
        return null;
    }
}
