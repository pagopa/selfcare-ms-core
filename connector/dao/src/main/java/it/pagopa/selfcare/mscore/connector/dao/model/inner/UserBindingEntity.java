package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@FieldNameConstants(asEnum = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserBindingEntity {

    @Indexed(unique = true)
    private String institutionId;
    private List<OnboardedProductEntity> products;
}
