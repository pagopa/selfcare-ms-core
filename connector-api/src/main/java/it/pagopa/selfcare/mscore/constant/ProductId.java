package it.pagopa.selfcare.mscore.constant;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductId {

    PROD_INTEROP("prod-interop");

    private final String value;

    ProductId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return value;
    }
}
