package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.core.util.TokenUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.INVALID_STATUS_CHANGE;
import static it.pagopa.selfcare.mscore.constant.GenericError.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardingDao {

    private final InstitutionConnector institutionConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final ProductService productService;
    private final CoreConfig coreConfig;

    public OnboardingRollback persist(List<String> toUpdate,
                                      List<String> toDelete,
                                      OnboardingRequest request,
                                      Institution institution,
                                      List<InstitutionGeographicTaxonomies> geographicTaxonomies,
                                      String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getOnboardingExpiringDate(), geographicTaxonomies);
        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());
        Onboarding onboarding = updateInstitution(request, institution, null, token);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution, token, onboarding);
        return new OnboardingRollback(token, onboarding, productMap, null);
    }



    public OnboardingRollback persistComplete(List<String> toUpdate,
                                      List<String> toDelete,
                                      OnboardingRequest request,
                                      Institution institution,
                                      List<InstitutionGeographicTaxonomies> geographicTaxonomies,
                                      String digest) {

        log.info("createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());

        OffsetDateTime createdAt = Objects.nonNull(request.getContractCreatedAt()) ? request.getContractCreatedAt() : OffsetDateTime.now();
        OffsetDateTime activatedAt = Objects.nonNull(request.getContractActivatedAt()) ? request.getContractActivatedAt() : OffsetDateTime.now();

        Token token = TokenUtils.toToken(request, institution, digest, null);
        token.setStatus(RelationshipState.ACTIVE);
        token.setContractSigned(request.getContractFilePath());
        token.setContentType(MediaType.APPLICATION_JSON_VALUE);
        token.setCreatedAt(createdAt);
        token.setActivatedAt(activatedAt);
        token = tokenConnector.save(token, geographicTaxonomies);

        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());

        Onboarding onboarding = constructOnboarding(request, institution);
        onboarding.setTokenId(token.getId());
        onboarding.setContract(request.getContractFilePath());
        onboarding.setStatus(RelationshipState.ACTIVE);
        onboarding.setCreatedAt(createdAt);
        Institution institutionUpdated = null;

        try {
            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());
            institutionUpdated = institutionConnector.findAndUpdateInstitutionDataWithNewOnboarding(institution.getId(),
                    token.getInstitutionUpdate(), onboarding);
        } catch (Exception e) {
            log.warn("can not update institution {}", institution.getId(), e);
            rollbackFirstStep(token, institution.getId(), onboarding);
        }

        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {

            for (UserToOnboard userToOnboard : request.getUsers()) {
                createOrAddOnboardedProductUser(toUpdate, userToOnboard, institution, request, token.getId(), createdAt, RelationshipState.ACTIVE)
                        .ifPresent(onboardedProduct -> productMap.put(userToOnboard.getId(), onboardedProduct));
            }
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            toDelete.addAll(usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institution.getId(), token, onboarding, productMap);
        }

        return new OnboardingRollback(token, onboarding, productMap, institutionUpdated);
    }

    public OnboardingRollback persistLegals(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getOnboardingExpiringDate(), null);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution, token, null);
        return new OnboardingRollback(token, null, productMap, null);
    }

    public OnboardingUpdateRollback persistForUpdate(Token token, Institution institution, RelationshipState toState, String digest) {
        log.trace("persistForUpdate start");
        log.debug("persistForUpdate token = {}, institution = {}, toState = {}, digest = {}", token, institution, toState, digest);
        OnboardingUpdateRollback rollback = new OnboardingUpdateRollback();
        if (isValidStateChangeForToken(token.getStatus(), toState)) {
            rollback.setToken(updateToken(token, toState, digest));
            if (RelationshipState.ACTIVE == toState) {
                rollback.setUpdatedInstitution(updateInstitutionData(institution, token, toState));
            } else {
                rollback.setUpdatedInstitution(updateInstitutionState(institution, token, toState));
            }
            rollback.setUserList(updateUsersState(institution, token, toState));
            log.trace("persistForUpdate end");
            return rollback;
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
                institutionConnector.findAndUpdate(institution.getId(), onboarding, geographicTaxonomies, null);
            }
        } catch (Exception e) {
            log.warn("can not update institution {}", institution.getId(), e);
            rollbackFirstStep(token, institution.getId(), onboarding);
        }
        return onboarding;
    }

    private Institution updateInstitutionData(Institution institution, Token token, RelationshipState toState) {
        try {
            return institutionConnector.findAndUpdateInstitutionData(institution.getId(), token, null, toState);
        } catch (Exception e) {
            log.warn("can not update data of institution {}", institution.getId(), e);
            rollbackFirstStepOfUpdate(token);
        }
        return institution;
    }

    private Institution updateInstitutionState(Institution institution, Token token, RelationshipState state) {
        log.info("update institution status from {} to {} for product {}", token.getStatus(), state, token.getProductId());
        try {
            return institutionConnector.findAndUpdateStatus(institution.getId(), token.getId(), state);
        } catch (Exception e) {
            log.warn("can not update state of institution {}", institution.getId(), e);
            rollbackFirstStepOfUpdate(token);
        }
        return institution;
    }

    private OnboardedProduct updateUser(OnboardedUser onboardedUser, UserToOnboard user, Institution institution, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request, institution);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institution.getId(),
                institution.getDescription(),
                institution.getParentDescription(),
                List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), institution.getId(), product, binding);
        return product;
    }

    public List<String> updateUsersState(Institution institution, Token token, RelationshipState state) {
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
        return toUpdate;
    }

    public void updateUserProductState(OnboardedUser user, String relationshipId, RelationshipState toState) {
        for (UserBinding binding : user.getBindings()) {
            binding.getProducts().stream()
                    .filter(onboardedProduct -> relationshipId.equalsIgnoreCase(onboardedProduct.getRelationshipId()))
                    .forEach(product -> checkStatus(product.getStatus(), user.getId(), product.getRelationshipId(), toState));
        }
    }

    private OnboardedProduct updateOperator(List<RelationshipInfo> response, OnboardedUser onboardedUser, UserToOnboard user, Institution institution, String productId) {
        OnboardedProduct product = constructOperatorProduct(user, productId);
        UserBinding binding = new UserBinding(institution.getId(),
                institution.getDescription(),
                institution.getParentDescription(),
                List.of(product));
        relationshipExistAndDelete(onboardedUser, productId, institution.getId(), user);
        userConnector.findAndUpdate(onboardedUser, user.getId(), institution.getId(), product, binding);
        response.add(new RelationshipInfo(institution, user.getId(), product));
        return product;
    }

    private void relationshipExistAndDelete(OnboardedUser onboardedUser, String productId, String institutionId, UserToOnboard user){
        List<OnboardedProduct> products = onboardedUser.getBindings().stream()
                .flatMap(userBinding -> userBinding.getProducts().stream()
                        .filter(userProduct -> userProduct.getProductId().equalsIgnoreCase(productId)
                        && !userProduct.getStatus().equals(RelationshipState.DELETED)
                        && userBinding.getInstitutionId().equals(institutionId)))
                .collect(Collectors.toList());
        if(!products.isEmpty()) {
            if (user.getRole().equals(PartyRole.OPERATOR)) {
                products.stream()
                        .filter(singleProduct -> !singleProduct.getRole().equals(user.getRole()) || singleProduct.getProductRole().equals(user.getProductRole()))
                        .collect(Collectors.toList())
                        .forEach(productToDelete -> updateUserProductState(onboardedUser, productToDelete.getRelationshipId(), RelationshipState.DELETED));
            } else {
                products.forEach(productToDelete -> updateUserProductState(onboardedUser, productToDelete.getRelationshipId(), RelationshipState.DELETED));
            }
        }
    }

    private Token updateToken(Token token, RelationshipState state, String digest) {
        log.info("update token {} from state {} to {}", token.getId(), token.getStatus(), state);
        Token updatedToken = tokenConnector.findAndUpdateToken(token, state, digest);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "updatedToken updatedToken = {}", updatedToken);
        return updatedToken;
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
                updateOrCreateUserToOnboard(toUpdate, userToOnboard, institution, request, token.getId(), productMap);
            }
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            toDelete.addAll(usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institution.getId(), token, onboarding, productMap);
        }
        return productMap;
    }

    private void updateOrCreateUserToOnboard(List<String> toUpdate, UserToOnboard userToOnboard, Institution institution, OnboardingRequest request, String tokenId, Map<String, OnboardedProduct> productMap) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
            OnboardedProduct currentProduct = updateUser(onboardedUser, userToOnboard, institution, request, tokenId);
            productMap.put(userToOnboard.getId(), currentProduct);
        } catch (ResourceNotFoundException e) {
            createNewUser(userToOnboard, institution, request, tokenId, Optional.empty());
        }
    }

    private Optional<OnboardedProduct> createOrAddOnboardedProductUser(List<String> toUpdate, UserToOnboard userToOnboard, Institution institution, OnboardingRequest request, String tokenId, OffsetDateTime createdAt, RelationshipState state) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());

            OnboardedProduct product = constructProduct(userToOnboard, request, institution);
            product.setTokenId(tokenId);
            product.setStatus(RelationshipState.ACTIVE);
            product.setCreatedAt(createdAt);
            UserBinding binding = new UserBinding(institution.getId(),
                    institution.getDescription(),
                    institution.getParentDescription(),
                    List.of(product));
            userConnector.findAndUpdate(onboardedUser, userToOnboard.getId(), institution.getId(), product, binding);

            return Optional.of(product);
        } catch (ResourceNotFoundException e) {
            createNewUser(userToOnboard, institution, request, tokenId, Optional.of(state));
        }

        return Optional.empty();
    }

    public List<RelationshipInfo> onboardOperator(Institution institution, String productId, List<UserToOnboard> users) {
        List<RelationshipInfo> response = new ArrayList<>();
        List<String> toUpdate = new ArrayList<>();
        List<String> usersId = users.stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            users.forEach(userToOnboard -> updateOrCreateOperator(toUpdate, userToOnboard, response, institution, productMap, productId));
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            log.warn("can not onboard operators", e);
            List<String> toDelete = usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList());
            rollbackUser(toUpdate, toDelete, institution.getId(), productMap);
        }
        return response;
    }

    private void updateOrCreateOperator(List<String> toUpdate, UserToOnboard userToOnboard, List<RelationshipInfo> response, Institution institution, Map<String, OnboardedProduct> productMap, String productId) {
        try {
            OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
            OnboardedProduct currentProduct = updateOperator(response, onboardedUser, userToOnboard, institution, productId);
            productMap.put(userToOnboard.getId(), currentProduct);
        } catch (ResourceNotFoundException e) {
            createOperator(response, userToOnboard, institution, productId);
        }
    }

    private void createNewUser(UserToOnboard user, Institution institution, OnboardingRequest request, String tokenId, Optional<RelationshipState> state) {
        OnboardedProduct product = constructProduct(user, request, institution);
        state.ifPresent(product::setStatus);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institution.getId(),
                institution.getDescription(),
                institution.getParentDescription(),
                List.of(product));
        userConnector.findAndCreate(user.getId(), binding);
    }

    private void createOperator(List<RelationshipInfo> response, UserToOnboard user, Institution institution, String productId) {
        OnboardedProduct product = constructOperatorProduct(user, productId);
        UserBinding binding = new UserBinding(institution.getId(),
                institution.getDescription(),
                institution.getParentDescription(),
                List.of(product));
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
        return productService.getProduct(productId);
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

    public void rollbackPersistOnboarding(String institutionId, Onboarding onboarding, List<UserToOnboard> users) {
        institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        users.forEach(user -> userConnector.findAndRemoveProduct(user.getId(), institutionId, OnboardingInstitutionUtils.constructOperatorProduct(user, onboarding.getProductId())));
        log.debug("rollback persistOnboarding");
    }

    private void rollbackUser(List<String> toUpdate, List<String> toDelete, String institutionId, Map<String, OnboardedProduct> productMap) {
        toUpdate.forEach(userId -> userConnector.findAndRemoveProduct(userId, institutionId, productMap.get(userId)));
        toDelete.forEach(userId -> userConnector.findAndRemoveProduct(userId, institutionId, productMap.get(userId)));
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
                return RelationshipState.PENDING == toState || RelationshipState.REJECTED == toState || RelationshipState.DELETED == toState
                        || RelationshipState.ACTIVE == toState;
            case PENDING:
                return RelationshipState.ACTIVE == toState || RelationshipState.REJECTED == toState || RelationshipState.DELETED == toState;
            default:
                return false;
        }
    }
}
