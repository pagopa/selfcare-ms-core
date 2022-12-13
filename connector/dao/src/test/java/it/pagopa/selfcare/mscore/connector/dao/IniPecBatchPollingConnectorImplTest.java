package it.pagopa.selfcare.mscore.connector.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchPollingEntity;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class IniPecBatchPollingConnectorImplTest {
    @InjectMocks
    private IniPecBatchPollingConnectorImpl iniPecBatchPollingConnectorImpl;
    @Mock
    private IniPecBatchPollingRepository iniPecBatchPollingRepository;


    @Test
    void testSave() {
        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setBatchId("batchId");
        iniPecBatchPolling.setPollingId("pollingId");
        iniPecBatchPolling.setStatus("status");
        iniPecBatchPolling.setId("6390cd95e28c6f70101f2e85");
        iniPecBatchPolling.setTimeStamp(LocalDateTime.now());

        IniPecBatchPollingEntity iniPecBatchPollingEntity = new IniPecBatchPollingEntity();
        iniPecBatchPollingEntity.setBatchId("batchId");
        iniPecBatchPollingEntity.setPollingId("pollingId");
        iniPecBatchPollingEntity.setStatus("status");
        iniPecBatchPollingEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        iniPecBatchPollingEntity.setTimeStamp(LocalDateTime.now());

        when(iniPecBatchPollingRepository.save(any())).thenReturn(iniPecBatchPollingEntity);

        assertNotNull(iniPecBatchPollingConnectorImpl.save(iniPecBatchPolling));
    }

    @Test
    void testFindByStatus(){
        IniPecBatchPollingEntity iniPecBatchPollingEntity = new IniPecBatchPollingEntity();
        iniPecBatchPollingEntity.setBatchId("batchId");
        iniPecBatchPollingEntity.setPollingId("pollingId");
        iniPecBatchPollingEntity.setStatus("status");
        iniPecBatchPollingEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        iniPecBatchPollingEntity.setTimeStamp(LocalDateTime.now());
        List<IniPecBatchPollingEntity> iniPecBatchPollingList = new ArrayList<>();
        iniPecBatchPollingList.add(iniPecBatchPollingEntity);
        Page<IniPecBatchPollingEntity> page = new PageImpl<>(iniPecBatchPollingList);

        when(iniPecBatchPollingRepository.find(any(),any(),eq(IniPecBatchPollingEntity.class))).thenReturn(page);

        assertNotNull(iniPecBatchPollingConnectorImpl.findByStatus("status"));
    }

    @Test
    void testConvertToIniPecBatchPollingEntity(){
        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setBatchId("batchId");
        iniPecBatchPolling.setPollingId("pollingId");
        iniPecBatchPolling.setStatus("status");
        iniPecBatchPolling.setId("6390cd95e28c6f70101f2e85");
        iniPecBatchPolling.setTimeStamp(LocalDateTime.now());

        assertNotNull(iniPecBatchPollingConnectorImpl.convertToIniPecBatchPollingEntity(iniPecBatchPolling));
    }
}

