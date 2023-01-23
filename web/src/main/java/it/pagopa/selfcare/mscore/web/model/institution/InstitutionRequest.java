package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InstitutionRequest {

    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;

    @NotNull
    private String taxCode;

    private List<GeoTaxonomies> geographicTaxonomies;
    private List<AttributesRequest> attributes;
    private PaymentServiceProviderRequest paymentServiceProvider;
    private DataProtectionOfficerRequest dataProtectionOfficer;

}
