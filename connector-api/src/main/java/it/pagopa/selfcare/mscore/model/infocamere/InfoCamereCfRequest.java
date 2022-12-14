package it.pagopa.selfcare.mscore.model.infocamere;

import lombok.Data;

import java.util.List;

@Data
public class InfoCamereCfRequest {
    private String dataOraRichiesta;
    private List<String> elencoCf;
}
