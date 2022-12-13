package it.pagopa.selfcare.mscore.connector.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.rest.client.IniPecRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.IniPecPecResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.IniPecPollingResponse;
import it.pagopa.selfcare.mscore.model.inipec.IniPecCfRequest;
import it.pagopa.selfcare.mscore.model.inipec.IniPecPec;

import java.util.ArrayList;
import java.util.List;

import it.pagopa.selfcare.mscore.model.inipec.Pec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecClientImplTest {
    @InjectMocks
    private IniPecClientImpl iniPecClientImpl;

    @Mock
    private IniPecRestClient iniPecRestClient;

    @Test
    void testCallEServiceRequestId() {
        IniPecPollingResponse iniPecPollingResponse = new IniPecPollingResponse();
        iniPecPollingResponse.setDataOraRichiesta("Data Ora Richiesta");
        iniPecPollingResponse.setIdentificativoRichiesta("Identificativo Richiesta");
        when(iniPecRestClient.callEServiceRequestId(any())).thenReturn(iniPecPollingResponse);

        IniPecCfRequest iniPecCfRequest = new IniPecCfRequest();
        iniPecCfRequest.setDataOraRichiesta("Data Ora Richiesta");
        iniPecCfRequest.setElencoCf(new ArrayList<>());
        assertEquals("Data Ora Richiesta",iniPecCfRequest.getDataOraRichiesta());
        assertEquals(0,iniPecCfRequest.getElencoCf().size());
        assertNotNull(iniPecClientImpl.callEServiceRequestId(iniPecCfRequest));
    }

    @Test
    void testCallEServiceRequestPec() {
        IniPecPecResponse iniPecPecResponse = new IniPecPecResponse();
        Pec pec = new Pec();
        pec.setPecImpresa("pecImpresa");
        pec.setCf("cf");
        pec.setPecProfessionistas(new ArrayList<>());
        List<Pec> pecs = new ArrayList<>();
        pecs.add(pec);
        iniPecPecResponse.setElencoPec(pecs);
        iniPecPecResponse.setIdentificativoRichiesta("correlationId");
        iniPecPecResponse.setDataOraDownload("Data Ora Download");

        when(iniPecRestClient.callEServiceRequestPec(any())).thenReturn(iniPecPecResponse);

        IniPecPec iniPecPec = iniPecClientImpl.callEServiceRequestPec("correlationId");
        assertEquals(iniPecPec.getElencoPec().get(0).getPecImpresa(),"pecImpresa");
        assertEquals(iniPecPec.getElencoPec().get(0).getCf(),"cf");
        assertEquals(iniPecPec.getElencoPec().get(0).getPecProfessionistas().size(),0);

        assertEquals("correlationId",iniPecPec.getIdentificativoRichiesta());
        assertEquals(1,iniPecPec.getElencoPec().size());
        assertEquals(iniPecPec.getDataOraDownload(),"Data Ora Download");
    }
}

