package it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy;

import lombok.Data;

@Data
public class GeographicTaxonomiesResponse {
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
