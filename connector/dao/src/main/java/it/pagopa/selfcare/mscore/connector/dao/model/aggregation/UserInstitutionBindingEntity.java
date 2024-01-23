package it.pagopa.selfcare.mscore.connector.dao.model.aggregation;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserInstitutionBindingEntity {

    private String institutionId;
    private OnboardedProductEntity products;
}
