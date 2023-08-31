package it.pagopa.selfcare.mscore.model.delegation;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DelegationInstitution {

    private String id;
    private String from;
    private String institutionFromName;
    private String institutionToName;
    private String institutionFromRootName;
    private DelegationType type;
    private String to;
    private String productId;
    private List<Institution> institutions;

}
