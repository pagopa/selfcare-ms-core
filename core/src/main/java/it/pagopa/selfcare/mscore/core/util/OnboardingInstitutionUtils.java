package it.pagopa.selfcare.mscore.core.util;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.ONBOARDING_INVALID_UPDATES;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.productRelationshipStates;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.verifyUsersRole;

@Slf4j
public class OnboardingInstitutionUtils {

    private OnboardingInstitutionUtils() {
    }

    public static void verifyPgUsers(List<OnboardedUser> users) {
        if(users.size() != 1){
            if (PartyRole.MANAGER != users.get(0).getRole()) {
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), users.get(0).getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
            }
        }else{
            throw new InvalidRequestException(USERS_SIZE_NOT_ADMITTED.getMessage(), USERS_SIZE_NOT_ADMITTED.getCode());
        }
    }

    public static void verifyUsers(List<OnboardedUser> users) {
        users.forEach(onboardedUser -> {
            if (!verifyUsersRole.contains(onboardedUser.getRole())) {
                throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), onboardedUser.getRole()), ROLES_NOT_ADMITTED_ERROR.getCode());
            }
        });
    }

    public static String createDigest(File pdf) {
        log.info("START- createDigest for pdf {}", pdf.getName());
        DSSDocument document = new FileDocument(pdf);
        return document.getDigest(DigestAlgorithm.SHA256);
    }

    public static void checkIfProductAlreadyOnboarded(Institution institution, OnboardingRequest request) {
        log.info("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", institution.getExternalId(), request.getProductId());
        if (institution.getOnboarding() != null) {
            Optional<Onboarding> optionalOnboarding = institution.getOnboarding().stream()
                    .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId()))
                    .findAny();
            if (optionalOnboarding.isPresent() && !productRelationshipStates.contains(optionalOnboarding.get().getStatus())) {
                throw new InvalidRequestException(String.format(PRODUCT_ALREADY_ONBOARDED.getMessage(),request.getProductId(), institution.getExternalId()), PRODUCT_ALREADY_ONBOARDED.getCode());
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
        token.setUsers(request.getUsers().stream().map(OnboardedUser::getUser).collect(Collectors.toList()));
        token.setProductId(request.getProductId());
        token.setChecksum(digest);

        if (request.getInstitutionUpdate() != null) {
            token.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));
        }

        // TODO: token.setExpiringDate();
        log.info("END - convertToToken");
        return token;
    }

    public static Map<String, Map<String, Product>> constructMap(OnboardedUser p, OnboardingRequest request, String institutionId) {
        Map<String, Map<String, Product>> map = new HashMap<>();
        map.put(institutionId, constructProductMap(request, p));
        return map;
    }

    public static Product constructProduct(OnboardedUser p, InstitutionType institutionType) {
        Product product = new Product();
        product.setRoles(List.of(p.getRole().name()));
        product.setStatus(retrieveStatusFromInstitutionType(institutionType));
        product.setCreatedAt(OffsetDateTime.now());
        return product;
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

    public static Map<String, Product> constructProductMap(OnboardingRequest onboardingInstitutionRequest, OnboardedUser p) {
        Map<String, Product> productMap = new HashMap<>();
        productMap.put(onboardingInstitutionRequest.getProductId(), constructProduct(p, onboardingInstitutionRequest.getInstitutionUpdate().getInstitutionType()));
        return productMap;
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
        //TODO: onboarding.setPremium();

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

    public static OnboardedUser getValidManager(List<OnboardedUser> users) {
        log.info("START - getValidManager for users list size: {}", users.size());
        return users.stream()
                .filter(onboardedUser -> PartyRole.MANAGER == onboardedUser.getRole())
                .findFirst().orElseThrow(() -> new InvalidRequestException(MANAGER_NOT_FOUND_ERROR.getMessage(), MANAGER_NOT_FOUND_ERROR.getCode()));
    }
}
