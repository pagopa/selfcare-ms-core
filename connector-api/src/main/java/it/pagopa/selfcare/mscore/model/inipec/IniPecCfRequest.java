package it.pagopa.selfcare.mscore.model.inipec;

import lombok.Data;

import java.util.List;

@Data
public class IniPecCfRequest {
    private String dataOraRichiesta;
    private List<String> elencoCf;
}
