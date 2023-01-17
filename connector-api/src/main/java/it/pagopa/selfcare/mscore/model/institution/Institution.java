package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;
import java.util.List;

@Data
public class Institution {

    private String id;
    private String externalId;
    private String ipaCode;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private Billing billing;
    private List<Onboarding> onboarding;
    private List<GeographicTaxonomies> geographicTaxonomies;
    private List<Attributes> attributes;
    private PaymentServiceProvider paymentServiceProvider;
    private DataProtectionOfficer dataProtectionOfficer;


}
