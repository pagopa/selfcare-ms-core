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
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.constant.TokenType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserService userService;

    public OnboardingServiceImpl(OnboardingDao onboardingDao,
                                 InstitutionService institutionService,
                                 UserService userService) {
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userService = userService;
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
        List<InstitutionGeographicTaxonomies> institutionGeographicTaxonomies = new ArrayList<>();
        if(!geographicTaxonomies.isEmpty()) {
               institutionGeographicTaxonomies = geographicTaxonomies.stream().map(geo -> new InstitutionGeographicTaxonomies(geo.getCode(), geo.getDesc())).collect(Collectors.toList());
        }
        List<String> toUpdate = new ArrayList<>();
        List<String> toDelete = new ArrayList<>();

        if (InstitutionType.PG == institution.getInstitutionType()) {
            if (request.getUsers().size() == 1) {
                verifyUsers(request.getUsers(), List.of(PartyRole.MANAGER));
            } else {
                throw new InvalidRequestException(CustomError.USERS_SIZE_NOT_ADMITTED.getMessage(), CustomError.USERS_SIZE_NOT_ADMITTED.getCode());
            }
            onboardingDao.persist(toUpdate, toDelete, request, institution, institutionGeographicTaxonomies, null);
        } else {
           //TODO: onboarding pg mittenti
        }
    }

    @Override
    public List<RelationshipInfo> onboardingOperators(OnboardingOperatorsRequest onboardingOperatorRequest, PartyRole role) {
        verifyUsers(onboardingOperatorRequest.getUsers(), List.of(role));
        Institution institution = institutionService.retrieveInstitutionById(onboardingOperatorRequest.getInstitutionId());
        return onboardingDao.onboardOperator(onboardingOperatorRequest, institution);
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
        if (request.getInstitutionUpdate().getGeographicTaxonomies() != null &&
                !request.getInstitutionUpdate().getGeographicTaxonomies().isEmpty()) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomies().stream
                    ().map(institutionGeographicTaxonomies -> institutionService.retrieveGeoTaxonomies(institutionGeographicTaxonomies.getCode())).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty()) {
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomies()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
            }
        }
        return geographicTaxonomies;
    }
}
