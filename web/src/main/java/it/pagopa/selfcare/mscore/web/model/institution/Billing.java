package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Billing {

    private String vatNumber;
    private String recipientCode;
    private String publicServer;
}
