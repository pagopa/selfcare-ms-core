package it.pagopa.selfcare.mscore.model.institution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;
    private boolean imported;

    public Institution(Institution institution) {
        id = institution.getId();
        externalId = institution.getExternalId();
        ipaCode = institution.getIpaCode();
        description = institution.getDescription();
        institutionType = institution.getInstitutionType();
        digitalAddress = institution.getDigitalAddress();
        address = institution.getAddress();
        zipCode = institution.getZipCode();
        taxCode = institution.getTaxCode();
        billing = institution.getBilling();
        onboarding = new ArrayList<>();
        if(institution.getOnboarding()!=null) {
            onboarding.addAll(institution.getOnboarding());
        }
        geographicTaxonomies = institution.getGeographicTaxonomies();
        attributes = institution.getAttributes();
        paymentServiceProvider = institution.getPaymentServiceProvider();
        dataProtectionOfficer = institution.getDataProtectionOfficer();
        rea = institution.getRea();
        createdAt = institution.getCreatedAt();
        shareCapital = institution.getShareCapital();
        businessRegisterPlace = institution.getBusinessRegisterPlace();
        supportEmail = institution.getSupportEmail();
        supportPhone = institution.getSupportPhone();
        imported = institution.isImported();
    }
}
