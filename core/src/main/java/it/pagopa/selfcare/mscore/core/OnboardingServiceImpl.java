package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final GeoTaxonomiesConnector geoTaxonomiesConnector;
    private final OnboardingDao onboardingDao;
    private final UserRegistryConnector userRegistryConnector;
    private final ContractService contractService;
    private final EmailService emailService;

    public OnboardingServiceImpl(GeoTaxonomiesConnector geoTaxonomiesConnector,
                                 OnboardingDao onboardingDao,
                                 UserRegistryConnector userRegistryConnector,
                                 ContractService contractService,
                                 EmailService emailService) {
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
        this.onboardingDao = onboardingDao;
        this.userRegistryConnector = userRegistryConnector;
        this.contractService = contractService;
        this.emailService = emailService;
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        List<Institution> list = onboardingDao.findInstitutionWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {

        List<RelationshipState> relationshipStateList = getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardedUser user = getUser(userId);
        Map<String, Map<String, OnboardedProduct>> userInstitutionsMap = getUserInstitutionsWithProductStatusIn(user.getBindings(), relationshipStateList);
        if (StringUtils.hasText(institutionId) || StringUtils.hasText(institutionExternalId)) {
            Institution onboardedInstitution = findInstitutionByOptionalId(institutionId, institutionExternalId);
            if (!userInstitutionsMap.containsKey(onboardedInstitution.getId())) {
                throw new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode());
            }
            Map<String, OnboardedProduct> institutionProductsMap = userInstitutionsMap.get(onboardedInstitution.getId());
            List<Onboarding> onboardingList = findOnboardingLinkedToProductWithStateIn(institutionProductsMap, onboardedInstitution, relationshipStateList);
            if (!onboardingList.isEmpty()) {
                onboardedInstitution.setOnboarding(onboardingList);
                onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, institutionProductsMap));
            }
        } else {
            userInstitutionsMap.forEach((idInstitution, institutionProductsMap) -> findInstutionById(idInstitution, onboardingInfoList, institutionProductsMap, relationshipStateList));
        }
        if (onboardingInfoList.isEmpty()) {
            throw new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode());
        }
        return onboardingInfoList;
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {
        Institution institution = onboardingDao.findInstitutionByExternalId(request.getInstitutionExternalId());
        checkIfProductAlreadyOnboarded(institution, request);
        validateOverridingData(request.getInstitutionUpdate(), institution);
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);

        List<OnboardedUser> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        if (InstitutionType.PG == institution.getInstitutionType()) {
            verifyPgUsers(request.getUsers());
            onboardingDao.persist(toUpdate, toDelete, request, institution, geographicTaxonomies, null);
        } else {
            User user = userRegistryConnector.getUserByInternalId(principal.getId(), EnumSet.allOf(User.Fields.class));
            verifyUsers(request.getUsers());
            List<String> validManagerList = getOnboardingValidManager(request.getUsers());
            User manager = userRegistryConnector.getUserByInternalId(validManagerList.get(0), EnumSet.allOf(User.Fields.class));

            List<User> delegate = request.getUsers()
                    .stream()
                    .filter(userToOnboard -> !validManagerList.contains(userToOnboard.getId()))
                    .map(userToOnboard -> userRegistryConnector.getUserByInternalId(userToOnboard.getId(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

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
            token.getUsers().forEach(s -> onboardedUsers.add(onboardingDao.getUserById(s)));
        }
        List<String> managerList = getValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
        List<User> managersData = managerList
                .stream()
                .map(user -> userRegistryConnector.getUserByInternalId(user, EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());

        Institution institution = onboardingDao.findInstitutionById(token.getInstitutionId());
        Product product = onboardingDao.getProductById(token.getProductId());
      //  contractService.verifySignature(contract, token, managersData);
        File logoFile = contractService.getLogoFile();
        emailService.sendCompletedEmail(contract, token, managersData, institution, product, logoFile);
        onboardingDao.persistForUpdate(token, institution, onboardedUsers, RelationshipState.ACTIVE);
    }

    @Override
    public void approveOnboarding(Token token, SelfCareUser selfCareUser) {
        log.info("Onboarding Approve having tokenId {}", token.getId());
        User currentUser = userRegistryConnector.getUserByInternalId(selfCareUser.getId(), EnumSet.allOf(User.Fields.class));

        //LISTA SIA DI DELEGATI CHE DI MANAGER
        List<OnboardedUser> onboardedUsers = new ArrayList<>();
        if (token.getUsers() != null) {
            token.getUsers().forEach(s -> onboardedUsers.add(onboardingDao.getUserById(s)));
        }

        List<String> validManagerList = getValidManager(onboardedUsers, token.getInstitutionId(), token.getProductId());
        User manager = userRegistryConnector.getUserByInternalId(validManagerList.get(0), EnumSet.allOf(User.Fields.class));
        List<User> delegate = onboardedUsers
                .stream()
                .filter(onboardedUser -> validManagerList.contains(onboardedUser.getUser()))
                .map(onboardedUser -> userRegistryConnector.getUserByInternalId(onboardedUser.getUser(), EnumSet.allOf(User.Fields.class))).collect(Collectors.toList());
        Institution institution = onboardingDao.findInstitutionById(token.getInstitutionId());
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
        Institution institution = onboardingDao.findInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
    }

    @Override
    public void onboardingReject(Token token) {
        Institution institution = onboardingDao.findInstitutionById(token.getInstitutionId());
        invalidateToken(token, institution);
        File logo = contractService.getLogoFile();
        Product product = onboardingDao.getProductById(token.getProductId());
        emailService.sendRejectMail(logo, institution, product);
    }

    private void invalidateToken(Token token, Institution institution) {
        log.info("START - invalidate token {}", token.getId());
        List<OnboardedUser> userList = new ArrayList<>();
        token.getUsers().forEach(s -> {
            OnboardedUser user = onboardingDao.getUserById(s);
            if (user != null) {
                userList.add(user);
            }
        });
        onboardingDao.persistForUpdate(token, institution, userList, RelationshipState.REJECTED);
    }

    private void findInstutionById(String idInstitution, List<OnboardingInfo> onboardingInfoList, Map<String, OnboardedProduct> institutionProductsMap, List<RelationshipState> relationshipStateList) {
        log.info("START - findInstitutionById: {}", idInstitution);
        Institution institutionFound = onboardingDao.findInstitutionById(idInstitution);
        List<Onboarding> onboardingList = findOnboardingLinkedToProductWithStateIn(institutionProductsMap, institutionFound, relationshipStateList);
        if (!onboardingList.isEmpty()) {
            institutionFound.setOnboarding(onboardingList);
            onboardingInfoList.add(new OnboardingInfo(institutionFound, institutionProductsMap));
        }
    }

    private OnboardedUser getUser(String userId) {
        log.info("START - getUser with id: {}", userId);
        List<OnboardedUser> userList = onboardingDao.getUserByUser(userId);
        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
        }
    }

    private Institution findInstitutionByOptionalId(String institutionId, String institutionExternalId) {
        if (StringUtils.hasText(institutionId)) {
            return onboardingDao.findInstitutionById(institutionId);
        } else {
            return onboardingDao.findInstitutionByExternalId(institutionExternalId);
        }
    }

    private List<GeographicTaxonomies> getGeographicTaxonomy(OnboardingRequest request) {
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        if (request.getInstitutionUpdate().getGeographicTaxonomyCodes() != null &&
                !request.getInstitutionUpdate().getGeographicTaxonomyCodes().isEmpty()) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomyCodes().stream
                    ().map(geoTaxonomiesConnector::getExtByCode).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty()) {
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomyCodes()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
            }
        }
        return geographicTaxonomies;
    }
}
