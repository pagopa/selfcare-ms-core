package it.pagopa.selfcare.mscore.core;

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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.ONBOARDING_INFO_INSTITUTION_NOT_FOUND;
import static it.pagopa.selfcare.mscore.constant.GenericErrorEnum.ONBOARDING_OPERATION_ERROR;
import static it.pagopa.selfcare.mscore.core.util.OnboardingInstitutionUtils.*;

@Service
@Slf4j
public class OnboardingDao {
    private final InstitutionConnector institutionConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;
    private final ProductConnector productConnector;

    public OnboardingDao(InstitutionConnector institutionConnector,
                         TokenConnector tokenConnector,
                         UserConnector userConnector, ProductConnector productConnector) {
        this.institutionConnector = institutionConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
        this.productConnector = productConnector;
    }

    public String persist(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String digest) {
        log.info("START - persist token, institution and users");
        Token token = createToken(request, institution, digest);
        log.info("{} for institution: {}", token.getId(), institution.getId());
        updateInstitution(request, institution, geographicTaxonomies, token.getId());
        createUsers(toUpdate, toDelete, request, institution, token);
        return token.getId();
    }

    public void persistForUpdate(Token token, Institution institution, List<OnboardedUser> userList, RelationshipState state) {
        log.info("START - persistForUpdate token, institution and users");
        Token tokenToUpdate = new Token(token);
        updateToken(tokenToUpdate, state);
        log.info("{} for institution: {}", token.getId(), institution.getId());
        updateInstitutionState(institution, token, state);
        updateUsers(userList, institution, token, state);
    }

    private void updateUsers(List<OnboardedUser> userList, Institution institution, Token token, RelationshipState state) {
        log.info("START - update {} users with status {}", userList.size(), state);
        List<OnboardedUser> toUpdate = new ArrayList<>();
        userList.forEach(onboardedUser -> {
            try {
                OnboardedUser userToUpdate = new OnboardedUser(onboardedUser);
                if (userToUpdate.getBindings() != null && userToUpdate.getBindings().get(token.getInstitutionId()) != null) {
                    OnboardedProduct onboardedProduct = userToUpdate.getBindings().get(token.getInstitutionId()).get(token.getProductId());
                    if (onboardedProduct != null) {
                        onboardedProduct.setStatus(state);
                        onboardedProduct.setUpdatedAt(OffsetDateTime.now());
                        userConnector.save(userToUpdate);
                        toUpdate.add(onboardedUser);
                        log.info("updated user {}", userToUpdate.getId());
                    }
                }
            } catch (Exception e) {
                rollbackSecondStepOfUpdate(toUpdate, institution, token);
            }
        });
        log.debug("users to update: {}", toUpdate);
    }


    private void updateInstitutionState(Institution institution, Token token, RelationshipState state) {
        log.info("START -  update institution {} with state {}", institution, state);
        Institution newInstitution = new Institution(institution);
        try {
            if (newInstitution.getOnboarding() != null) {
                newInstitution.getOnboarding().stream()
                        .filter(onboarding -> token.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                        .findFirst().ifPresentOrElse(onboarding -> {
                                    onboarding.setStatus(state);
                                    onboarding.setUpdatedAt(OffsetDateTime.now());
                                },
                                () -> {
                                    throw new ResourceNotFoundException(String.format(PRODUCTS_NOT_FOUND_ERROR.getMessage(), institution.getId()),
                                            PRODUCTS_NOT_FOUND_ERROR.getCode());
                                });
            }
            log.debug("{} product {} to institution {}", state, token.getProductId(), institution.getExternalId());
            institutionConnector.save(newInstitution);
        } catch (Exception e) {
            rollbackFirstStepOfUpdate(token);
        }
    }

    private Token updateToken(Token token, RelationshipState state) {
        log.info("START - update token {} with state {}", token.getId(), state);
        token.setUpdatedAt(OffsetDateTime.now());
        token.setStatus(state);
        return tokenConnector.save(token);
    }

    private Token createToken(OnboardingRequest request, Institution institution, String digest) {
        log.info("START - createToken for institution {} and product {}", institution.getExternalId(), request.getProductId());
        return tokenConnector.save(convertToToken(request, institution, digest));
    }

    private void updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String tokenId) {
        log.info("START - update institution {}", institution.getId());
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
            log.info("END - institution {} updated", institution.getId());
        } catch (Exception e) {
            rollbackFirstStep(tokenId);
        }
    }

    private void createUsers(List<OnboardedUser> toUpdate, List<String> toDelete, OnboardingRequest request, Institution institution, Token token) {
        try {
            request.getUsers()
                    .forEach(userToOnboard -> checkIfNewUser(toUpdate, toDelete, userToOnboard, institution.getId(), request));
            log.debug("users to delete: {}", toDelete);
            log.debug("users to update: {}", toUpdate);
            token.setUsers(toDelete);
            token.getUsers().addAll(toUpdate.stream().map(OnboardedUser::getId).collect(Collectors.toList()));
            tokenConnector.save(token);
        } catch (Exception e) {
            rollbackSecondStep(toUpdate, toDelete, institution, token.getId());
        }

    }

    private void checkIfNewUser(List<OnboardedUser> toUpdate, List<String> toDelete, UserToOnboard user, String institutionId, OnboardingRequest request) {
        List<OnboardedUser> onboardedUsers = userConnector.getByUser(user.getId());
        if (!onboardedUsers.isEmpty()) {
            onboardedUsers.forEach(onboardedUser -> {
                toUpdate.add(onboardedUser);
                if (onboardedUser.getBindings().get(institutionId) != null) {
                    onboardedUser.getBindings().get(institutionId).put(request.getProductId(), constructProduct(user, request.getInstitutionUpdate().getInstitutionType()));
                } else {
                    onboardedUser.getBindings().put(institutionId, constructProductMap(request, user));
                }
                onboardedUser.setUpdatedAt(OffsetDateTime.now());
                userConnector.save(onboardedUser);
            });
        } else {
            OnboardedUser newUser = new OnboardedUser();
            newUser.setUser(user.getId());
            newUser.setBindings(constructMap(user, request, institutionId));
            newUser.setCreatedAt(OffsetDateTime.now());
            newUser.setUpdatedAt(OffsetDateTime.now());
            OnboardedUser onboardedUser = userConnector.save(newUser);
            toDelete.add(onboardedUser.getId());
        }
    }

    public void rollbackSecondStep(List<OnboardedUser> toUpdate, List<String> toDelete, Institution institution, String tokenId) {
        log.info("START - rollbackSecondStep - rollback token {}, institution {} and {} users", tokenId, institution.getId(), toUpdate.size() + toDelete.size());
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        toDelete.forEach(userConnector::deleteById);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public void rollbackSecondStepOfUpdate(List<OnboardedUser> toUpdate, Institution institution, Token token) {
        log.info("START - rollbackSecondStep - rollback token {}, institution {} and {} users", token.getId(), institution.getId(), toUpdate.size());
        tokenConnector.save(token);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStep(String tokenId) {
        log.info("START - rollbackFirstStep - rollback token");
        tokenConnector.deleteById(tokenId);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStepOfUpdate(Token token) {
        log.info("START - rollbackFirstStep - rollback token");
        tokenConnector.save(token);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    public List<Institution> findInstitutionWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        return institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
    }

    public Institution findInstitutionByExternalId(String institutionExternalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(institutionExternalId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), "institutionExternalId : " + institutionExternalId), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("founded institution having externalId: {}", institutionExternalId);
        return opt.get();

    }

    public OnboardedUser getUserById(String s) {
        return userConnector.getById(s);
    }

    public Product getProductById(String productId) {
        return productConnector.getProductById(productId);
    }

    public Institution findInstitutionById(String institutionId) {
        Optional<Institution> opt = institutionConnector.findById(institutionId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), institutionId, null), INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("Founded institution {}", opt.get().getExternalId());
        return opt.get();
    }

    public List<OnboardedUser> findOnboardedManager(String institutionId, String productId) {
        return userConnector.findOnboardedManager(institutionId, productId);
    }

    public List<OnboardedUser> getUserByUser(String userId) {
        return userConnector.getByUser(userId);
    }
}
