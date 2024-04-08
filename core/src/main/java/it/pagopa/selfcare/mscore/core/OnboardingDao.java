package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.product.entity.Product;
import it.pagopa.selfcare.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.INVALID_STATUS_CHANGE;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.constructOperatorProduct;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.constructProduct;

@Service
@Slf4j
@RequiredArgsConstructor
public class OnboardingDao {

    private final InstitutionConnector institutionConnector;
    private final UserConnector userConnector;

    public OnboardingDao(InstitutionConnector institutionConnector,
                         UserConnector userConnector) {
        this.institutionConnector = institutionConnector;
        this.userConnector = userConnector;
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
}
