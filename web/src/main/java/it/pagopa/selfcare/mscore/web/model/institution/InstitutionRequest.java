package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class InstitutionRequest {

    private String id;
    private String externalId;
    private String origin;
    private String originId;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String city;
    private String county;
    private String country;
    private String taxCode;
    private BillingRequest billing;
    private List<OnboardingRequest> onboarding;
    private List<GeoTaxonomies> geographicTaxonomies;
    private List<AttributesRequest> attributes;
    private PaymentServiceProviderRequest paymentServiceProvider;
    private DataProtectionOfficerRequest dataProtectionOfficer;
    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;
    private boolean imported;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean delegation;

}
