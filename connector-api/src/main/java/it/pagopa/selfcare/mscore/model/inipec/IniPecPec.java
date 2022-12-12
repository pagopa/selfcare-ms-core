package it.pagopa.selfcare.mscore.model.inipec;

import lombok.Data;

import java.util.List;

@Data
public class IniPecPec {
    private String dataOraDownload;
    private String identificativoRichiesta;
    private List<Pec> elencoPec;
}
