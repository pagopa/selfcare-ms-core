package it.pagopa.selfcare.mscore.web.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonResponse {
    private String id;
}
