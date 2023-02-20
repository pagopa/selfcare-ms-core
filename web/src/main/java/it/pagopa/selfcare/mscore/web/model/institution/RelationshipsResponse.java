package it.pagopa.selfcare.mscore.web.model.institution;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelationshipsResponse {

   List<RelationshipResult> relationshipInfoList;

}
