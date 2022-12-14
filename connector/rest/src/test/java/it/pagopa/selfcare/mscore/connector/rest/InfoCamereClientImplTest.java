package it.pagopa.selfcare.mscore.connector.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.rest.client.InfoCamereRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePecResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.InfoCamerePollingResponse;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereCfRequest;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamerePec;

import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.infocamere.Pec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class InfoCamereClientImplTest {
    @InjectMocks
    private InfoCamereClientImpl infoCamereClientImpl;

    @Mock
    private InfoCamereRestClient infoCamereRestClient;

    @Test
    void testCallEServiceRequestId() {
        InfoCamerePollingResponse infoCamerePollingResponse = new InfoCamerePollingResponse();
        infoCamerePollingResponse.setDataOraRichiesta("Data Ora Richiesta");
        infoCamerePollingResponse.setIdentificativoRichiesta("Identificativo Richiesta");
        when(infoCamereRestClient.callEServiceRequestId(any())).thenReturn(infoCamerePollingResponse);

        InfoCamereCfRequest infoCamereCfRequest = new InfoCamereCfRequest();
        infoCamereCfRequest.setDataOraRichiesta("Data Ora Richiesta");
        infoCamereCfRequest.setElencoCf(new ArrayList<>());
        assertEquals("Data Ora Richiesta", infoCamereCfRequest.getDataOraRichiesta());
        assertEquals(0, infoCamereCfRequest.getElencoCf().size());
        assertNotNull(infoCamereClientImpl.callEServiceRequestId(infoCamereCfRequest));
    }

    @Test
    void testCallEServiceRequestPec() {
        InfoCamerePecResponse infoCamerePecResponse = new InfoCamerePecResponse();
        Pec pec = new Pec();
        pec.setPecImpresa("pecImpresa");
        pec.setCf("cf");
        pec.setPecProfessionistas(new ArrayList<>());
        List<Pec> pecs = new ArrayList<>();
        pecs.add(pec);
        infoCamerePecResponse.setElencoPec(pecs);
        infoCamerePecResponse.setIdentificativoRichiesta("correlationId");
        infoCamerePecResponse.setDataOraDownload("Data Ora Download");

        when(infoCamereRestClient.callEServiceRequestPec(any())).thenReturn(infoCamerePecResponse);

        InfoCamerePec infoCamerePec = infoCamereClientImpl.callEServiceRequestPec("correlationId");
        assertEquals(infoCamerePec.getElencoPec().get(0).getPecImpresa(),"pecImpresa");
        assertEquals(infoCamerePec.getElencoPec().get(0).getCf(),"cf");
        assertEquals(infoCamerePec.getElencoPec().get(0).getPecProfessionistas().size(),0);

        assertEquals("correlationId", infoCamerePec.getIdentificativoRichiesta());
        assertEquals(1, infoCamerePec.getElencoPec().size());
        assertEquals(infoCamerePec.getDataOraDownload(),"Data Ora Download");
    }
}

