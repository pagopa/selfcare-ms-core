package it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy;

import lombok.Data;

@Data
public class GeographicTaxonomiesResponse {
    private String code; //REQUIRED
    private String desc; //REQUIRED
    private String region;
    private String province;
    private String provinceAbbreviation;
    private String country;
    private String countryAbbreviation;
    private String startDate;
    private String endDate;
    private boolean enable; //REQUIRED
}
