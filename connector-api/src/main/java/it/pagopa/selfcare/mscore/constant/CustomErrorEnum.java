package it.pagopa.selfcare.mscore.constant;

public enum CustomErrorEnum {

    GET_INSTITUTION_MANAGER_NOT_FOUND("0043", "Cannot find active manager for institution having externalId %s and product %s"),
    USER_NOT_FOUND_ERROR("0031", "User having userId %s not found"),
    USER_ALREADY_EXIST_ERROR("0033", "Person already exists"),
    USERS_SIZE_NOT_ADMITTED("0030","At least one user for onboarding PG"),
    PRODUCT_ALREADY_ONBOARDED("0032", "Product %s already onboarded for institution having externalId %s"),
    MANAGER_NOT_FOUND_ERROR("0033", "No onboarded managers for at least one of the institutions %s and product %s"),
    ROLES_NOT_ADMITTED_ERROR("0034","Roles %s are not admitted for this operation"),
    INSTITUTION_NOT_ONBOARDED("0004", "Institution having externalId %s is not onboarded for product %s"),
    INSTITUTION_NOT_FOUND("0036", "Cannot find Institution using institutionId %s and externalInstitutionId %s"),
    INSTITUTION_LEGAL_NOT_FOUND("0037", "Institution with externalInstitutionId %s is not related to user"),
    CREATE_INSTITUTION_CONFLICT("0038", "Institution having externalId %s already exists"),
    CREATE_INSTITUTION_NOT_FOUND("0039", "Institution having externalId %s not exists in registry"),
    GET_INSTITUTION_BILLING_NOT_FOUND("0045", "Cannot find billing data for institution having externalId %s and product %s"),
    ONBOARDING_INVALID_UPDATES("0046", "Cannot perform data overrides on institution having external id %s"),
    GEO_TAXONOMY_CODE_NOT_FOUND("0049", "Error on retrieve geographic taxonomy code %s: %s"),
    PRODUCTS_NOT_FOUND_ERROR("0030", "Products not found for institution having internalId %s"),
    ONBOARDING_INFO_INSTITUTION_NOT_FOUND("0050", "No onboarding information found for %s"),
    ONBOARDING_INFO_ERROR("0051", "Error getting onboarding info"),
    TOKEN_ALREADY_CONSUMED("0040", "Token %s has already consumed"),
    TOKEN_NOT_FOUND("0014","Token %s not found"),
    CONTRACT_NOT_FOUND("0015","Token for institution %s and product %s not found"),
    GET_INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution having externalId %s and productId %s"),
    RELATIONSHIP_NOT_ACTIVABLE("0007", "Relationship %s is in status %s and cannot be activated"),
    RELATIONSHIP_NOT_SUSPENDABLE("0010", "Relationship %s is in status %s and cannot be suspended"),
    RELATIONSHIP_NOT_FOUND("0008", "Relationship not found for Institution %s User %s Role %s");

    private final String code;
    private final String detail;

    CustomErrorEnum(String code, String detail) {
        this.code = code;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }
    public String getMessage() {
        return detail;
    }

}
