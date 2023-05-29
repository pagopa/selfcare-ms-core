package it.pagopa.selfcare.mscore.connector.dao.model.aggregation;


import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Sharded;

import java.util.List;

@Data
@NoArgsConstructor
@Document("User")
@Sharded(shardKey = {"id"})
@FieldNameConstants(asEnum = true)
public class UserInstitutionAggregationEntity {

    @Id
    private String id;
    private UserInstitutionBindingEntity bindings;
    private List<InstitutionEntity> institutions;
}
