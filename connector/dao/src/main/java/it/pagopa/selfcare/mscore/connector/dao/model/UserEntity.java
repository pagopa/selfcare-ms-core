package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document("User")
@FieldNameConstants(asEnum = true)
public class UserEntity {

    @Id
    private String id;
    private List<UserBindingEntity> bindings;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
