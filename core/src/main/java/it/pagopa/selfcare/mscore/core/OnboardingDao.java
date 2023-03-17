package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.INVALID_STATUS_CHANGE;
import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

@Service
@Slf4j
public class OnboardingDao {

    private final InstitutionConnector institutionConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final CoreConfig coreConfig;

    public OnboardingDao(InstitutionConnector institutionConnector,
                         TokenConnector tokenConnector,
                         UserConnector userConnector,
                         CoreConfig coreConfig) {
        this.institutionConnector = institutionConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.coreConfig = coreConfig;
    }

    public OnboardingRollback persist(List<String> toUpdate,
                                      List<String> toDelete,
                                      OnboardingRequest request,
                                      Institution institution,
                                      List<InstitutionGeographicTaxonomies> geographicTaxonomies,
                                      String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getExpiringDate(), geographicTaxonomies);
        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());
        Onboarding onboarding = updateInstitution(request, institution, geographicTaxonomies, token);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution.getId(), token.getId(), onboarding);
        return new OnboardingRollback(token.getId(), onboarding, productMap);
    }

    private Onboarding updateInstitution(OnboardingRequest request, Institution institution, List<InstitutionGeographicTaxonomies> geographicTaxonomies, Token token) {
        Onboarding onboarding = constructOnboarding(request);
        onboarding.setTokenId(token.getId());
        try {
            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());
            if (RelationshipState.ACTIVE == token.getStatus()) {
                institutionConnector.findAndUpdateInstitutionData(institution.getId(), token, onboarding, null);
            } else {
                institutionConnector.findAndUpdate(institution.getId(), onboarding, geographicTaxonomies);
            }
        } catch (Exception e) {
            rollbackFirstStep(token, institution.getId(), onboarding);
        }
        return onboarding;
    }

    private OnboardedProduct updateUser(OnboardedUser onboardedUser, UserToOnboard user, String institutionId, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institutionId, List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), institutionId, product, binding);
        return product;
    }

    public void updateUserProductState(OnboardedUser user, String relationshipId, RelationshipState toState) {
        for (UserBinding binding : user.getBindings()) {
            binding.getProducts().stream()
                    .filter(onboardedProduct -> relationshipId.equalsIgnoreCase(onboardedProduct.getRelationshipId()))
                    .forEach(product -> checkStatus(product.getStatus(), user.getId(), product.getRelationshipId(), toState));
        }
    }

    private OnboardedProduct updateOperator(List<RelationshipInfo> response, OnboardedUser onboardedUser, UserToOnboard user, Institution institution, OnboardingOperatorsRequest request) {
        OnboardedProduct product = constructOperatorProduct(user, request);
        UserBinding binding = new UserBinding(request.getInstitutionId(), List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), request.getInstitutionId(), product, binding);
        response.add(new RelationshipInfo(institution, user.getId(), product));
        return product;
    }

    private Token createToken(OnboardingRequest request, Institution institution, String digest, Integer expire, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        log.info("createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        OffsetDateTime expiringDate = OffsetDateTime.now().plus(expire, ChronoUnit.DAYS);
        return tokenConnector.save(TokenUtils.toToken(request, institution, digest, expiringDate), geographicTaxonomies);
    }

    private Map<String, OnboardedProduct> createUsers(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, String institutionId, String tokenId, Onboarding onboarding) {
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            for (UserToOnboard userToOnboard : request.getUsers()) {
                updateOrCreateUser(toUpdate, userToOnboard, institutionId, request, tokenId, productMap);
            }
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            toDelete.addAll(usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institutionId, tokenId, onboarding, productMap);
        }
        return productMap;
    }

    private void updateOrCreateUser(List<String> toUpdate, UserToOnboard userToOnboard, String institutionId, OnboardingRequest request, String tokenId, Map<String, OnboardedProduct> productMap) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
            OnboardedProduct currentProduct = updateUser(onboardedUser, userToOnboard, institutionId, request, tokenId);
            productMap.put(userToOnboard.getId(), currentProduct);
        } catch (ResourceNotFoundException e) {
            createNewUser(userToOnboard, institutionId, request, tokenId);
        }
    }

    public List<RelationshipInfo> onboardOperator(OnboardingOperatorsRequest request, Institution institution) {
        List<RelationshipInfo> response = new ArrayList<>();
        List<String> toUpdate = new ArrayList<>();
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            request.getUsers()
                    .forEach(userToOnboard -> updateOrCreateOperator(toUpdate, userToOnboard, response, institution, request, productMap));
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            List<String> toDelete = usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList());
            rollbackUser(toUpdate, toDelete, request.getInstitutionId(), productMap);
        }
        return response;
    }

    private void updateOrCreateOperator(List<String> toUpdate, UserToOnboard userToOnboard, List<RelationshipInfo> response, Institution institution, OnboardingOperatorsRequest request, Map<String, OnboardedProduct> productMap) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
            OnboardedProduct currentProduct = updateOperator(response, onboardedUser, userToOnboard, institution, request);
            productMap.put(userToOnboard.getId(), currentProduct);
        } catch (ResourceNotFoundException e) {
            createOperator(response, userToOnboard, institution, request);
        }
    }

    private void createNewUser(UserToOnboard user, String institutionId, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institutionId, List.of(product));
        userConnector.findAndCreate(user.getId(), binding);
    }

    private void createOperator(List<RelationshipInfo> response, UserToOnboard user, Institution institution, OnboardingOperatorsRequest request) {
        OnboardedProduct product = constructOperatorProduct(user, request);
        UserBinding binding = new UserBinding(request.getInstitutionId(), List.of(product));
        userConnector.findAndCreate(user.getId(), binding);
        response.add(new RelationshipInfo(institution, user.getId(), product));
    }

    private OnboardedUser isNewUser(List<String> toUpdate, String userId) {
        OnboardedUser onboardedUser = userConnector.findById(userId);
        toUpdate.add(onboardedUser.getId());
        return onboardedUser;
    }

    private void checkStatus(RelationshipState fromState, String userId, String relationshipId, RelationshipState toState) {
        if (isValidStateChangeForRelationship(fromState, toState)) {
            userConnector.findAndUpdateState(userId, relationshipId, null, toState);
        } else {
            throw new InvalidRequestException((String.format(INVALID_STATUS_CHANGE.getMessage(), fromState, toState)), INVALID_STATUS_CHANGE.getCode());
        }
    }


    private void rollbackFirstStep(Token token, String institutionId, Onboarding onboarding) {
        tokenConnector.deleteById(token.getId());
        institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        log.debug("rollback first step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStep(List<String> toUpdate, List<String> toDelete, String institutionId, String tokenId, Onboarding onboarding, Map<String, OnboardedProduct> productMap) {
        if (tokenId != null) {
            tokenConnector.deleteById(tokenId);
        }
        if (institutionId != null && onboarding != null) {
            institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        }
        rollbackUser(toUpdate, toDelete, institutionId, productMap);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackUser(List<String> toUpdate, List<String> toDelete, String institutionId, Map<String, OnboardedProduct> productMap) {
        toUpdate.forEach(userId -> userConnector.findAndRemoveProduct(userId, institutionId, productMap.get(userId)));
        toDelete.forEach(userConnector::deleteById);
        log.debug("rollback second step completed");
    }

    private boolean isValidStateChangeForRelationship(RelationshipState fromState, RelationshipState toState) {
        switch (toState) {
            case ACTIVE:
                return fromState == RelationshipState.SUSPENDED;
            case SUSPENDED:
                return fromState == RelationshipState.ACTIVE;
            case DELETED:
                return fromState != RelationshipState.DELETED;
            default:
                return false;
        }
    }
}
