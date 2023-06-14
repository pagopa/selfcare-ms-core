package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BulkInstitution {

    @NotNull
    private String id;

    @NotNull
    private String externalId;

    @NotNull
    private String origin;

    @NotNull
    private String originId;

    @NotNull
    private String description;

    private InstitutionType institutionType;

    @NotNull
    private String digitalAddress;

    @NotNull
    private String address;

    @NotNull
    private String zipCode;

    @NotNull
    private String taxCode;

    private List<AttributesResponse> attributes;

    @NotNull
    private Map<String, BulkProduct> products;
}
