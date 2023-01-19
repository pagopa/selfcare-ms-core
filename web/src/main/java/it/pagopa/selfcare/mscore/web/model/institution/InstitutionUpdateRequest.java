package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionUpdateRequest {

    private InstitutionType institutionType;
    private String description;
    private String digitalAddress;
    private String address;
    private String taxCode;
    private String zipCode;
    private PaymentServiceProviderResponse paymentServiceProviderResponse;
    private DataProtectionOfficerResponse dataProtectionOfficer;
    private List<String> geographicTaxonomyCodes;

}
