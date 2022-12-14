package it.pagopa.selfcare.mscore.connector.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchPollingEntity;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;

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
class InfoCamereBatchPollingConnectorImplTest {
    @InjectMocks
    private InfoCamereBatchPollingConnectorImpl infoCamereBatchPollingConnectorImpl;
    @Mock
    private InfoCamereBatchPollingRepository infoCamereBatchPollingRepository;


    @Test
    void testSave() {
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setBatchId("batchId");
        infoCamereBatchPolling.setPollingId("pollingId");
        infoCamereBatchPolling.setStatus("status");
        infoCamereBatchPolling.setId("6390cd95e28c6f70101f2e85");
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());

        InfoCamereBatchPollingEntity infoCamereBatchPollingEntity = new InfoCamereBatchPollingEntity();
        infoCamereBatchPollingEntity.setBatchId("batchId");
        infoCamereBatchPollingEntity.setPollingId("pollingId");
        infoCamereBatchPollingEntity.setStatus("status");
        infoCamereBatchPollingEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        infoCamereBatchPollingEntity.setTimeStamp(LocalDateTime.now());

        when(infoCamereBatchPollingRepository.save(any())).thenReturn(infoCamereBatchPollingEntity);

        assertNotNull(infoCamereBatchPollingConnectorImpl.save(infoCamereBatchPolling));
    }

    @Test
    void testFindByStatus(){
        InfoCamereBatchPollingEntity infoCamereBatchPollingEntity = new InfoCamereBatchPollingEntity();
        infoCamereBatchPollingEntity.setBatchId("batchId");
        infoCamereBatchPollingEntity.setPollingId("pollingId");
        infoCamereBatchPollingEntity.setStatus("status");
        infoCamereBatchPollingEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        infoCamereBatchPollingEntity.setTimeStamp(LocalDateTime.now());
        List<InfoCamereBatchPollingEntity> iniPecBatchPollingList = new ArrayList<>();
        iniPecBatchPollingList.add(infoCamereBatchPollingEntity);
        Page<InfoCamereBatchPollingEntity> page = new PageImpl<>(iniPecBatchPollingList);

        when(infoCamereBatchPollingRepository.find(any(),any(),eq(InfoCamereBatchPollingEntity.class))).thenReturn(page);

        assertNotNull(infoCamereBatchPollingConnectorImpl.findByStatus("status"));
    }

    @Test
    void testConvertToIniPecBatchPollingEntity(){
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setBatchId("batchId");
        infoCamereBatchPolling.setPollingId("pollingId");
        infoCamereBatchPolling.setStatus("status");
        infoCamereBatchPolling.setId("6390cd95e28c6f70101f2e85");
        infoCamereBatchPolling.setTimeStamp(LocalDateTime.now());

        assertNotNull(infoCamereBatchPollingConnectorImpl.convertToIniPecBatchPollingEntity(infoCamereBatchPolling));
    }
}

