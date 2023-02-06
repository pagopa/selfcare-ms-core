package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInfoUtils.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final InstitutionConnector institutionConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;
    private final ContractService contractService;

    @Autowired
    public OnboardingServiceImpl(InstitutionConnector institutionConnector,
                                 GeoTaxonomiesConnector geoTaxonomiesConnector,
                                 TokenConnector tokenConnector,
                                 UserConnector userConnector,
                                 UserRegistryConnector userRegistryConnector, ContractService contractService) {
        this.institutionConnector = institutionConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
        this.contractService = contractService;
    }

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            log.info(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId));
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<OnboardingInfo> getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {
        log.info("Getting onboarding info for institution having institutionId {} institutionExternalId {} and states {}", institutionId, institutionExternalId, states);

        List<RelationshipState> relationshipStateList = getRelationShipStateList(states);
        List<OnboardingInfo> onboardingInfoList = new ArrayList<>();
        OnboardedUser user = getUser(userId);
        Map<String, Map<String, Product>> userInstitutionsMap = getUserInstitutionsWithProductStatusIn(user.getBindings(), relationshipStateList);
        if (StringUtils.hasText(institutionId) || StringUtils.hasText(institutionExternalId)) {
            Institution onboardedInstitution = findInstitutionByOptionalId(institutionId, institutionExternalId);
            if (!userInstitutionsMap.containsKey(onboardedInstitution.getId())) {
                //L'utente non è collegato all'institution trovata
                log.info("Error getting onboarding info");
                throw new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode());
            }
            Map<String, Product> institutionProductsMap = userInstitutionsMap.get(onboardedInstitution.getId());
            List<Onboarding> onboardingList = findOnboardingLinkedToProductWithStateIn(institutionProductsMap, onboardedInstitution, relationshipStateList);
            if (!onboardingList.isEmpty()) {
                onboardedInstitution.setOnboarding(onboardingList);
                onboardingInfoList.add(new OnboardingInfo(onboardedInstitution, institutionProductsMap));
            }
        } else {
            userInstitutionsMap.forEach((idInstitution, institutionProductsMap) -> findInstutionById(idInstitution, onboardingInfoList, institutionProductsMap, relationshipStateList));
        }
        if (onboardingInfoList.isEmpty()) {
            //Non sono stati trovati prodotti con uno degli stati passati in input
            log.info("Error getting onboarding info");
            throw new InvalidRequestException(ONBOARDING_INFO_ERROR.getMessage(), ONBOARDING_INFO_ERROR.getCode());
        }
        return onboardingInfoList;
    }

    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {

        log.info("Onboarding optionalInstitution having externalId {}", request.getInstitutionExternalId());

        Optional<Institution> optionalInstitution = institutionConnector.findByExternalId(request.getInstitutionExternalId());
        if (optionalInstitution.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), null, request.getInstitutionExternalId()), INSTITUTION_NOT_FOUND.getCode());
        }
        Institution institution = optionalInstitution.get();
        checkIfProductAlreadyOnboarded(institution, request);
        validateOverridingData(request.getInstitutionUpdate(), institution);
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);

        if (InstitutionType.PG == institution.getInstitutionType()) {
            verifyPgUsers(request.getUsers());
            persist(request, institution, geographicTaxonomies, null);
        } else {
            User user = userRegistryConnector.getUserByInternalId(principal.getId(), EnumSet.allOf(User.Fields.class));
            verifyUsers(request.getUsers());
            OnboardedUser validManager = getValidManager(request.getUsers());
            String contractTemplate = contractService.extractTemplate(request);
            File pdf = contractService.createContractPDF(contractTemplate, validManager, request.getUsers(), institution, request, geographicTaxonomies);
            String digest = createDigest(pdf);
            log.info("Digest {}", digest);
            persist(request, institution, geographicTaxonomies, digest);
            contractService.sendMail(pdf,institution, user, request);
        }
    }

    private void findInstutionById(String idInstitution, List<OnboardingInfo> onboardingInfoList, Map<String, Product> institutionProductsMap, List<RelationshipState> relationshipStateList) {
        log.info("START - findInstitutionById: {}", idInstitution);
        Optional<Institution> optInstitution = institutionConnector.findById(idInstitution);
        if (optInstitution.isPresent()) {
            Institution institutionFound = optInstitution.get();
            List<Onboarding> onboardingList = findOnboardingLinkedToProductWithStateIn(institutionProductsMap, institutionFound, relationshipStateList);
            if (!onboardingList.isEmpty()) {
                institutionFound.setOnboarding(onboardingList);
                onboardingInfoList.add(new OnboardingInfo(institutionFound, institutionProductsMap));
            }
        }
    }

    private OnboardedUser getUser(String userId) {
        log.info("START - getUser with id: {}", userId);
        List<OnboardedUser> userList = userConnector.getByUser(userId);
        if (userList != null && !userList.isEmpty()) {
            return userList.get(0);
        } else {
            throw new ResourceNotFoundException(String.format(USER_NOT_FOUND_ERROR.getMessage(), userId), USER_NOT_FOUND_ERROR.getCode());
        }
    }

    private Institution findInstitutionByOptionalId(String institutionId, String institutionExternalId) {
        if (StringUtils.hasText(institutionId)) {
            Optional<Institution> found = institutionConnector.findById(institutionId);
            if (found.isEmpty()) {
                throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), "institutionId : " + institutionId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
            }
            log.info("founded institution having internalId: {}", institutionId);
            return found.get();
        } else {
            Optional<Institution> found = institutionConnector.findByExternalId(institutionExternalId);
            if (found.isEmpty()) {
                throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), "institutionExternalId : " + institutionExternalId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
            }
            log.info("founded institution having externalId: {}", institutionExternalId);
            return found.get();
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

    public void persist(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String digest) {
        log.info("START - persist token, institution and users");
        String tokenId = createToken(request, institution, digest);
        log.info("{} for institution: {}", tokenId, institution.getId());
        updateInstitution(request, institution, geographicTaxonomies, tokenId);
        createUsers(request, institution, tokenId);
    }

    private String createToken(OnboardingRequest request, Institution institution, String digest) {
        log.info("START - createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        return tokenConnector.save(convertToToken(request, institution, digest)).getId();
    }

    private void updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String tokenId) {
        Institution newInstitution = new Institution(institution);
        Onboarding onboarding = constructOnboarding(request);
        try {
            if (newInstitution.getOnboarding() != null) {
                newInstitution.getOnboarding().add(onboarding);
            } else {
                newInstitution.setOnboarding(List.of(onboarding));
            }
            newInstitution.setUpdatedAt(OffsetDateTime.now());
            newInstitution.setGeographicTaxonomies(geographicTaxonomies);

            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());

            institutionConnector.save(newInstitution);
        } catch (Exception e) {
            rollbackFirstStep(tokenId);
        }
    }

    private void createUsers(OnboardingRequest request, Institution institution, String tokenId) {
        List<String> toDelete = new ArrayList<>();
        List<OnboardedUser> toUpdate = new ArrayList<>();
        request.getUsers()
                .forEach(onboardedUser -> {
                    try {
                        checkIfNewUser(toUpdate, toDelete, onboardedUser, institution.getId(), request);
                    } catch (Exception e) {
                        rollbackSecondStep(toUpdate, toDelete, institution, tokenId);
                    }
                });
        log.debug("users to delete: {}", toDelete);
        log.debug("users to update: {}", toUpdate);
    }

    private void checkIfNewUser(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardedUser user, String institutionId, OnboardingRequest request) {
        List<OnboardedUser> onboardedUsers = userConnector.getByUser(user.getUser());
        if (!onboardedUsers.isEmpty()) {
            onboardedUsers.forEach(o -> {
                if (o.getBindings().get(institutionId) != null) {
                    o.getBindings().get(institutionId).put(request.getProductId(), constructProduct(user, request.getInstitutionUpdate().getInstitutionType()));
                } else {
                    o.getBindings().put(institutionId, constructProductMap(request, user));
                }
                toUpdate.add(userConnector.save(o));
            });
        } else {
            user.setBindings(constructMap(user, request, institutionId));
            user.setCreatedAt(OffsetDateTime.now());
            toDelete.add(userConnector.save(user).getId());
        }
    }

    private void rollbackSecondStep(List<OnboardedUser> toUpdate, List<String> toDelete, Institution institution, String tokenId) {
        log.info("START - rollbackSecondStep - rollback token and institution");
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        toDelete.forEach(userConnector::deleteById);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStep(String tokenId) {
        log.info("START - rollbackFirstStep - rollback token");
        tokenConnector.deleteById(tokenId);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }
}
