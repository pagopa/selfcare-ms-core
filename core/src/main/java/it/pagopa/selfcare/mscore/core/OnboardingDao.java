package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.api.*;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
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
                         UserConnector userConnector, ProductConnector productConnector, CoreConfig coreConfig) {
        this.institutionConnector = institutionConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.productConnector = productConnector;
        this.coreConfig = coreConfig;
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

    private void updateUsers(List<OnboardedUser> userList, Institution institution, Token token, RelationshipState state) {
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
            rollbackFirstStep(tokenId);
        }
    }

    private void createUsers(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, Token token) {
        List<String> usersId = request.getUsers().stream().map(UserToOnboard::getId).collect(Collectors.toList());
        try {
            request.getUsers()
                    .forEach(userToOnboard -> {
                        checkIfNewUser(toUpdate, userToOnboard.getId());
                        persistUser(userToOnboard, institution.getId(), request);
                    });
            log.debug("users to update: {}", toUpdate);
            tokenConnector.findAndUpdateTokenUser(token.getId(), usersId);
        } catch (Exception e) {
            List<String> toUpdateId = toUpdate.stream().map(OnboardedUser::getId).collect(Collectors.toList());
            toDelete.addAll(usersId.stream().filter(id -> !toUpdateId.contains(id)).collect(Collectors.toList()));
            rollbackSecondStep(toUpdate, toDelete, institution, token.getId());
        }
    }

    private void persistUser(UserToOnboard user, String institutionId, OnboardingRequest request) {
        OnboardedProduct product = constructProduct(user, request.getInstitutionUpdate().getInstitutionType());
        UserBinding binding = constructBinding(user, request, institutionId);
        userConnector.findAndUpdate(user.getId(), institutionId, product, binding);
    }

    private void checkIfNewUser(List<OnboardedUser> toUpdate, String userId) {
        OnboardedUser onboardedUser = userConnector.getById(userId);
        if (onboardedUser != null) {
            toUpdate.add(onboardedUser);
        }
    }

    public void rollbackSecondStep(List<OnboardedUser> toUpdate, List<String> toDelete, Institution institution, String tokenId) {
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        toDelete.forEach(userConnector::deleteById);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStepOfUpdate(List<OnboardedUser> toUpdate, Institution institution, Token token) {
        tokenConnector.save(token);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStep(String tokenId) {
        tokenConnector.deleteById(tokenId);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStepOfUpdate(Token token) {
        tokenConnector.save(token);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void findInstitutionWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }


    public Product getProductById(String productId) {
        return productConnector.getProductById(productId);
    }

    public Institution saveInstitution(Institution institution) {
        Institution saved = institutionConnector.save(institution);
        log.info("institution {} created with id: {}", saved.getExternalId(), saved.getId());
        return saved;
    }

    public Institution getInstitutionProduct(String externalId, String productId) {
        return institutionConnector.findInstitutionProduct(externalId, productId);
    }
}
