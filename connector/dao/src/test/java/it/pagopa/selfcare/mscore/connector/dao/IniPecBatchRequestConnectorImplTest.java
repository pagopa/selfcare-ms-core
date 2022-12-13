package it.pagopa.selfcare.mscore.connector.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchRequestEntity;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;

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
class IniPecBatchRequestConnectorImplTest {
    @InjectMocks
    private IniPecBatchRequestConnectorImpl iniPecBatchRequestConnectorImpl;
    @Mock
    private IniPecBatchRequestRepository iniPecBatchRequestRepository;


    @Test
    void testSave() {
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("6390cd95e28c6f70101f2e85");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("taxId");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());

        IniPecBatchRequestEntity iniPecBatchRequestEntity = new IniPecBatchRequestEntity();
        iniPecBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        iniPecBatchRequestEntity.setBatchId("batchId");
        iniPecBatchRequestEntity.setCf("taxId");
        iniPecBatchRequestEntity.setRetry(0);
        iniPecBatchRequestEntity.setStatus("status");
        iniPecBatchRequestEntity.setLastReserved(LocalDateTime.now());
        iniPecBatchRequestEntity.setTimeStamp(iniPecBatchRequest.getTimeStamp());
        iniPecBatchRequestEntity.setTtl(LocalDateTime.now());

        when(iniPecBatchRequestRepository.save(any())).thenReturn(iniPecBatchRequestEntity);

        assertNotNull(iniPecBatchRequestConnectorImpl.save(iniPecBatchRequest));
    }

    @Test
    void testFindAllByBatchId(){
        IniPecBatchRequestEntity iniPecBatchRequestEntity = new IniPecBatchRequestEntity();
        iniPecBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        iniPecBatchRequestEntity.setBatchId("batchId");
        iniPecBatchRequestEntity.setCf("taxId");
        iniPecBatchRequestEntity.setRetry(0);
        iniPecBatchRequestEntity.setStatus("status");
        iniPecBatchRequestEntity.setLastReserved(LocalDateTime.now());
        iniPecBatchRequestEntity.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequestEntity.setTtl(LocalDateTime.now());
        List<IniPecBatchRequestEntity> iniPecBatchRequestEntities = new ArrayList<>();
        iniPecBatchRequestEntities.add(iniPecBatchRequestEntity);
        Page<IniPecBatchRequestEntity> page = new PageImpl<>(iniPecBatchRequestEntities);

        when(iniPecBatchRequestRepository.find(any(),any(),eq(IniPecBatchRequestEntity.class))).thenReturn(page);

        assertNotNull(iniPecBatchRequestConnectorImpl.findAllByBatchId("batchId"));
    }


    @Test
    void testFindAllByBatchIdNotAndStatusWorking(){
        IniPecBatchRequestEntity iniPecBatchRequestEntity = new IniPecBatchRequestEntity();
        iniPecBatchRequestEntity.setId(new ObjectId("6390cd95e28c6f70101f2e85"));
        iniPecBatchRequestEntity.setBatchId("batchId");
        iniPecBatchRequestEntity.setCf("taxId");
        iniPecBatchRequestEntity.setRetry(0);
        iniPecBatchRequestEntity.setStatus("status");
        iniPecBatchRequestEntity.setLastReserved(LocalDateTime.now());
        iniPecBatchRequestEntity.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequestEntity.setTtl(LocalDateTime.now());
        List<IniPecBatchRequestEntity> iniPecBatchRequestEntities = new ArrayList<>();
        iniPecBatchRequestEntities.add(iniPecBatchRequestEntity);
        Page<IniPecBatchRequestEntity> page = new PageImpl<>(iniPecBatchRequestEntities);

        when(iniPecBatchRequestRepository.find(any(),any(),eq(IniPecBatchRequestEntity.class))).thenReturn(page);

        assertNotNull(iniPecBatchRequestConnectorImpl.findAllByBatchIdNotAndStatusWorking("batchId"));
    }

    @Test
    void testSetBatchIdAndStatusWorking(){
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("6390cd95e28c6f70101f2e85");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("taxId");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());
        List<IniPecBatchRequest> iniPecBatchRequests = new ArrayList<>();
        iniPecBatchRequests.add(iniPecBatchRequest);

        testSave();

        assertNotNull(iniPecBatchRequestConnectorImpl.setBatchIdAndStatusWorking(iniPecBatchRequests,"batchId"));
    }


    @Test
    void testConvertToIniPecBatchRequestEntity(){
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId("6390cd95e28c6f70101f2e85");
        iniPecBatchRequest.setBatchId("batchId");
        iniPecBatchRequest.setCf("taxId");
        iniPecBatchRequest.setRetry(0);
        iniPecBatchRequest.setStatus("status");
        iniPecBatchRequest.setCorrelationId("correlationId");
        iniPecBatchRequest.setLastReserved(LocalDateTime.now());
        iniPecBatchRequest.setTimeStamp(LocalDateTime.now());
        iniPecBatchRequest.setTtl(LocalDateTime.now());

        assertNotNull(iniPecBatchRequestConnectorImpl.convertToIniPecBatchRequestEntity(iniPecBatchRequest));
    }

}

