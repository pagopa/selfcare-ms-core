package it.pagopa.selfcare.mscore.web.model.institution;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Valid
public class BulkPartiesSeed {

    @NotNull
    private List<String> partyIdentifiers;
}
