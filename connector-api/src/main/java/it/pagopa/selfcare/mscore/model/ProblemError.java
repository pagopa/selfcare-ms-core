package it.pagopa.selfcare.mscore.model;

import lombok.Builder;


@Builder
public class ProblemError {
    private String code;
    private String detail;
}
