package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstitutionResponse {

    private String id;
    private String externalId;
    private String ipaCode;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private List<GeoTaxonomies> geographicTaxonomies;
    private List<AttributesResponse> attributes;
    private PaymentServiceProviderResponse paymentServiceProviderResponse;
    private DataProtectionOfficerResponse dataProtectionOfficer;
    //private BillingResponse billing;
    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportEmail;
    private String supportPhone;
    private boolean imported;
}
