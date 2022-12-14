package it.pagopa.selfcare.mscore.connector.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchRequestEntity;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;

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
class InfoCamereBatchRequestConnectorImplTest {
    @InjectMocks
    private InfoCamereBatchRequestConnectorImpl infoCamereBatchRequestConnectorImpl;
    @Mock
    private InfoCamereBatchRequestRepository infoCamereBatchRequestRepository;


    @Test
    void testSave() {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("6390cd95e28c6f70101f2e85");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("taxId");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        InfoCamereBatchRequestEntity infoCamereBatchRequestEntity = new InfoCamereBatchRequestEntity();
        infoCamereBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        infoCamereBatchRequestEntity.setBatchId("batchId");
        infoCamereBatchRequestEntity.setCf("taxId");
        infoCamereBatchRequestEntity.setRetry(0);
        infoCamereBatchRequestEntity.setStatus("status");
        infoCamereBatchRequestEntity.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequestEntity.setTimeStamp(infoCamereBatchRequest.getTimeStamp());
        infoCamereBatchRequestEntity.setTtl(LocalDateTime.now());

        when(infoCamereBatchRequestRepository.save(any())).thenReturn(infoCamereBatchRequestEntity);

        assertNotNull(infoCamereBatchRequestConnectorImpl.save(infoCamereBatchRequest));
    }

    @Test
    void testFindAllByBatchId(){
        InfoCamereBatchRequestEntity infoCamereBatchRequestEntity = new InfoCamereBatchRequestEntity();
        infoCamereBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        infoCamereBatchRequestEntity.setBatchId("batchId");
        infoCamereBatchRequestEntity.setCf("taxId");
        infoCamereBatchRequestEntity.setRetry(0);
        infoCamereBatchRequestEntity.setStatus("status");
        infoCamereBatchRequestEntity.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequestEntity.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequestEntity.setTtl(LocalDateTime.now());
        List<InfoCamereBatchRequestEntity> iniPecBatchRequestEntities = new ArrayList<>();
        iniPecBatchRequestEntities.add(infoCamereBatchRequestEntity);
        Page<InfoCamereBatchRequestEntity> page = new PageImpl<>(iniPecBatchRequestEntities);

        when(infoCamereBatchRequestRepository.find(any(),any(),eq(InfoCamereBatchRequestEntity.class))).thenReturn(page);

        assertNotNull(infoCamereBatchRequestConnectorImpl.findAllByBatchId("batchId"));
    }


    @Test
    void testFindAllByBatchIdNotAndStatusWorking(){
        InfoCamereBatchRequestEntity infoCamereBatchRequestEntity = new InfoCamereBatchRequestEntity();
        infoCamereBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        infoCamereBatchRequestEntity.setBatchId("batchId");
        infoCamereBatchRequestEntity.setCf("taxId");
        infoCamereBatchRequestEntity.setRetry(0);
        infoCamereBatchRequestEntity.setStatus("status");
        infoCamereBatchRequestEntity.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequestEntity.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequestEntity.setTtl(LocalDateTime.now());
        List<InfoCamereBatchRequestEntity> iniPecBatchRequestEntities = new ArrayList<>();
        iniPecBatchRequestEntities.add(infoCamereBatchRequestEntity);
        Page<InfoCamereBatchRequestEntity> page = new PageImpl<>(iniPecBatchRequestEntities);

        when(infoCamereBatchRequestRepository.find(any(),any(),eq(InfoCamereBatchRequestEntity.class))).thenReturn(page);

        assertNotNull(infoCamereBatchRequestConnectorImpl.findAllByBatchIdNotAndStatusWorking("batchId"));
    }

    @Test
    void testSetBatchIdAndStatusWorking(){
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("6390cd95e28c6f70101f2e85");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("taxId");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());
        List<InfoCamereBatchRequest> infoCamereBatchRequests = new ArrayList<>();
        infoCamereBatchRequests.add(infoCamereBatchRequest);

        testSave();

        assertNotNull(infoCamereBatchRequestConnectorImpl.setBatchIdAndStatusWorking(infoCamereBatchRequests,"batchId"));
    }


    @Test
    void testConvertToIniPecBatchRequestEntity(){
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId("6390cd95e28c6f70101f2e85");
        infoCamereBatchRequest.setBatchId("batchId");
        infoCamereBatchRequest.setCf("taxId");
        infoCamereBatchRequest.setRetry(0);
        infoCamereBatchRequest.setStatus("status");
        infoCamereBatchRequest.setCorrelationId("correlationId");
        infoCamereBatchRequest.setLastReserved(LocalDateTime.now());
        infoCamereBatchRequest.setTimeStamp(LocalDateTime.now());
        infoCamereBatchRequest.setTtl(LocalDateTime.now());

        assertNotNull(infoCamereBatchRequestConnectorImpl.convertToIniPecBatchRequestEntity(infoCamereBatchRequest));
    }

}

