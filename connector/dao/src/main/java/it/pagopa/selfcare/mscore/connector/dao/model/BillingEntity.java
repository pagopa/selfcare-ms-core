package it.pagopa.selfcare.mscore.connector.dao.model;

import lombok.Data;

@Data
public class BillingEntity {
    private String vatNumber;
    private String recipientCode;
    private boolean publicServices;
}
