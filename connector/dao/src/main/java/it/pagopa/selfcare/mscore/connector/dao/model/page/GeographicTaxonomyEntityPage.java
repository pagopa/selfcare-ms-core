package it.pagopa.selfcare.mscore.connector.dao.model.page;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import lombok.Data;

import java.util.List;

@Data
public class GeographicTaxonomyEntityPage {
    private Integer total;
    private List<GeoTaxonomyEntity> data;
}
