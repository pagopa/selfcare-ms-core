package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class InstitutionPut {

    List<String> geographicTaxonomyCodes;
}
