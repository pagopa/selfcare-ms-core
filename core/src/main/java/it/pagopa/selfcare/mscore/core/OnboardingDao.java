package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

@Service
@Slf4j
public class OnboardingDao {
    private final InstitutionConnector institutionConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;

    public OnboardingDao(InstitutionConnector institutionConnector,
                         TokenConnector tokenConnector,
                         UserConnector userConnector) {
        this.institutionConnector = institutionConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
    }

    public String persist(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String digest) {
        Token token = createToken(request, institution, digest);
        log.info("created token {} for institution {} and product {}", token.getId(), institution.getId(), request.getProductId());
        updateInstitution(request, institution, geographicTaxonomies, token.getId());
        createUsers(toUpdate, toDelete, request, institution, token);
        return token.getId();
    }

    public String persistUsers(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, String digest) {
        Token token = createToken(request, institution, digest);
        createUsers(toUpdate, toDelete, request, institution, token);
        return token.getId();
    }

    public void persistForUpdate(Token token, Institution institution, List<OnboardedUser> userList, RelationshipState stateTo) {
        updateToken(token, stateTo);
        updateInstitutionState(institution, token, stateTo);
        updateUsers(userList, institution, token, stateTo);
    }

    public void updateUsers(List<OnboardedUser> userList, Institution institution, Token token, RelationshipState state) {
        log.info("update {} users state from {} to {} for product {}", userList.size(), token.getStatus(), state, token.getProductId());
        List<OnboardedUser> toUpdate = new ArrayList<>();
        userList.forEach(onboardedUser -> {
            try {
                userConnector.findAndUpdateState(onboardedUser.getId(), institution.getId(), token.getProductId(), state);
                toUpdate.add(onboardedUser);
                log.debug("updated user {}", onboardedUser.getId());
            } catch (Exception e) {
                rollbackSecondStepOfUpdate(toUpdate, institution, token);
            }
        });
    }


    private void updateInstitutionState(Institution institution, Token token, RelationshipState state) {
        log.info("update institution state from {} to {} for product {}", institution, state);
        try {
            institutionConnector.findAndUpdateStatus(institution.getId(), token.getProductId(), state);
            //TODO: VEDERE L'ECCEZIONE IN CASO DI PRODOTTO NON TROVATO
        } catch (Exception e) {
            rollbackFirstStepOfUpdate(token);
        }
    }

    private void updateToken(Token token, RelationshipState state) {
        log.info("update token {} from state {} to {}", token.getId(), token.getStatus(), state);
        tokenConnector.findAndUpdateTokenState(token.getId(), state);
    }

    private Token createToken(OnboardingRequest request, Institution institution, String digest) {
        log.info("createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        return tokenConnector.save(convertToToken(request, institution, digest));
    }

    private void updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String tokenId) {
        Onboarding onboarding = constructOnboarding(request);
        try {
            log.debug("add onboarding {} to institution {}", onboarding, institution.getExternalId());
            institutionConnector.findAndUpdate(institution.getId(), onboarding, geographicTaxonomies);
        } catch (Exception e) {
            rollbackFirstStep(tokenId, institution);
        }
    }

    private void createUsers(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, Token token) {
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        try {
            request.getUsers()
                    .forEach(userToOnboard -> {
                        OnboardedUser onboardedUser = isNewUser(toUpdate, userToOnboard.getId());
                        if (onboardedUser != null) {
                            updateUser(onboardedUser, userToOnboard, institution.getId(), request);
                        } else {
                            createNewUser(userToOnboard, institution.getId(), request);
                        }
                    });
            log.debug("users to update: {}", toUpdate);
            tokenConnector.findAndUpdateTokenUser(token.getId(), usersId);
        } catch (Exception e) {
            List<String> toUpdateId = toUpdate.stream().map(OnboardedUser::getId).collect(Collectors.toList());
            toDelete.addAll(usersId.stream().filter(id -> !toUpdateId.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institution, token.getId());
        }
    }

    private void createNewUser(UserToOnboard user, String institutionId, OnboardingRequest request) {
        OnboardedProduct product = constructProduct(user, request);
        UserBinding binding = new UserBinding(institutionId, List.of(product), OffsetDateTime.now());
        userConnector.findAndCreate(user.getId(), institutionId, product, binding);
    }

    private void updateUser(OnboardedUser onboardedUser, UserToOnboard user, String institutionId, OnboardingRequest request) {
        OnboardedProduct product = constructProduct(user, request);
        UserBinding binding = new UserBinding(institutionId, List.of(product), OffsetDateTime.now());
        userConnector.findAndUpdate(onboardedUser, user.getId(), institutionId, product, binding);
    }

    private OnboardedUser isNewUser(List<OnboardedUser> toUpdate, String userId) {
        OnboardedUser onboardedUser = userConnector.getById(userId);
        if (onboardedUser != null) {
            toUpdate.add(onboardedUser);
        }
        return onboardedUser;
    }

    public void rollbackSecondStep(List<OnboardedUser> toUpdate, List<String> toDelete, Institution institution, String tokenId) {
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        toDelete.forEach(userConnector::deleteById);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStep(String tokenId, Institution institution) {
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStepOfUpdate(List<OnboardedUser> toUpdate, Institution institution, Token token) {
        tokenConnector.save(token);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStepOfUpdate(Token token) {
        tokenConnector.save(token);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void updateUserProductState(OnboardedUser user, String relationshipId, List<RelationshipState> fromStates, RelationshipState toState) {
        for (UserBinding binding : user.getBindings()) {
            for (OnboardedProduct product : binding.getProducts()) {
                if (relationshipId.equalsIgnoreCase(product.getRelationshipId())
                        && fromStates.contains(product.getStatus())) {
                    userConnector.findAndUpdateState(user.getId(), binding.getInstitutionId(), product.getProductId(), toState);
                } else {
                    throw new InvalidRequestException("0000", "Invalid status change");
                }
            }
        }
    }
}
