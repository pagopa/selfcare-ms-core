package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.*;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final List<RelationshipState> productRelationshipStates =
            List.of(RelationshipState.PENDING,
                    RelationshipState.REJECTED,
                    RelationshipState.TOBEVALIDATED);

    private final List<PartyRole> verifyUsersRole =
            List.of(PartyRole.MANAGER,
                    PartyRole.DELEGATE);

    private final InstitutionConnector institutionConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;
    private final TokenConnector tokenConnector;
    private final UserConnector userConnector;

    public OnboardingServiceImpl(InstitutionConnector institutionConnector, GeoTaxonomiesConnector geoTaxonomiesConnector, TokenConnector tokenConnector, UserConnector userConnector) {
        this.institutionConnector = institutionConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
        this.tokenConnector = tokenConnector;
        this.userConnector = userConnector;
    }


    @Override
    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {
        log.info("Onboarding institution having externalId {}", request.getInstitutionExternalId());

        Optional<Institution> institution = institutionConnector.findByExternalId(request.getInstitutionExternalId());
        if (institution.isEmpty())
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), null, request.getInstitutionExternalId()), INSTITUTION_NOT_FOUND.getCode());

        checkIfProductAlreadyOnboarded(institution.get(), request);

        validateOverridingData(request.getInstitutionUpdate(), institution.get());

        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);

        if (InstitutionType.PA.equals(request.getInstitutionUpdate().getInstitutionType()))
            verifyPaUsers(request.getUsers());

        else if (InstitutionType.PG.equals(request.getInstitutionUpdate().getInstitutionType()))
            verifyPgUsers(request.getUsers());

        persist(request, institution.get(), geographicTaxonomies);
    }

    private void checkIfProductAlreadyOnboarded(Institution institution, OnboardingRequest request) {
        if (institution.getOnboarding() != null) {
            Optional<Onboarding> optionalOnboarding = institution.getOnboarding().stream()
                    .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                    .findAny();
            if (optionalOnboarding.isPresent() && !productRelationshipStates.contains(optionalOnboarding.get().getStatus()))
                throw new InvalidRequestException(MANAGER_FOUND_ERROR.getMessage(), MANAGER_FOUND_ERROR.getCode());
        }
    }

    private void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        //TODO: AGGIUNGERE INSTITUTIONTYPE EQUALS PG SE DATI OBBLIGATORI
        if (InstitutionType.PA.equals(institutionUpdate.getInstitutionType())
                && (!institution.getDescription().equalsIgnoreCase(institutionUpdate.getDescription())
                || !institution.getTaxCode().equalsIgnoreCase(institutionUpdate.getTaxCode())
                || !institution.getDigitalAddress().equalsIgnoreCase(institutionUpdate.getDigitalAddress())
                || !institution.getZipCode().equalsIgnoreCase(institutionUpdate.getZipCode())
                || !institution.getAddress().equalsIgnoreCase(institutionUpdate.getAddress()))) {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
    }

    private List<GeographicTaxonomies> getGeographicTaxonomy(OnboardingRequest request) {
        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        if (request.getInstitutionUpdate().getGeographicTaxonomyCodes() != null &&
                !request.getInstitutionUpdate().getGeographicTaxonomyCodes().isEmpty()) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomyCodes().stream
                    ().map(geoTaxonomiesConnector::getExtByCode).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty())
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomyCodes()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
        }
        return geographicTaxonomies;
    }

    private void verifyPgUsers(List<OnboardedUser> users) {
        users.forEach(onboardedUser -> {
            if (!PartyRole.MANAGER.equals(onboardedUser.getRole()))
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), onboardedUser.getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
        });
    }

    private void verifyPaUsers(List<OnboardedUser> users) {
        users.forEach(onboardedUser -> {
            if (!verifyUsersRole.contains(onboardedUser.getRole()))
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), onboardedUser.getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
        });
    }

    private void persist(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies) {
        String tokenId = createToken(request, institution);
        updateInstitution(request, institution, geographicTaxonomies, tokenId);
        createUsers(request, institution, tokenId);
    }

    private String createToken(OnboardingRequest request, Institution institution) {
        return tokenConnector.save(convertToToken(request, institution)).getId();
    }

    private void updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies, String tokenId) {
        Institution newInstitution = new Institution(institution);
        try {
            if (newInstitution.getOnboarding() != null)
                newInstitution.getOnboarding().add(constructOnboarding(request));
            else {
                newInstitution.setOnboarding(List.of(constructOnboarding(request)));
            }
            newInstitution.setUpdatedAt(OffsetDateTime.now());
            newInstitution.setGeographicTaxonomies(geographicTaxonomies);

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
    }

    private Token convertToToken(OnboardingRequest request, Institution institution) {
        Token token = new Token();
        if (request.getContract() != null)
            token.setContract(request.getContract().getPath());

        token.setCreatedAt(OffsetDateTime.now());
        token.setInstitutionId(institution.getId());
        token.setUsers(request.getUsers().stream().map(OnboardedUser::getUser).collect(Collectors.toList()));
        token.setProductId(request.getProductId());

        if (request.getInstitutionUpdate() != null)
            token.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));

        // TODO: token.setExpiringDate() token.setCheckSum();

        return token;
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

    private static Map<String, Map<String, Product>> constructMap(OnboardedUser p, OnboardingRequest request, String institutionId) {
        Map<String, Map<String, Product>> map = new HashMap<>();
        map.put(institutionId, constructProductMap(request, p));
        return map;
    }

    private static Product constructProduct(OnboardedUser p, InstitutionType institutionType) {
        Product product = new Product();
        product.setRoles(List.of(p.getRole().name()));
        product.setStatus(retrieveStatusFromInstitutionType(institutionType));
        product.setCreatedAt(OffsetDateTime.now());
        return product;
    }

    private static RelationshipState retrieveStatusFromInstitutionType(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
                return RelationshipState.TOBEVALIDATED;
        }
    }

    private static Map<String, Product> constructProductMap(OnboardingRequest onboardingInstitutionRequest, OnboardedUser p) {
        Map<String, Product> productMap = new HashMap<>();
        productMap.put(onboardingInstitutionRequest.getProductId(), constructProduct(p, onboardingInstitutionRequest.getInstitutionUpdate().getInstitutionType()));
        return productMap;
    }

    private void rollbackSecondStep(List<OnboardedUser> toUpdate, List<String> toDelete, Institution institution, String tokenId) {
        tokenConnector.deleteById(tokenId);
        institutionConnector.save(institution);
        toUpdate.forEach(userConnector::save);
        toDelete.forEach(userConnector::deleteById);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private void rollbackFirstStep(String tokenId) {
        tokenConnector.deleteById(tokenId);
        throw new InvalidRequestException(ONBOARDING_OPERATION_ERROR.getMessage(), ONBOARDING_OPERATION_ERROR.getCode());
    }

    private Onboarding constructOnboarding(OnboardingRequest request) {
        Onboarding onboarding = new Onboarding();

        onboarding.setProductId(request.getProductId());
        onboarding.setBilling(request.getBillingRequest());
        onboarding.setPricingPlan(request.getPricingPlan());
        onboarding.setCreatedAt(OffsetDateTime.now());

        if (request.getContract() != null)
            onboarding.setContract(request.getContract().getPath());

        if (request.getInstitutionUpdate() != null)
            onboarding.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));

        //TODO: onboarding.setPremium();

        return onboarding;
    }

    private RelationshipState getStatus(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
                return RelationshipState.TOBEVALIDATED;
        }
    }
}
