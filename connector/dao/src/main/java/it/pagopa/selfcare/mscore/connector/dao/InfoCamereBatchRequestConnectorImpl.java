package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchRequestConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchRequestEntity;
import it.pagopa.selfcare.mscore.model.infocamere.BatchStatus;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoCamereBatchRequestConnectorImpl implements InfoCamereBatchRequestConnector {

    private final InfoCamereBatchRequestRepository repository;

    @Autowired
    public InfoCamereBatchRequestConnectorImpl(InfoCamereBatchRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public InfoCamereBatchRequest save(InfoCamereBatchRequest infoCamereBatchRequest) {
        final InfoCamereBatchRequestEntity entity = new InfoCamereBatchRequestEntity(infoCamereBatchRequest);
        return convertToIniPecBatchRequest(repository.save(entity));
    }

    @Override
    public List<InfoCamereBatchRequest> findAllByBatchId(String batchId){
        Pageable pageableRequest = PageRequest.of(0, 5);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("batchId").is(batchId)
                )
        );
        return repository.find(query, pageableRequest, InfoCamereBatchRequestEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<InfoCamereBatchRequest> findAllByBatchIdNotAndStatusWorking(String batchId){
        Pageable pageableRequest = PageRequest.of(0, 5);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("batchId").ne(batchId),
                        Criteria.where("status").is(BatchStatus.WORKING.getValue())
                )
        );
        return repository.find(query, pageableRequest, InfoCamereBatchRequestEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<InfoCamereBatchRequest> setBatchIdAndStatusWorking(List<InfoCamereBatchRequest> infoCamereBatchRequests, String batchId){
        return infoCamereBatchRequests.stream().map(iniPecBatchRequest -> {
            iniPecBatchRequest.setLastReserved(LocalDateTime.now());
            iniPecBatchRequest.setBatchId(batchId);
            iniPecBatchRequest.setRetry(iniPecBatchRequest.getRetry()+1);
            iniPecBatchRequest.setStatus(BatchStatus.WORKING.getValue());
            if(iniPecBatchRequest.getRetry()>3){
                iniPecBatchRequest.setStatus(BatchStatus.ERROR.getValue());
            }
            return save(iniPecBatchRequest);
        }).collect(Collectors.toList());
    }

    private InfoCamereBatchRequest convertToIniPecBatchRequest(InfoCamereBatchRequestEntity entity) {
        InfoCamereBatchRequest infoCamereBatchRequest = new InfoCamereBatchRequest();
        infoCamereBatchRequest.setId(entity.getId().toString());
        infoCamereBatchRequest.setBatchId(entity.getBatchId());
        infoCamereBatchRequest.setCf(entity.getCf());
        infoCamereBatchRequest.setStatus(entity.getStatus());
        infoCamereBatchRequest.setRetry(entity.getRetry());
        infoCamereBatchRequest.setTtl(entity.getTtl());
        infoCamereBatchRequest.setLastReserved(entity.getLastReserved());
        return infoCamereBatchRequest;
    }

    public InfoCamereBatchRequestEntity convertToIniPecBatchRequestEntity(InfoCamereBatchRequest infoCamereBatchRequest) {
        InfoCamereBatchRequestEntity entity = new InfoCamereBatchRequestEntity();
        if (infoCamereBatchRequest.getId() != null) {
            entity.setId(new ObjectId(infoCamereBatchRequest.getId()));
        }
        entity.setBatchId(infoCamereBatchRequest.getBatchId());
        entity.setTimeStamp(infoCamereBatchRequest.getTimeStamp());
        entity.setTtl(infoCamereBatchRequest.getTtl());
        entity.setRetry(infoCamereBatchRequest.getRetry());
        entity.setCf(infoCamereBatchRequest.getCf());
        entity.setStatus(infoCamereBatchRequest.getStatus());
        entity.setLastReserved(infoCamereBatchRequest.getLastReserved());
        return entity;
    }
}
