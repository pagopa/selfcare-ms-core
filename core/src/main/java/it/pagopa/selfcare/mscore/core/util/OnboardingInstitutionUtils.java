package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import it.pagopa.selfcare.mscore.model.product.Product;
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

    public static void validatePaOnboarding(Billing billing) {
        if (billing == null
                || StringUtils.isEmpty(billing.getVatNumber())
                || StringUtils.isEmpty(billing.getRecipientCode())) {
            throw new InvalidRequestException(CustomError.ONBOARDING_BILLING_ERROR.getCode(), CustomError.ONBOARDING_BILLING_ERROR.getMessage());
        }
    }

    public static void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        log.info("START - validateOverridingData for institution having externalId: {}", institution.getExternalId());
        if (InstitutionType.PA == institutionUpdate.getInstitutionType()
                && (!validateParameter(institution.getDescription(), institutionUpdate.getDescription())
                || !validateParameter(institution.getTaxCode(), institutionUpdate.getTaxCode())
                || !validateParameter(institution.getDigitalAddress(), institutionUpdate.getDigitalAddress())
                || !validateParameter(institution.getZipCode(), institutionUpdate.getZipCode())
                || !validateParameter(institution.getAddress(), institutionUpdate.getAddress()))) {
            throw new InvalidRequestException(String.format(ONBOARDING_INVALID_UPDATES.getMessage(), institution.getExternalId()), ONBOARDING_INVALID_UPDATES.getCode());
        }
        log.info("END - validateOverridingData without error");
    }

    private static boolean validateParameter(String startValue, String toValue) {
        if (!StringUtils.isEmpty(startValue) && !StringUtils.isEmpty(toValue)) {
            return startValue.equalsIgnoreCase(toValue);
        }
        return !StringUtils.isEmpty(startValue) || StringUtils.isEmpty(toValue);
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

    public static List<String> getValidManagerToOnboard(List<UserToOnboard> users, Token token) {
        log.info("START - getOnboardingValidManager for users list size: {}", users.size());
        List<String> response;
        if (token != null) {
            response = token.getUsers().stream()
                    .filter(tokenUser -> PartyRole.MANAGER == tokenUser.getRole())
                    .map(TokenUser::getUserId)
                    .collect(Collectors.toList());
        } else {
            response = users.stream()
                    .filter(userToOnboard -> PartyRole.MANAGER == userToOnboard.getRole())
                    .map(UserToOnboard::getId)
                    .collect(Collectors.toList());
        }
        if (response.isEmpty()) {
            throw new InvalidRequestException(CustomError.MANAGER_NOT_FOUND_GENERIC_ERROR.getMessage(), CustomError.MANAGER_NOT_FOUND_GENERIC_ERROR.getCode());
        }
        return response;
    }

    public static List<String> getOnboardedValidManager(Token token) {
        List<String> managerList = new ArrayList<>();
        if (token.getUsers() != null) {
            managerList = token.getUsers().stream().filter(tokenUser -> PartyRole.MANAGER == tokenUser.getRole())
                    .map(TokenUser::getUserId).collect(Collectors.toList());
        }
        if (managerList.isEmpty()) {
            throw new InvalidRequestException(CustomError.MANAGER_NOT_FOUND_ERROR.getMessage(), CustomError.MANAGER_NOT_FOUND_ERROR.getCode());
        }
        return managerList;
    }

    public static OnboardingRequest constructOnboardingRequest(OnboardingLegalsRequest onboardingLegalsRequest) {
        OnboardingRequest request = new OnboardingRequest();
        request.setProductId(onboardingLegalsRequest.getProductId());
        request.setProductName(onboardingLegalsRequest.getProductName());
        request.setUsers(onboardingLegalsRequest.getUsers());
        request.setInstitutionExternalId(onboardingLegalsRequest.getInstitutionExternalId());
        request.setContract(onboardingLegalsRequest.getContract());
        request.setSignContract(true);
        request.setTokenType(TokenType.LEGALS);
        return request;
    }

    public static OnboardingRequest constructOnboardingRequest(Token token, Institution institution, Product product) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setProductId(token.getProductId());
        onboardingRequest.setProductName(product.getTitle());
        onboardingRequest.setPricingPlan(retrivePricingPlan(token, institution, onboardingRequest));
        Contract contract = new Contract();
        contract.setPath(token.getContractTemplate());
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setInstitutionType((institution.getInstitutionType()));
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setContract(contract);
        onboardingRequest.setSignContract(true);
        onboardingRequest.setBillingRequest(retriveBilling(token, institution));
        return onboardingRequest;
    }

    private static String retrivePricingPlan(Token token, Institution institution, OnboardingRequest onboardingRequest) {
        institution.getOnboarding().stream()
                .filter(onboarding -> onboarding.getTokenId().equals(token.getId())
                        && onboarding.getProductId().equals(token.getProductId()))
                .map(Onboarding::getPricingPlan)
                .forEach(onboardingRequest::setPricingPlan);
        return onboardingRequest.getPricingPlan();
    }

    private static Billing retriveBilling(Token token, Institution institution) {
        for (Onboarding onboarding : institution.getOnboarding()) {
            if (onboarding.getTokenId().equals(token.getId())) {
                return onboarding.getBilling();
            }
        }

        return institution.getBilling();
    }


    public static Onboarding constructOnboarding(OnboardingRequest request, Institution institution) {
        Onboarding onboarding = new Onboarding();

        onboarding.setProductId(request.getProductId());
        onboarding.setBilling(request.getBillingRequest());
        onboarding.setPricingPlan(request.getPricingPlan());
        onboarding.setCreatedAt(OffsetDateTime.now());
        onboarding.setUpdatedAt(OffsetDateTime.now());
        if (request.getContract() != null) {
            onboarding.setContract(request.getContract().getPath());
        }

        onboarding.setStatus(getStatus(request.getInstitutionUpdate(),
                institution.getInstitutionType(), institution.getOrigin(), request.getProductId()));

        return onboarding;
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

    public static void validateSaOnboarding(String vatNumber) {
        if (StringUtils.isEmpty(vatNumber)){
            throw new InvalidRequestException(CustomError.ONBOARDING_BILLING_VATNUMBER_ERROR.getCode(), CustomError.ONBOARDING_BILLING_VATNUMBER_ERROR.getMessage());
        }
    }
}
