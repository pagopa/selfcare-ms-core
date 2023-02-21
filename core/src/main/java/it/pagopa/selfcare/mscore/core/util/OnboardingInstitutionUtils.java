package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.UserToOnboard;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.ONBOARDING_INVALID_UPDATES;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.productRelationshipStates;

@Slf4j
public class OnboardingInstitutionUtils {

    private OnboardingInstitutionUtils() {
    }

    public static void verifyUsers(List<UserToOnboard> users, List<PartyRole> role) {
        List<UserToOnboard> userList = new ArrayList<>();
        users.forEach(onboardedUser -> {
            if (!role.contains(onboardedUser.getRole())) {
                userList.add(onboardedUser);
            }
        });
        if (!userList.isEmpty()) {
            List<String> userIdList = userList.stream().map(UserToOnboard::getId).collect(Collectors.toList());
            throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), StringUtils.join(userIdList, ", ")), ROLES_NOT_ADMITTED_ERROR.getCode());
        }
    }

    public static void checkIfProductAlreadyOnboarded(Institution institution, OnboardingRequest request) {
        log.info("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", institution.getExternalId(), request.getProductId());
        if (institution.getOnboarding() != null) {
            Optional<Onboarding> optionalOnboarding = institution.getOnboarding().stream()
                    .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                    .findAny();
            if (optionalOnboarding.isPresent() && !productRelationshipStates.contains(optionalOnboarding.get().getStatus())) {
                throw new InvalidRequestException(String.format(PRODUCT_ALREADY_ONBOARDED.getMessage(), request.getProductId(), institution.getExternalId()), PRODUCT_ALREADY_ONBOARDED.getCode());
            }
        }
        log.info("END - checkIfProductAlreadyOnboarded without error");
    }

    public static void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        log.info("START - validateOverridingData for institution having externalId: {}", institution.getExternalId());
        //TODO: AGGIUNGERE INSTITUTIONTYPE EQUALS PG SE DATI OBBLIGATORI
        if (InstitutionType.PA == institutionUpdate.getInstitutionType()
                && (!institution.getDescription().equalsIgnoreCase(institutionUpdate.getDescription())
                || !institution.getTaxCode().equalsIgnoreCase(institutionUpdate.getTaxCode())
                || !institution.getDigitalAddress().equalsIgnoreCase(institutionUpdate.getDigitalAddress())
                || !institution.getZipCode().equalsIgnoreCase(institutionUpdate.getZipCode())
                || !institution.getAddress().equalsIgnoreCase(institutionUpdate.getAddress()))) {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
        log.info("END - validateOverridingData without error");
    }

    public static Token convertToToken(OnboardingRequest request, Institution institution, String digest) {
        log.info("START - convertToToken for institution having externalId: {} and digest: {}", institution.getExternalId(), digest);
        Token token = new Token();
        if (request.getContract() != null) {
            token.setContract(request.getContract().getPath());
        }
        token.setCreatedAt(OffsetDateTime.now());
        token.setInstitutionId(institution.getId());
        token.setProductId(request.getProductId());
        token.setChecksum(digest);

        if (request.getInstitutionUpdate() != null) {
            token.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));
        }
        log.info("END - convertToToken");
        return token;
    }

    public static OnboardedProduct constructProduct(UserToOnboard p, OnboardingRequest request) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setProductId(request.getProductId());
        onboardedProduct.setRole(p.getRole());
        if (request.getContract() != null) {
            onboardedProduct.setContract(request.getContract().getPath());
        }
        onboardedProduct.setProductRoles(p.getProductRole());
        if (request.getInstitutionUpdate() != null) {
            onboardedProduct.setStatus(retrieveStatusFromInstitutionType(request.getInstitutionUpdate().getInstitutionType()));
        }
        onboardedProduct.setCreatedAt(OffsetDateTime.now());
        if (p.getEnv() != null) {
            onboardedProduct.setEnv(p.getEnv());
        }
        return onboardedProduct;
    }

    public static RelationshipState retrieveStatusFromInstitutionType(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
                return RelationshipState.TOBEVALIDATED;
        }
    }

    public static Onboarding constructOnboarding(OnboardingRequest request) {
        Onboarding onboarding = new Onboarding();

        onboarding.setProductId(request.getProductId());
        onboarding.setBilling(request.getBillingRequest());
        onboarding.setPricingPlan(request.getPricingPlan());
        onboarding.setCreatedAt(OffsetDateTime.now());

        if (request.getContract() != null) {
            onboarding.setContract(request.getContract().getPath());
        }
        if (request.getInstitutionUpdate() != null) {
            onboarding.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));
        }
        return onboarding;
    }

    private static RelationshipState getStatus(InstitutionType institutionType) {
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
