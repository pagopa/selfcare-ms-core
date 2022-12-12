package it.pagopa.selfcare.mscore.connector.rest.model;

import it.pagopa.selfcare.mscore.model.inipec.Pec;
import lombok.Data;

import java.util.List;

@Data
public class IniPecPecResponse {
    private String dataOraDownload;
    private String identificativoRichiesta;
    private List<Pec> elencoPec;
}
