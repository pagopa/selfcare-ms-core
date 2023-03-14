package it.pagopa.selfcare.mscore.connector.dao.model.page;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import lombok.Data;

@Data
public class RelationshipEntityPageElement {
    private String id;
    private OnboardedProductEntity product;
}
