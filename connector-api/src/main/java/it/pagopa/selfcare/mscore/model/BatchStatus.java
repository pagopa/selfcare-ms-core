package it.pagopa.selfcare.mscore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BatchStatus {

    NO_BATCH_ID("NO_BATCH_ID"),
    NOT_WORKED("NOT_WORKED"),
    WORKING("WORKING"),
    WORKED("WORKED"),
    ERROR("ERROR");

    private String value;

    BatchStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static BatchStatus fromValue(String value) {
        for (BatchStatus b : BatchStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
