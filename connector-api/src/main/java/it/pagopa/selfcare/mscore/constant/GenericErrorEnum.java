package it.pagopa.selfcare.mscore.constant;

public enum GenericErrorEnum {

    GET_INSTITUTION_BY_EXTERNAL_ID_ERROR("0041", "Error while retrieving institution having externalId %s"),
    GET_INSTITUTION_MANAGER_ERROR("0042", "Error while retrieving institution having externalId %s"),
    GET_INSTITUTION_BILLING_ERROR("0044", "Error while retrieving institution having externalId %s"),
    CREATE_INSTITUTION_ERROR("0037", "Error while creating requested institution"),
    ONBOARDING_OPERATION_ERROR("0017", "Error while performing onboarding operation"),

    GENERIC_ERROR("0000","Generic Error");
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
