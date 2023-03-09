package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InstitutionUpdateRequest {

    @NotNull(message = "institutionType is required")
    private InstitutionType institutionType;

    private String description;
    private String digitalAddress;
    private String address;

    @NotEmpty(message = "taxCode is required")
    private String taxCode;

    private String zipCode;
    private PaymentServiceProviderRequest paymentServiceProvider;
    private DataProtectionOfficerRequest dataProtectionOfficer;
    private List<String> geographicTaxonomyCodes;

    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;
    private boolean imported;
}
