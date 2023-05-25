package it.pagopa.selfcare.mscore.connector.rest.model.registryproxy;

import it.pagopa.selfcare.mscore.constant.Origin;
import lombok.Data;

@Data
public class UoResponse {

    private String id;
    private String codiceIpa;
    private String denominazioneEnte;
    private String codiceFiscaleEnte;
    private String codiceUniUo;
    private String codiceUniUoPadre;
    private String codiceUniAoo;
    private String descrizioneUo;
    private String mail1;
    private Origin origin;
}
