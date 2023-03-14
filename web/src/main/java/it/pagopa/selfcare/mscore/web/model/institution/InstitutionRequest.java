package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InstitutionRequest {

    @NotEmpty(message = "Description is required")
    private String description;

    private InstitutionType institutionType;

    @NotEmpty(message = "DigitalAddress is required")
    private String digitalAddress;

    @NotEmpty(message = "Address is required")
    private String address;

    @NotEmpty(message = "ZipCode is required")
    private String zipCode;

    @NotNull(message = "TaxCode is required")
    private String taxCode;

    private List<GeoTaxonomies> geographicTaxonomies;
    private List<AttributesRequest> attributes;
    private PaymentServiceProviderRequest paymentServiceProvider;
    private DataProtectionOfficerRequest dataProtectionOfficer;

    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;

}
