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

        if(institution.get().getOnboarding()!=null) {
            Optional<Onboarding> optionalOnboarding = institution.get().getOnboarding().stream()
                    .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                    .findAny();
            if (optionalOnboarding.isPresent() && !productRelationshipStates.contains(optionalOnboarding.get().getStatus()))
                throw new InvalidRequestException(MANAGER_FOUND_ERROR.getMessage(), MANAGER_FOUND_ERROR.getCode());

        }

        validateOverridingData(request.getInstitutionUpdate(), institution.get());
        List<GeographicTaxonomies> geographicTaxonomies = getGeographicTaxonomy(request);
        verifyUsers(request.getUsers());

        persist(request, institution.get(), geographicTaxonomies);
    }

    private void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        if ((InstitutionType.PG.equals(institutionUpdate.getInstitutionType())
                || InstitutionType.PA.equals(institutionUpdate.getInstitutionType()))
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
        if (request.getInstitutionUpdate().getGeographicTaxonomyCodes() != null) {
            geographicTaxonomies = request.getInstitutionUpdate().getGeographicTaxonomyCodes().stream
                    ().map(geoTaxonomiesConnector::getExtByCode).collect(Collectors.toList());
            if (geographicTaxonomies.isEmpty())
                throw new ResourceNotFoundException(String.format(GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), request.getInstitutionUpdate().getGeographicTaxonomyCodes()),
                        GEO_TAXONOMY_CODE_NOT_FOUND.getCode());
        }
        return geographicTaxonomies;
    }

    private void verifyUsers(List<OnboardedUser> users) {
        users.forEach(onboardedUser -> {
            if (!verifyUsersRole.contains(onboardedUser.getRole()))
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), onboardedUser.getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
        });
    }

    private void persist(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies) {
        createToken(request, institution);
        createUsers(request);
        updateInstitution(request, institution, geographicTaxonomies);
        //TODO: ADD TRY CATCH CON ROLLBACK PER STEP
    }

    private List<String> createUsers(OnboardingRequest request) {
        return request.getUsers()
                .stream()
                .map(onboardedUser -> {
                    onboardedUser.setCreatedAt(OffsetDateTime.now());
                    return userConnector.save(onboardedUser).getId();
                })
                .collect(Collectors.toList());
    }

    private String createToken(OnboardingRequest request, Institution institution) {
        return tokenConnector.save(convertToToken(request, institution)).getId();
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

        // token.setExpiringDate();
        // token.setCheckSum();

        return token;
    }

    private String updateInstitution(OnboardingRequest request, Institution institution, List<GeographicTaxonomies> geographicTaxonomies) {
        institution.getOnboarding().add(constructOnboarding(request));
        institution.setUpdatedAt(OffsetDateTime.now());
        institution.setGeographicTaxonomies(geographicTaxonomies);
        return institutionConnector.save(institution).getId();
    }

    private Onboarding constructOnboarding(OnboardingRequest request) {
        Onboarding onboarding = new Onboarding();

        onboarding.setProductId(request.getProductId());
        onboarding.setBilling(request.getBillingRequest());
        onboarding.setPricingPlan(request.getPricingPlan());
        onboarding.setCreatedAt(OffsetDateTime.now());
        onboarding.setUpdatedAt(OffsetDateTime.now());

        if (request.getContract() != null)
            onboarding.setContract(request.getContract().getPath());

        if (request.getInstitutionUpdate() != null)
            onboarding.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));

        //onboarding.setPremium();

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
