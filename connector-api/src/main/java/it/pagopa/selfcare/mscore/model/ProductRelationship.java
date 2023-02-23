package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductRelationship {
    private String tokenId;
    private Institution institution;
    private Map<String, List<OnboardedProduct>> userProducts;
}
