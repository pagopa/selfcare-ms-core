package it.pagopa.selfcare.mscore.model.institution;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Institution {

    private String id;
    private String externalId;
    private Origin origin;
    private String originId;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private Billing billing;
    private List<Onboarding> onboarding;
    private List<InstitutionGeographicTaxonomies> geographicTaxonomies;
    private List<Attributes> attributes;
    private PaymentServiceProvider paymentServiceProvider;
    private DataProtectionOfficer dataProtectionOfficer;
    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;
    private boolean imported;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
