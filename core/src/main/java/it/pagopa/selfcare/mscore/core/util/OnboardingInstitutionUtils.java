package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.constant.Env;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.onboarding.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;
import static it.pagopa.selfcare.mscore.constant.CustomError.ONBOARDING_INVALID_UPDATES;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.PRODUCT_RELATIONSHIP_STATES;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class OnboardingInstitutionUtils {

    public static void checkIfProductAlreadyOnboarded(Institution institution, OnboardingRequest request) {
        log.info("START - checkIfProductAlreadyOnboarded for institution having externalId: {} and productId: {}", institution.getExternalId(), request.getProductId());
        if (institution.getOnboarding() != null) {
            Optional<Onboarding> optionalOnboarding = institution.getOnboarding().stream()
                    .filter(onboarding -> request.getProductId().equalsIgnoreCase(onboarding.getProductId())
                            && RelationshipState.ACTIVE == onboarding.getStatus())
                    .findAny();
            if (optionalOnboarding.isPresent() && !PRODUCT_RELATIONSHIP_STATES.contains(optionalOnboarding.get().getStatus())) {
                throw new ResourceConflictException(String.format(PRODUCT_ALREADY_ONBOARDED.getMessage(), request.getProductId(), institution.getExternalId()), PRODUCT_ALREADY_ONBOARDED.getCode());
            }
        }
        log.info("END - checkIfProductAlreadyOnboarded without error");
    }

    public static void validatePaOnboarding(OnboardingRequest request) {
        if (request.getBillingRequest() == null
                || StringUtils.isEmpty(request.getBillingRequest().getVatNumber())
                || StringUtils.isEmpty(request.getBillingRequest().getRecipientCode())) {
            throw new InvalidRequestException(ONBOARDING_BILLING_ERROR.getCode(), ONBOARDING_BILLING_ERROR.getMessage());
        }
    }

    public static void validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        log.info("START - validateOverridingData for institution having externalId: {}", institution.getExternalId());
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

    public static RelationshipState getStatus(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return RelationshipState.PENDING;
            case PG:
                return RelationshipState.ACTIVE;
            default:
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
            throw new InvalidRequestException(String.format(ROLES_NOT_ADMITTED_ERROR.getMessage(), StringUtils.join(userRoleList, ", ")), ROLES_NOT_ADMITTED_ERROR.getCode());
        }
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
            throw new InvalidRequestException(MANAGER_NOT_FOUND_ERROR.getMessage(), MANAGER_NOT_FOUND_ERROR.getCode());
        }
        return response;
    }

    public static List<String> getOnboardedValidManager(List<OnboardedUser> users, String institutionId, String productId) {
        log.info("START - getValidManager for users list size: {}", users.size());
        List<String> response = new ArrayList<>();
        for (OnboardedUser onboardedUser : users) {
            onboardedUser.getBindings().stream()
                    .filter(userBinding -> institutionId.equalsIgnoreCase(userBinding.getInstitutionId()))
                    .flatMap(userBinding -> userBinding.getProducts().stream())
                    .filter(product -> productId.equalsIgnoreCase(product.getProductId()) && PartyRole.MANAGER == product.getRole())
                    .forEach(product -> response.add(onboardedUser.getId()));
        }
        if (response.isEmpty()) {
            throw new InvalidRequestException(MANAGER_NOT_FOUND_ERROR.getMessage(), MANAGER_NOT_FOUND_ERROR.getCode());
        }
        return response;
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

    public static OnboardingRequest constructOnboardingRequest(Token token, Institution institution) {
        OnboardingRequest onboardingRequest = new OnboardingRequest();
        onboardingRequest.setProductId(token.getProductId());
        onboardingRequest.setProductName(token.getProductId());
        Contract contract = new Contract();
        contract.setPath(token.getContractTemplate());
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setDescription(institution.getDescription());
        onboardingRequest.setInstitutionUpdate(institutionUpdate);
        onboardingRequest.setContract(contract);
        onboardingRequest.setSignContract(true);

        return onboardingRequest;
    }

    public static Onboarding constructOnboarding(OnboardingRequest request, Institution institution) {
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
        } else if (institution.getInstitutionType() != null) {
            onboarding.setStatus(getStatus(institution.getInstitutionType()));
        }

        return onboarding;
    }

    public static OnboardedProduct constructOperatorProduct(UserToOnboard user, OnboardingOperatorsRequest request) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setRelationshipId(UUID.randomUUID().toString());
        onboardedProduct.setProductId(request.getProductId());
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

    public static OnboardedProduct constructProduct(UserToOnboard p, OnboardingRequest request, Institution institution) {
        OnboardedProduct onboardedProduct = new OnboardedProduct();
        onboardedProduct.setRelationshipId(UUID.randomUUID().toString());
        onboardedProduct.setProductId(request.getProductId());
        onboardedProduct.setRole(p.getRole());
        if (request.getContract() != null) {
            onboardedProduct.setContract(request.getContract().getPath());
        }
        onboardedProduct.setProductRole(p.getProductRole());
        if (request.getInstitutionUpdate() != null) {
            onboardedProduct.setStatus(getStatus(request.getInstitutionUpdate().getInstitutionType()));
        } else if (institution.getInstitutionType() != null) {
            onboardedProduct.setStatus(getStatus(institution.getInstitutionType()));
        }
        onboardedProduct.setCreatedAt(OffsetDateTime.now());
        if (p.getEnv() != null) {
            onboardedProduct.setEnv(p.getEnv());
        }
        return onboardedProduct;
    }
}
