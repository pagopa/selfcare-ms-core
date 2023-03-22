package it.pagopa.selfcare.mscore.constant;

public enum CustomError {

    RELATIONSHIP_ID_NOT_FOUND("0008", "Relationship %s not found"),
    GET_INSTITUTION_MANAGER_NOT_FOUND("0043", "Cannot find active manager for institution having externalId %s and product %s"),
    USER_NOT_FOUND_ERROR("0031", "User having userId %s not found"),
    USERS_NOT_FOUND_ERROR("0031", "Users having userIds %s not found"),
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
    ONBOARDING_INVALID_UPDATES("0046", "Cannot perform data overrides on institution having external id %s"),
    GEO_TAXONOMY_CODE_NOT_FOUND("0049", "Error on retrieve geographic taxonomy code %s: %s"),
    PRODUCTS_NOT_FOUND_ERROR("0030", "Products not found for institution having internalId %s"),
    ONBOARDING_INFO_INSTITUTION_NOT_FOUND("0050", "No onboarding information found for %s"),
    ONBOARDING_INFO_ERROR("0051", "Error getting onboarding info"),
    ONBOARDING_BILLING_ERROR("0000", "Billing vatNumber and recipientCode are required"),
    ONBOARDING_PENDING("0000", "There is already an onboarding request for product %s pending"),
    TOKEN_ALREADY_CONSUMED("0040", "Token %s has already consumed"),
    TOKEN_EXPIRED("0040", "Token %s is expired in %s and is status is DELETED"),
    TOKEN_DELETED("0040", "Token %s was DELETED"),
    TOKEN_NOT_FOUND("0014", "Token %s not found"),
    CONTRACT_NOT_FOUND("0015", "Token for institution %s and product %s not found or is in invalid status"),
    DOCUMENT_NOT_FOUND("0016", "Document for relationship %s not found"),
    GET_INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution having externalId %s and productId %s"),
    RELATIONSHIP_NOT_ACTIVABLE("0007", "Relationship %s cannot be activated"),
    INVALID_STATUS_CHANGE("0000", "Cannot update state from value %s to value %s"),
    RELATIONSHIP_NOT_SUSPENDABLE("0010", "Relationship %s cannot be suspended"),
    MISSING_QUERY_PARAMETER("0045", "At least one query parameter between [userId, institutionId] must be passed"),
    RELATIONSHIP_NOT_FOUND("0008", "Relationship not found for Institution %s, User %s and Role %s");

    private final String code;
    private final String detail;

    CustomError(String code, String detail) {
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
