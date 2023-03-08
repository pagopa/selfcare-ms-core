package it.pagopa.selfcare.mscore.constant;

public enum GenericError {

    GET_USER_INSTITUTION_RELATIONSHIP_ERROR("0023", "Error while retrieving user relationships"),
    GET_INSTITUTION_BY_ID_ERROR("0040", "Error while retrieving institution having id %s"),
    GET_INSTITUTION_BY_EXTERNAL_ID_ERROR("0041", "Error while retrieving institution having externalId %s"),
    INSTITUTION_MANAGER_ERROR("0042", "Error while retrieving institution having externalId %s"),
    INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution having externalId %s"),
    CREATE_INSTITUTION_ERROR("0037", "Error while creating requested institution"),
    ONBOARDING_OPERATION_ERROR("0017", "Error while performing onboarding operation"),
    ONBOARDING_VERIFICATION_ERROR("0015", "Error while verifying onboarding"),
    GETTING_ONBOARDING_INFO_ERROR("0016", "Error while getting onboarding info"),
    GET_PRODUCTS_ERROR("0031", "Error while getting products"),
    CONTRACT_PATH_ERROR("0100", "Contract Path is required"),
    MANAGER_EMAIL_NOT_FOUND("0101", "Manager email not found"),
    VERIFY_TOKEN_FAILED("0041", "Something went wrong trying to verify token"),

    SIGNATURE_NOT_FOUND("1007", "No signature found"),
    SIGNATURE_VALIDATION_ERROR("1004", "The tax code related to signature does not match anyone contained in the relationships"),
    ORIGINAL_DOCUMENT_NOT_FOUND("1008", "Original document information not found"),
    DOCUMENT_VALIDATION_FAIL("1000", "Error trying to validate document, due: %s"),
    INVALID_SIGNATURE_FORMS("1003", "Only CAdES signature form is admitted. Invalid signatures forms detected: %s"),
    INVALIDATE_ONBOARDING_ERROR("0022", "Error while invalidating onboarding"),
    CONFIRM_ONBOARDING_ERROR("0021", "Error while confirming onboarding"),
    INVALID_DOCUMENT_SIGNATURE("1002", "Document signature is invalid"),
    INVALID_CONTRACT_DIGEST("1001", "Invalid file digest"),

    INVALIDE_SIGNATURE_TAX_CODE_FORMAT("1005", "Invalid tax code format found in digital signature"),
    TAX_CODE_NOT_FOUND_IN_SIGNATURE("1006", "No tax code has been found in digital signature"),
    INVALID_SIGNATURE_TAX_CODE("1004", "The tax code related to signature does not match anyone contained in the relationships"),
    UNABLE_TO_DOWNLOAD_FILE("1102", "Unable to download template %s"),
    INVALID_SIGNATURE("0005", "Signature not valid: %s"),
    ERROR_DURING_SEND_MAIL("0000", "Error during send mail to %s"),
    ERROR_DURING_UPLOAD_FILE("0000", "Error during upload file %s"),
    ERROR_DURING_DOWNLOAD_FILE("0000", "Error during download file %s"),
    RETRIEVING_USER_RELATIONSHIP_ERROR("0023", "Error while retrieving user relationships"),
    ACTIVATE_RELATIONSHIP_ERROR("0024", "Error while activating relationship"),
    SUSPEND_RELATIONSHIP_ERROR("0025", "Error while suspending relationship"),
    PUT_INSTITUTION_ERROR("0051", "Error while updating institution"),
    ONBOARDING_SUBDELEGATES_ERROR("0019", "Error while onboarding subdelegates"),
    ONBOARDING_OPERATORS_ERROR("0020", "Error while onboarding operators"),
    ONBOARDING_LEGALS_ERROR("0018", "Error while onboarding legals"),
    RETRIEVE_GEO_TAXONOMIES_ERROR("0050", "Error while retrieving institution geographic taxonomy"),
    GET_RELATIONSHIP_ERROR("0028", "Error while getting relationship"),
    CREATE_PERSON_ERROR("0009", "Error while creating person"),
    GENERIC_ERROR("0000", "Generic Error");
    private final String code;
    private final String detail;


    GenericError(String code, String detail) {
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
