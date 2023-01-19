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

        Optional<Onboarding> optionalOnboarding = institution.get().getOnboarding().stream()
                .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                .findAny();

        if (optionalOnboarding.isPresent() && !productRelationshipStates.contains(optionalOnboarding.get().getStatus()))
            throw new InvalidRequestException(MANAGER_FOUND_ERROR.getMessage(), MANAGER_FOUND_ERROR.getCode());

        validateOverridingData(request.getInstitutionUpdate(), institution.get());

        List<GeographicTaxonomies> geographicTaxonomies = new ArrayList<>();
        if (request.getInstitutionUpdate().getGeographicTaxonomyCodes() != null) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomyCodes().stream
                    ().map(geoTaxonomiesConnector::getExtByCode).collect(Collectors.toList());
            if(geographicTaxonomies.isEmpty())
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(),request.getInstitutionUpdate().getGeographicTaxonomyCodes()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
        }

        verifyUsers(request.getUsers());

        List<String> usersList = createUsers(request);

        try {
            createToken(request, institution.get().getId(), usersList);
        } catch (Exception e) {
            usersList.forEach(userConnector::deleteById);
            throw new InvalidRequestException("", "");
        }

        updateInstitution(request, institution.get(), geographicTaxonomies);
    }


    private void updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies) {
        institution.getOnboarding().add(constructOnboarding(request));
        institution.setGeographicTaxonomies(geographicTaxonomies);
        institutionConnector.save(institution);
    }

    private Onboarding constructOnboarding(OnboardingRequest request) {
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId(request.getProductId());

        if (request.getContract() != null)
            onboarding.setContract(request.getContract().getPath());

        //onboarding.setPremium();

        onboarding.setBilling(request.getBillingRequest());
        onboarding.setPricingPlan(request.getPricingPlan());
        onboarding.setCreatedAt(OffsetDateTime.now());
        onboarding.setUpdatedAt(OffsetDateTime.now());

        if (request.getInstitutionUpdate() != null)
            setOnboardingStatus(onboarding, request.getInstitutionUpdate().getInstitutionType());

        return onboarding;
    }

    private void createToken(OnboardingRequest request, String institutionId, List<String> usersList) {
        tokenConnector.save(convertToToken(request, institutionId, usersList));
    }

    private Token convertToToken(OnboardingRequest request, String institutionId, List<String> users) {
        Token token = new Token();
        if (request.getContract() != null)
            token.setContract(request.getContract().getPath());
        token.setInstitutionId(institutionId);
        token.setUsers(users);
        token.setProductId(request.getProductId());

        //token.setExpiringDate();
        // token.setCheckSum();

        if (request.getInstitutionUpdate() != null)
            setTokenStatus(token, request.getInstitutionUpdate().getInstitutionType());

        return token;
    }

    private void setTokenStatus(Token token, InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                token.setStatus(RelationshipState.PENDING);
                break;
            case PG:
                token.setStatus(RelationshipState.ACTIVE);
                break;
            default:
                token.setStatus(RelationshipState.TOBEVALIDATED);
                break;
        }
    }

    private void setOnboardingStatus(Onboarding onboarding, InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                onboarding.setStatus(RelationshipState.PENDING);
                break;
            case PG:
                onboarding.setStatus(RelationshipState.ACTIVE);
                break;
            default:
                onboarding.setStatus(RelationshipState.TOBEVALIDATED);
                break;
        }
    }

    private List<String> createUsers(OnboardingRequest request) {
        return request.getUsers()
                .stream()
                .map(onboardedUser -> userConnector.save(onboardedUser).getUser())
                .collect(Collectors.toList());
    }

    private void verifyUsers(List<OnboardedUser> users) {
        users.forEach(onboardedUser -> {
            if (!verifyUsersRole.contains(onboardedUser.getRole()))
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), onboardedUser.getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
        });
    }

    private boolean validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        if (InstitutionType.PG.equals(institutionUpdate.getInstitutionType())
                || InstitutionType.PA.equals(institutionUpdate.getInstitutionType())) {
            return institutionUpdate.getDescription().equalsIgnoreCase(institution.getDescription())
                    && institutionUpdate.getTaxCode().equalsIgnoreCase(institution.getTaxCode())
                    && institutionUpdate.getDigitalAddress().equalsIgnoreCase(institution.getDigitalAddress())
                    && institutionUpdate.getZipCode().equalsIgnoreCase(institution.getZipCode())
                    && institutionUpdate.getAddress().equalsIgnoreCase(institution.getAddress());
        } else {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
    }
}
