package it.pagopa.selfcare.mscore.connector.rest.model.registryproxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.mscore.constant.Origin;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AooResponse {

    private String id;
    private String codiceIpa;
    private String denominazioneEnte;
    private String codiceFiscaleEnte;
    private String codiceUniAoo;
    private String denominazioneAoo;
    private String mail1;
    private String codAoo;
    private Origin origin;

    private String dataIstituzione;
    private String nomeResponsabile;
    private String cognomeResponsabile;
    private String mailResponsabile;
    private String telefonoResponsabile;
    private String codiceComuneISTAT;
    private String codiceCatastaleComune;
    private String CAP;
    private String indirizzo;
    private String telefono;
    private String fax;
    private String tipoMail1;
    private String protocolloInformatico;
    private String URIProtocolloInformatico;
    private String dataAggiornamento;
}
