package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

@Data
public class GeoTaxonomies {
    private String code; //REQUIRED
    private String desc; //REQUIRED
    private boolean enable; //REQUIRED
}
