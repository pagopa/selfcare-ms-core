package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.INVALID_STATUS_CHANGE;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ONBOARDING_OPERATION_ERROR;
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

    public OnboardingRollback persist(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String digest) {
        Token token = createToken(request, institution, digest, coreConfig.getExpiringDate());
        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());
        Onboarding onboarding = updateInstitution(request, institution, geographicTaxonomies, token);
        Map<String, OnboardedProduct> productMap = createUsers(toUpdate, toDelete, request, institution.getId(), token.getId(), onboarding);
        return new OnboardingRollback(token.getId(), onboarding, productMap);
    }

    public void persistForUpdate(Token token, Institution institution, RelationshipState stateTo, String digest) {
        if (isValidStatusChange(token.getStatus(), stateTo)) {
            updateToken(token, stateTo, digest);
            updateInstitutionState(institution, token, stateTo);
            updateUsers(token.getUsers(), institution, token, stateTo);
        } else {
            throw new InvalidRequestException(String.format(INVALID_STATUS_CHANGE.getMessage(), token.getStatus(), stateTo), INVALID_STATUS_CHANGE.getCode());
        }
    }

    public void updateUsers(List<TokenUser> userList, Institution institution, Token token, RelationshipState state) {
        log.info("update {} users state from {} to {} for product {}", userList.size(), token.getStatus(), state, token.getProductId());
        List<String> toUpdate = new ArrayList<>();
        userList.forEach(tokenUser -> {
            try {
                userConnector.findAndUpdateState(tokenUser.getUserId(), institution.getId(), token.getProductId(), state);
                toUpdate.add(tokenUser.getUserId());
                log.debug("updated user {}", tokenUser.getUserId());
            } catch (Exception e) {
                rollbackSecondStepOfUpdate(toUpdate, institution, token);
            }
        });
    }


    private void updateInstitutionState(Institution institution, Token token, RelationshipState state) {
        log.info("update institution status from {} to {} for product {}", token.getStatus(), state, token.getProductId());
        try {
            institutionConnector.findAndUpdateStatus(institution.getId(), token.getProductId(), state);
        } catch (Exception e) {
            rollbackFirstStepOfUpdate(token);
        }
    }

    private void updateToken(Token token, RelationshipState state, String digest) {
        log.info("update token {} from state {} to {}", token.getId(), token.getStatus(), state);
        tokenConnector.findAndUpdateToken(token.getId(), state, digest);
    }

    private Token createToken(OnboardingRequest request, Institution institution, String digest, Integer expire) {
        log.info("createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        OffsetDateTime expiringDate = OffsetDateTime.now().plus(expire, TimeUnit.DAYS.toChronoUnit());
        return tokenConnector.save(convertToToken(request, institution, digest, expiringDate));
    }

    private Onboarding updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, Token token) {
        Onboarding onboarding = constructOnboarding(request);
        onboarding.setTokenId(token.getId());
        try {
            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());
            institutionConnector.findAndUpdate(institution.getId(), onboarding, geographicTaxonomies);
        } catch (Exception e) {
            rollbackFirstStep(token, institution.getId(), onboarding);
        }
        return onboarding;
    }

    private Map<String, OnboardedProduct> createUsers(List<String> toUpdate, List<String> toDelete, OnboardingRequest request, String institutionId, String tokenId, Onboarding onboarding) {
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            for (UserToOnboard userToOnboard : request.getUsers()) {
                OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
                if (onboardedUser != null) {
                    OnboardedProduct currentProduct = updateUser(onboardedUser, userToOnboard, institutionId, request, tokenId);
                    productMap.put(userToOnboard.getId(), currentProduct);
                } else {
                    createNewUser(userToOnboard, institutionId, request, tokenId);
                }
            }
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            toDelete.addAll(usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institutionId, tokenId, onboarding, productMap);
        }
        return productMap;
    }

    private void createNewUser(UserToOnboard user, String institutionId, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institutionId, List.of(product));
        userConnector.findAndCreate(user.getId(), binding);
    }

    private OnboardedProduct updateUser(OnboardedUser onboardedUser, UserToOnboard user, String institutionId, OnboardingRequest request, String tokenId) {
        OnboardedProduct product = constructProduct(user, request);
        product.setTokenId(tokenId);
        UserBinding binding = new UserBinding(institutionId, List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), institutionId, product, binding);
        return product;
    }

    private OnboardedUser isNewUser(List<String> toUpdate, String userId) {
        OnboardedUser onboardedUser = userConnector.findById(userId);
        if (onboardedUser != null) {
            toUpdate.add(onboardedUser.getId());
        }
        return onboardedUser;
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

    private void rollbackFirstStep(Token token, String institutionId, Onboarding onboarding) {
        tokenConnector.deleteById(token.getId());
        institutionConnector.findAndRemoveOnboarding(institutionId, onboarding);
        log.debug("rollback first step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStepOfUpdate(List<String> toUpdate, Institution institution, Token token) {
        tokenConnector.findAndUpdateToken(token.getId(), token.getStatus(), token.getChecksum());
        institutionConnector.findAndUpdateStatus(institution.getId(), token.getProductId(), token.getStatus());
        toUpdate.forEach(userId -> userConnector.findAndUpdateState(userId, institution.getId(), token.getProductId(), token.getStatus()));
        log.debug("rollback second step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStepOfUpdate(Token token) {
        tokenConnector.findAndUpdateToken(token.getId(), token.getStatus(), token.getChecksum());
        log.debug("rollback first step completed");
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void updateUserProductState(OnboardedUser user, String relationshipId, List<RelationshipState> fromStates, RelationshipState toState) {
        for (UserBinding binding : user.getBindings()) {
            binding.getProducts().stream()
                    .filter(onboardedProduct -> relationshipId.equalsIgnoreCase(onboardedProduct.getRelationshipId()))
                    .forEach(product -> checkStatus(fromStates, product.getStatus(), user.getId(), binding.getInstitutionId(), product.getProductId(), toState));
        }
    }

    private void checkStatus(List<RelationshipState> fromStates, RelationshipState status, String userId, String institutionId, String productId, RelationshipState toState) {
        if (fromStates.contains(status)) {
            userConnector.findAndUpdateState(userId, institutionId, productId, toState);
        } else {
            throw new InvalidRequestException((String.format(INVALID_STATUS_CHANGE.getMessage(), status, toState)), INVALID_STATUS_CHANGE.getCode());
        }
    }

    public Product getProductById(String productId) {
        return productConnector.getProductById(productId);
    }

    private boolean isValidStatusChange(RelationshipState status, RelationshipState stateTo) {
        switch (status) {
            case TOBEVALIDATED:
                return RelationshipState.PENDING == stateTo || RelationshipState.REJECTED == stateTo;
            case PENDING:
                //TODO: SCADENZA TOKEN IN PENDING CHANGE STATUS? DELETE RECORD?
                return RelationshipState.ACTIVE == stateTo;
            default:
                return false;
        }
    }

    public List<RelationshipInfo> onboardOperator(OnboardingOperatorsRequest request, Institution institution) {
        List<RelationshipInfo> response = new ArrayList<>();
        List<String> toUpdate = new ArrayList<>();
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        Map<String, OnboardedProduct> productMap = new HashMap<>();
        try {
            request.getUsers()
                    .forEach(userToOnboard -> {
                        OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
                        if (onboardedUser != null) {
                            OnboardedProduct currentProduct = updateOperator(response, onboardedUser, userToOnboard, institution, request);
                            productMap.put(userToOnboard.getId(), currentProduct);
                        } else {
                            createOperator(response, userToOnboard, institution, request);
                        }
                    });
            log.debug("users to update: {}", toUpdate);
        } catch (Exception e) {
            List<String> toDelete = usersId.stream().filter(id -> !toUpdate.contains(id)).collect(Collectors.toList());
            rollbackUser(toUpdate, toDelete, request.getInstitutionId(), productMap);
        }
        return response;
    }

    private void createOperator(List<RelationshipInfo> response, UserToOnboard user, Institution institution, OnboardingOperatorsRequest request) {
        OnboardedProduct product = constructOperatorProduct(user, request);
        UserBinding binding = new UserBinding(request.getInstitutionId(), List.of(product));
        userConnector.findAndCreate(user.getId(), binding);
        response.add(new RelationshipInfo(institution, user.getId(), product));
    }

    private OnboardedProduct updateOperator(List<RelationshipInfo> response, OnboardedUser onboardedUser, UserToOnboard user, Institution institution, OnboardingOperatorsRequest request) {
        OnboardedProduct product = constructOperatorProduct(user, request);
        UserBinding binding = new UserBinding(request.getInstitutionId(), List.of(product));
        userConnector.findAndUpdate(onboardedUser, user.getId(), request.getInstitutionId(), product, binding);
        response.add(new RelationshipInfo(institution, user.getId(), product));
        return product;
    }
}
