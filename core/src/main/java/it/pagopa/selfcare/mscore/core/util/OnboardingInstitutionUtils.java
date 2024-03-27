package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.commons.base.utils.Origin;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.ONBOARDING_INVALID_UPDATES;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.PRODUCT_RELATIONSHIP_STATES;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingInstitutionUtils {

    public static void checkIfProductAlreadyOnboarded(Institution institution, String productId) {
        log.info("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", institution.getExternalId(), productId);
        if (institution.getOnboarding() != null) {
            Optional<Onboarding> optionalOnboarding = institution.getOnboarding().stream()
                    .filter(onboarding -> productId.equalsIgnoreCase(onboarding.getProductId())
                            && RelationshipState.ACTIVE == onboarding.getStatus())
                    .findAny();
            if (optionalOnboarding.isPresent() && !PRODUCT_RELATIONSHIP_STATES.contains(optionalOnboarding.get().getStatus())) {
                throw new ResourceConflictException(String.format(CustomError.PRODUCT_ALREADY_ONBOARDED.getMessage(), productId, institution.getExternalId()), CustomError.PRODUCT_ALREADY_ONBOARDED.getCode());
            }
        }
        log.info("END - checkIfProductAlreadyOnboarded without error");
    }

    public static void validateOnboarding(Billing billing, boolean checkRecipientCode) {
        if (billing == null
                || StringUtils.isEmpty(billing.getVatNumber())
                || (checkRecipientCode && StringUtils.isEmpty(billing.getRecipientCode()))) {
            throw new InvalidRequestException(CustomError.ONBOARDING_BILLING_ERROR.getCode(), CustomError.ONBOARDING_BILLING_ERROR.getMessage());
        }
    }

    public static void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        log.info("START - validateOverridingData for institution having externalId: {} and origin: {}", institution.getExternalId(), institution.getOrigin());
        if (Origin.IPA.getValue().equalsIgnoreCase(institution.getOrigin())) {
            validateIpaOverriding(institutionUpdate, institution);
        } else if (!Origin.INFOCAMERE.getValue().equalsIgnoreCase(institution.getOrigin())) {
            validateDefaultOverriding(institutionUpdate, institution);
        }

        log.info("END - validateOverridingData without error");
    }

    private static void validateIpaOverriding(InstitutionUpdate institutionUpdate, Institution institution) {
        if (isInvalidOverride(institution.getDescription(), institutionUpdate.getDescription())
                || isInvalidOverride(institution.getTaxCode(), institutionUpdate.getTaxCode())
                || isInvalidOverride(institution.getDigitalAddress(), institutionUpdate.getDigitalAddress())
                || isInvalidOverride(institution.getZipCode(), institutionUpdate.getZipCode())
                || isInvalidOverride(institution.getAddress(), institutionUpdate.getAddress())
        ) {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
    }

    private static void validateDefaultOverriding(InstitutionUpdate institutionUpdate, Institution institution) {
        if (isInvalidOverride(institution.getDigitalAddress(), institutionUpdate.getDigitalAddress())) {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
    }

    private static boolean isInvalidOverride(String startValue, String toValue) {
        if (!StringUtils.isEmpty(startValue) && !StringUtils.isEmpty(toValue)) {
            return !startValue.equalsIgnoreCase(toValue);
        }
        return StringUtils.isEmpty(startValue) && !StringUtils.isEmpty(toValue);
    }

    public static RelationshipState getStatus(InstitutionUpdate institutionUpdate, InstitutionType institutionType, String institutionOrigin, String productId) {
        if (Objects.nonNull(institutionUpdate) && Objects.nonNull(institutionUpdate.getInstitutionType())) {
            return getStatusByInstitutionType(institutionUpdate.getInstitutionType(), productId, institutionOrigin);
        }

        if (Objects.nonNull(institutionType)) {
            return getStatusByInstitutionType(institutionType, productId, institutionOrigin);
        }

        return null;
    }

    private static RelationshipState getStatusByInstitutionType(InstitutionType institutionType, String productId, String institutionOrigin) {
        switch (institutionType) {
            case PA:
            case SA:
            case AS:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
                if (InstitutionType.GSP == institutionType && "prod-interop".equals(productId) && "IPA".equals(institutionOrigin)) {
                    return RelationshipState.PENDING;
                }
                return RelationshipState.TOBEVALIDATED;

        }
    }

    public static void verifyUsers(List<UserToOnboard> users, List<PartyRole> role) {
        List<UserToOnboard> userList = new ArrayList<>();
        users.forEach(onboardedUser -> {
            if (!role.contains(onboardedUser.getRole())) {
                userList.add(onboardedUser);
            }
        });
        if (!userList.isEmpty()) {
            List<PartyRole> userRoleList = userList.stream().map(UserToOnboard::getRole).collect(Collectors.toList());
            throw new InvalidRequestException(String.format(CustomError.ROLES_NOT_ADMITTED_ERROR.getMessage(), StringUtils.join(userRoleList, ", ")), CustomError.ROLES_NOT_ADMITTED_ERROR.getCode());
        }
    }

    public static String getValidManagerId(List<UserToOnboard> users) {
        log.debug("START - getOnboardingValidManager for users list size: {}", users.size());

        return users.stream()
                .filter(userToOnboard -> PartyRole.MANAGER == userToOnboard.getRole())
                .map(UserToOnboard::getId)
                .findAny()
                .orElseThrow(() -> new InvalidRequestException(CustomError.MANAGER_NOT_FOUND_GENERIC_ERROR.getMessage(),
                        CustomError.MANAGER_NOT_FOUND_GENERIC_ERROR.getCode()));
    }

    public static OnboardedProduct constructOperatorProduct(UserToOnboard user, String productId) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setRelationshipId(UUID.randomUUID().toString());
        onboardedProduct.setProductId(productId);
        onboardedProduct.setRole(user.getRole());
        onboardedProduct.setProductRole(user.getProductRole());
        onboardedProduct.setStatus(RelationshipState.ACTIVE);
        onboardedProduct.setCreatedAt(OffsetDateTime.now());
        if (user.getEnv() != null) {
            onboardedProduct.setEnv(user.getEnv());
        } else {
            onboardedProduct.setEnv(Env.ROOT);
        }
        return onboardedProduct;
    }

    public static OnboardedProduct constructProduct(UserToOnboard userToOnboard, OnboardingRequest request, Institution institution) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setRelationshipId(UUID.randomUUID().toString());
        onboardedProduct.setProductId(request.getProductId());
        onboardedProduct.setRole(userToOnboard.getRole());
        onboardedProduct.setProductRole(userToOnboard.getProductRole());
        onboardedProduct.setStatus(getStatus(request.getInstitutionUpdate(),
                institution.getInstitutionType(), institution.getOrigin(), request.getProductId()));

        onboardedProduct.setCreatedAt(OffsetDateTime.now());
        onboardedProduct.setUpdatedAt(OffsetDateTime.now());
        if (userToOnboard.getEnv() != null) {
            onboardedProduct.setEnv(userToOnboard.getEnv());
        }
        return onboardedProduct;
    }

}
