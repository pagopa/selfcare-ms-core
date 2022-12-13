package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecServiceImplTest {
    @Mock
    private IniPecBatchRequestConnector iniPecBatchRequestConnector;

    @InjectMocks
    private IniPecServiceImpl iniPecServiceImpl;

    @Test
    void testCreateBatchRequestByCfAndCorrelationId() {
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("id");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("cf");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());

        when(iniPecBatchRequestConnector.save(any())).thenReturn(iniPecBatchRequest);

        assertNotNull(iniPecServiceImpl.createBatchRequestByCf("cf"));
    }
}

