package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@FieldNameConstants(asEnum = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserBindingEntity {
    private String institutionId;
    private List<OnboardedProductEntity> products;
    private OffsetDateTime createdAt;
}
