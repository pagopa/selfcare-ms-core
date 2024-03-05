package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProductResponse;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstitutionResponse {

    @NotBlank
    private String id;
    private String externalId;
    @NotBlank
    private String origin;
    @NotBlank
    private String originId;
    private String description;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    @NotBlank
    private String taxCode;
    private String city;
    private String county;
    private String country;
    private List<GeoTaxonomies> geographicTaxonomies;
    private List<AttributesResponse> attributes;
    private List<OnboardedProductResponse> onboarding;
    private PaymentServiceProviderResponse paymentServiceProvider;
    private DataProtectionOfficerResponse dataProtectionOfficer;
    private RootParentResponse rootParent;
    private String rea;
    private String shareCapital;
    private String businessRegisterPlace;
    private String supportPhone;
    private boolean imported;
    private String subunitCode;
    private String subunitType;
    private String aooParentCode;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean delegation;

}
