package it.pagopa.selfcare.mscore.model.inipec;

import lombok.Data;

import java.util.List;

@Data
public class Pec {
    private String cf;
    private String pecImpresa;
    private List<String> pecProfessionistas;
}
