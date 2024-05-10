package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

@Data
public class BrokerResponse {

    private String id;
    private String taxCode;
    private String description;
    private int numberOfDelegations;

}
