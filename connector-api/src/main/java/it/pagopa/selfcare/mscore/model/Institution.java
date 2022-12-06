package it.pagopa.selfcare.mscore.model;

import lombok.Data;
import java.util.List;

@Data
public class Institution {
    private String id;
    private String externalId;
    private String description;
    private String ipaCode;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private Billing billing;
    private List<Onboarding> onboarding;
}
