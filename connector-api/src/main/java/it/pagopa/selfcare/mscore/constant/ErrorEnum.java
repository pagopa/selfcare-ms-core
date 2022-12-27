package it.pagopa.selfcare.mscore.constant;

public enum ErrorEnum {

    ERROR("0000","Generic Error"),
    GET_INSTITUTION_MANAGER_ERROR("0042", "Error while retrieving institution having externalId %s"),
    GET_INSTITUTION_MANAGER_NOT_FOUND("0043", "Cannot find active manager for institution having externalId %s and product %s"),
    CLAIM_NOT_FOUND("0001", "Claim %s not found"),
    CONTRACT_NOT_FOUND("0003", "Contract not found for institution having externalId %s"),
    INSTITUTION_NOT_ONBOARDED("0004", "Institution having externalId %s is not onboarded for product %s"),
    INVALID_SIGNATURE("0005", "Signature not valid ${signatureValidationErrors.mkString()}"),
    RELATIONSHIP_DOCUMENT_NOT_FOUND("0006", "Relationship document not found for relationship %s"),
    RELATIONSHIP_NOT_ACTIVABLE("0007", "Relationship %s is in status %s and cannot be activated"),
    RELATIONSHIP_NOT_FOUND("0008", "Relationship not found for Institution %s User %s Role %s"),
    RELATIONSHIP_NOT_FOUND_IN_INSTITUTION("0009", "Relationship %s not found for Institution %s"),
    RELATIONSHIP_NOT_SUSPENDABLE("0010", "Relationship $relationshipId is in status %s and cannot be suspended"),
    UID_VALIDATION_ERROR("0013", "Error while uid validation: %s"),
    MULTIPLE_PRODUCTS_REQUEST_ERROR("0014", "Multi products request is forbidden: Products in request: ${products.mkString()} "),
    ONBOARDING_VERIFICATION_ERROR("0015", "Error while verifying onboarding"),
    GETTING_ONBOARDING_INFO_ERROR("0016", "Error while getting onboarding info"),
    ONBOARDING_OPERATION_ERROR("0017", "Error while performing onboarding operation"),
    ONBOARDING_LEGALS_ERROR("0018", "Error while onboarding legals"),
    ONBOARDING_SUBDELEGATES_ERROR("0019", "Error while onboarding subdelegates"),
    ONBOARDING_OPERATORS_ERROR("0020", "Error while onboarding operators"),
    CONFIRM_ONBOARDING_ERROR("0021", "Error while confirming onboarding"),
    INVALIDATE_ONBOARDING_ERROR("0022", "Error while invalidating onboarding"),
    RETRIEVING_USER_RELATIONSHIPS_ERROR("0023", "Error while retrieving user relationships"),
    ACTIVATE_RELATIONSHIP_ERROR("0024", "Error while activating relationship"),
    SUSPENDING_RELATIONSHIP_ERROR("0025", "Error while suspending relationship"),
    BAD_REQUEST_ERROR("0026", "Bad request error"),
    ONBOARDING_DOCUMENT_ERROR("0027", "Error retrieving document for relationship %s"),
    GET_RELATIONSHIP_ERROR("0028", "Error while getting relationship"),
    DELETE_RELATIONSHIP_ERROR("0029", "Error while deleting relationship"),
    PRODUCTS_NOT_FOUND_ERROR("0030", "Products not found for institution having externalId %s"),
    GET_PRODUCTS_ERROR("0031", "Error while getting products"),
    MANAGER_FOUND_ERROR("0032", "Onboarded managers found for this institution"),
    MANAGER_NOT_FOUND_ERROR("0033", "No onboarded managers for at least one of the institutions %s and product %s"),
    INVALID_CATEGORY_ERROR("0035", "Invalid category %s"),
    INSTITUTION_NOT_FOUND("0036", "Cannot find Institution using institutionId %s and externalInstitutionId %s"),
    CREATE_INSTITUTION_ERROR("0037", "Error while creating requested institution"),
    CREATE_INSTITUTION_CONFLICT("0038", "Institution already exists"),
    CREATE_INSTITUTION_NOT_FOUND("0039", "Institution doesn't exist in party-registry"),
    GET_INSTITUTION_ERROR("0040", "Error while retrieving institution having id %s"),
    GET_INSTITUTION_BY_EXTERNAL_ID_ERROR("0041", "Error while retrieving institution having externalId %s"),
    GET_INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution having externalId %s"),
    GET_INSTITUTION_BILLING_NOT_FOUND("0045", "Cannot find billing data for institution having externalId %s and product %s"),
    ONBOARDING_INVALID_UPDATES("0046", "Cannot perform data overrides on institution having external id %s"),
    TOKEN_VERIFICATION_FATAL_ERROR("0048", "Error on retrieve %s: %s"),
    GEO_TAXONOMY_CODE_NOT_FOUND("0049", "Error on retrieve geographic taxonomy code %s: %s"),
    GEO_TAXONOMY_CODE_ERROR("0050", "Error while retrieving institution geographic taxonomy"),
    PUT_INSTITUTION_ERROR("0051", "Error while updating institution %s with %s");

    private final String code;
    private final String detail;

    ErrorEnum(String code, String detail) {
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
