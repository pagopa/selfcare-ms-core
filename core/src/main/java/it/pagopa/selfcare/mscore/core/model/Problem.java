package it.pagopa.selfcare.mscore.core.model;

import lombok.Data;

import java.util.List;

@Data
public class Problem {

    private String type;
    private Integer status;
    private String title;
    private String detail;
    private List<ProblemError> errors;
}
