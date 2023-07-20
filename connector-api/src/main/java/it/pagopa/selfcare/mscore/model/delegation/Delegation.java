package it.pagopa.selfcare.mscore.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Delegation {

    private String id;
    private String from;
    private String institutionFromName;
    private String institutionFromRootName;
    private DelegationType type;
    private String to;
    private String productId;

}
