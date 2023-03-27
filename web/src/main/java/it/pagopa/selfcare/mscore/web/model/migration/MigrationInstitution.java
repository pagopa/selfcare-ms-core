package it.pagopa.selfcare.mscore.web.model.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MigrationInstitution {

    private String id;
    private String externalId;

    @NotNull(message = "Valid value for Origin are: IPA, INFOCAMERE, SELC or STATIC")
    private Origin origin;

    private String originId;
    private String description;

    @NotNull(message = "Valid value for institutionType are: PA,PG,GSP,PT,SCP or PSP")
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
