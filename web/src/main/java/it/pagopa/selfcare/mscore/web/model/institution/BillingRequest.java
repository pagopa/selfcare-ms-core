package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

@Data
public class BillingRequest {

    private String vatNumber;
    private String recipientCode;
    private String publicServer;
}
