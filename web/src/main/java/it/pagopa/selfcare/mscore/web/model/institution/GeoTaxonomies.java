package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

@Data
public class GeoTaxonomies {
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
