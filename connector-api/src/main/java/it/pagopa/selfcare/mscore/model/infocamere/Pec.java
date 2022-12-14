package it.pagopa.selfcare.mscore.model.infocamere;

import lombok.Data;

import java.util.List;

@Data
public class Pec {
    private String cf;
    private String pecImpresa;
    private List<String> pecProfessionistas;
}
