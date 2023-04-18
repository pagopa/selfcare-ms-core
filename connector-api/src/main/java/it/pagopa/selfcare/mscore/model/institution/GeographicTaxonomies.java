package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;

@Data
public class GeographicTaxonomies {
    private String geotax_id; //REQUIRED
    private String description; //REQUIRED
    private String istat_code;
    private String province_id;
    private String province_abbreviation;
    private String region_id;
    private String country;
    private String country_abbreviation;
    private boolean enable; //REQUIRED
}
