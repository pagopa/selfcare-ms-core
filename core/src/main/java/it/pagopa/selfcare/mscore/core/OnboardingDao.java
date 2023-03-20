package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
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
import it.pagopa.selfcare.mscore.model.product.Product;
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
    private final ProductConnector productConnector;
    private final CoreConfig coreConfig;

    public OnboardingDao(InstitutionConnector institutionConnector,
                         TokenConnector tokenConnector,
                         UserConnector userConnector,
                         ProductConnector productConnector,
                         CoreConfig coreConfig) {
        this.institutionConnector = institutionConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.productConnector = productConnector;
        this.coreConfig = coreConfig;
    }

    public OnboardingRollback persist(List<String> toUpdate,
                                      List<String> toDelete,
                                      OnboardingRequest request,
                                      Institution institution,
                                      List<InstitutionGeographicTaxonomies> geographicTaxonomies,
                                      String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getOnboardingExpiringDate(), geographicTaxonomies);
        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());
        Onboarding onboarding = updateInstitution(request, institution, geographicTaxonomies, token);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution, token, onboarding);
        return new OnboardingRollback(token, onboarding, productMap);
    }

    public OnboardingRollback persistLegals(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getOnboardingExpiringDate(), null);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution, token, null);
        return new OnboardingRollback(token, null, productMap);
    }

    public void persistForUpdate(Token token, Institution institution, RelationshipState toState, String digest) {
        if (isValidStateChangeForToken(token.getStatus(), toState)) {
            updateToken(token, toState, digest);
            if (RelationshipState.ACTIVE == toState) {
                updateInstitutionData(institution, token, toState);
            } else {
                updateInstitutionState(institution, token, toState);
            }
            updateUsersState(institution, token, toState);
        } else {
            throw new InvalidRequestException(String.format(INVALID_STATUS_CHANGE.getMessage(), token.getStatus(), toState), INVALID_STATUS_CHANGE.getCode());
        }
    }

    private Onboarding updateInstitution(OnboardingRequest request, Institution institution, List<InstitutionGeographicTaxonomies> geographicTaxonomies, Token token) {
        Onboarding onboarding = constructOnboarding(request, institution);
        onboarding.setTokenId(token.getId());
        try {
            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());
            if (RelationshipState.ACTIVE == token.getStatus()) {
                institutionConnector.findAndUpdateInstitutionData(institution.getId(), token, onboarding, null);
            } else {
                institutionConnector.findAndUpdate(institution.getId(), onboarding, geographicTaxonomies);
            }
        } catch (Exception e) {
            log.warn("can not update institution {}", institution.getId(), e);
            rollbackFirstStep(token, institution.getId(), onboarding);
        }
        return onboarding;
    }

    private void updateInstitutionData(Institution institution, Token token, RelationshipState toState) {
        try {
            institutionConnector.findAndUpdateInstitutionData(institution.getId(), token, null, toState);
        } catch (Exception e) {
            log.warn("can not update data of institution {}", institution.getId(), e);
            rollbackFirstStepOfUpdate(token);
        }
    }

    private void updateInstitutionState(Institution institution, Token token, RelationshipState state) {
        log.info("update institution status from {} to {} for product {}", token.getStatus(), state, token.getProductId());
        try {
            institutionConnector.findAndUpdateStatus(institution.getId(), token.getId(), state);
        } catch (Exception e) {
            log.warn("can not update state of institution {}", institution.getId(), e);
            rollbackFirstStepOfUpdate(token);
        }
    }

    private OnboardedProduct updateUser(OnboardedUser onboardedUser, UserToOnboard user, Institution institution, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request, institution);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institution.getId(), List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), institution.getId(), product, binding);
        return product;
    }

    public void updateUsersState(Institution institution, Token token, RelationshipState state) {
        log.info("update {} users state from {} to {} for product {}", token.getUsers().size(), token.getStatus(), state, token.getProductId());
        List<String> toUpdate = new ArrayList<>();
        token.getUsers().forEach(tokenUser -> {
            try {
                log.debug("updating user {} with tokenId {} to state {}", tokenUser.getUserId(), token.getId(), state);
                userConnector.findAndUpdateState(tokenUser.getUserId(), null, token, state);
                toUpdate.add(tokenUser.getUserId());
                log.debug("updated user {}", tokenUser.getUserId());
            } catch (Exception e) {
                log.warn("can not update state of user {}", tokenUser.getUserId(), e);
                rollbackSecondStepOfUpdate(toUpdate, institution, token);
            }
        });
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

    private void updateToken(Token token, RelationshipState state, String digest) {
        log.info("update token {} from state {} to {}", token.getId(), token.getStatus(), state);
        tokenConnector.findAndUpdateToken(token, state, digest);
    }

    private Token createToken(OnboardingRequest request, Institution institution, String digest, Integer expire, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        log.info("createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        OffsetDateTime expiringDate = OffsetDateTime.now().plus(expire, ChronoUnit.DAYS);
        return tokenConnector.save(TokenUtils.toToken(request, institution, digest, expiringDate), geographicTaxonomies);
    }

    private Map<String, OnboardedProduct> createUsers(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, Token token, Onboarding onboarding) {
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            for (UserToOnboard userToOnboard : request.getUsers()) {
                updateOrCreateUser(toUpdate, userToOnboard, institution, request, token.getId(), productMap);
            }
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            toDelete.addAll(usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institution.getId(), token, onboarding, productMap);
        }
        return productMap;
    }

    private void updateOrCreateUser(List<String> toUpdate, UserToOnboard userToOnboard, Institution institution, OnboardingRequest request, String tokenId, Map<String, OnboardedProduct> productMap) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
            OnboardedProduct currentProduct = updateUser(onboardedUser, userToOnboard, institution, request, tokenId);
            productMap.put(userToOnboard.getId(), currentProduct);
        } catch (ResourceNotFoundException e) {
            createNewUser(userToOnboard, institution, request, tokenId);
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
            log.warn("can not onboard operators", e);
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

    private void createNewUser(UserToOnboard user, Institution institution, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request, institution);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institution.getId(), List.of(product));
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

    public Product getProductById(String productId) {
        return productConnector.getProductById(productId);
    }

    public Token getTokenById(String tokenId) {
        return tokenConnector.findById(tokenId);
    }

    private void rollbackFirstStep(Token token, String institutionId, Onboarding onboarding) {
        tokenConnector.deleteById(token.getId());
        institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        log.debug("rollback first step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStep(List<String> toUpdate, List<String> toDelete, String institutionId, Token token, Onboarding onboarding, Map<String, OnboardedProduct> productMap) {
        if (token != null && token.getId()!=null) {
            tokenConnector.deleteById(token.getId());
        }
        if (institutionId != null && onboarding != null) {
            institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        }
        rollbackUser(toUpdate, toDelete, institutionId, productMap);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStepOfUpdate(Token token) {
        tokenConnector.findAndUpdateToken(token, token.getStatus(), token.getChecksum());
        log.debug("rollback first step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStepOfUpdate(List<String> toUpdate, Institution institution, Token token) {
        tokenConnector.findAndUpdateToken(token, token.getStatus(), token.getChecksum());
        institutionConnector.findAndUpdateStatus(institution.getId(), token.getId(), token.getStatus());
        toUpdate.forEach(userId -> userConnector.findAndUpdateState(userId, null, token, token.getStatus()));
        log.debug("rollback second step completed");
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

    private boolean isValidStateChangeForToken(RelationshipState fromState, RelationshipState toState) {
        switch (fromState) {
            case TOBEVALIDATED:
                return RelationshipState.PENDING == toState || RelationshipState.REJECTED == toState || RelationshipState.DELETED == toState;
            case PENDING:
                return RelationshipState.ACTIVE == toState || RelationshipState.REJECTED == toState || RelationshipState.DELETED == toState;
            default:
                return false;
        }
    }
}
