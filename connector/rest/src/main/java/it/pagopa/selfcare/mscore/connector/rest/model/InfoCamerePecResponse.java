package it.pagopa.selfcare.mscore.connector.rest.model;

import it.pagopa.selfcare.mscore.model.infocamere.Pec;
import lombok.Data;

import java.util.List;

@Data
public class InfoCamerePecResponse {
    private String dataOraDownload;
    private String identificativoRichiesta;
    private List<Pec> elencoPec;
}
