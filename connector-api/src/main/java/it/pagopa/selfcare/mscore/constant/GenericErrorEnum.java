package it.pagopa.selfcare.mscore.constant;

public enum GenericErrorEnum {
    GET_INSTITUTION_BY_EXTERNAL_ID_ERROR("0041", "Error while retrieving institution with externalId"),
    INSTITUTION_MANAGER_ERROR("0042", "Error while retrieving institution with externalId"),
    INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution billing data"),
    CREATE_INSTITUTION_ERROR("0037", "Error while creating requested institution"),
    ONBOARDING_OPERATION_ERROR("0017", "Error while performing onboarding operation"),

    ONBOARDING_VERIFICATION_ERROR("0015", "Error while verifying onboarding"),
    GETTING_ONBOARDING_INFO_ERROR("0016", "Error while getting onboarding info"),
    GET_PRODUCTS_ERROR("0031", "Error while getting products"),

    CONTRACT_PATH_ERROR("0100", "Contract Path is required"),
    RETRIEVING_USER_RELATIONSHIP_ERROR("0023", "Error while retrieving user relationships"),
    RETRIEVE_GEO_TAXONOMIES_ERROR("0050", "Error while retrieving institution geographic taxonomy"),
    GENERIC_ERROR("0000", "Generic Error");
    private final String code;
    private final String detail;


    GenericErrorEnum(String code, String detail) {
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
