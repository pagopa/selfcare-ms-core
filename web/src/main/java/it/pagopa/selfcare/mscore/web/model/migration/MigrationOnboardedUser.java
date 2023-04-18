package it.pagopa.selfcare.mscore.web.model.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MigrationOnboardedUser {
    private String id;
    private List<UserBinding> bindings;
    private OffsetDateTime createdAt;
}
