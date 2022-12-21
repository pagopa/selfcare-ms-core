package it.pagopa.selfcare.mscore.api;


import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;

public interface GeoTaxonomiesConnector {
    GeographicTaxonomies getExtByCode(String code);
}
