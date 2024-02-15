package it.pagopa.selfcare.mscore.web.model.institution;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class InstitutionFromIpaPost {

    @NotNull
    private String taxCode;
    private String subunitCode;
    private InstitutionPaSubunitType subunitType;
    private List<GeoTaxonomies> geographicTaxonomies;
    private InstitutionType institutionType;
}
