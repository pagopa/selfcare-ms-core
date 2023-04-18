package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;

@Data
public class GeographicTaxonomies {
    private String code;
    private String desc;
    private String region;
    private String province;
    private String provinceAbbreviation;
    private String country;
    private String countryAbbreviation;
    private String startDate;
    private String endDate;
    private boolean enable;
}
