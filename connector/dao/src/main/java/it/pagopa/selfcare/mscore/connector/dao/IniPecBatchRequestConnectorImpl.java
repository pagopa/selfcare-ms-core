package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.IniPecBatchRequestConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchRequestEntity;
import it.pagopa.selfcare.mscore.model.inipec.BatchStatus;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
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
public class IniPecBatchRequestConnectorImpl implements IniPecBatchRequestConnector {

    private final IniPecBatchRequestRepository repository;

    @Autowired
    public IniPecBatchRequestConnectorImpl(IniPecBatchRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public IniPecBatchRequest save(IniPecBatchRequest iniPecBatchRequest) {
        final IniPecBatchRequestEntity entity = new IniPecBatchRequestEntity(iniPecBatchRequest);
        return convertToIniPecBatchRequest(repository.save(entity));
    }

    @Override
    public List<IniPecBatchRequest> findAllByBatchId(String batchId){
        Pageable pageableRequest = PageRequest.of(0, 5);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("batchId").is(batchId)
                )
        );
        return repository.find(query, pageableRequest, IniPecBatchRequestEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<IniPecBatchRequest> findAllByBatchIdNotAndStatusWorking(String batchId){
        Pageable pageableRequest = PageRequest.of(0, 5);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("batchId").ne(batchId),
                        Criteria.where("status").is(BatchStatus.WORKING.getValue())
                )
        );
        return repository.find(query, pageableRequest, IniPecBatchRequestEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchRequest)
                .collect(Collectors.toList());
    }

    @Override
    public List<IniPecBatchRequest> setBatchIdAndStatusWorking(List<IniPecBatchRequest> iniPecBatchRequests, String batchId){
        return iniPecBatchRequests.stream().map(iniPecBatchRequest -> {
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

    private IniPecBatchRequest convertToIniPecBatchRequest(IniPecBatchRequestEntity entity) {
        IniPecBatchRequest iniPecBatchRequest = new IniPecBatchRequest();
        iniPecBatchRequest.setId(entity.getId().toString());
        iniPecBatchRequest.setBatchId(entity.getBatchId());
        iniPecBatchRequest.setCf(entity.getCf());
        iniPecBatchRequest.setStatus(entity.getStatus());
        iniPecBatchRequest.setRetry(entity.getRetry());
        iniPecBatchRequest.setTtl(entity.getTtl());
        iniPecBatchRequest.setLastReserved(entity.getLastReserved());
        return iniPecBatchRequest;
    }

    public IniPecBatchRequestEntity convertToIniPecBatchRequestEntity(IniPecBatchRequest iniPecBatchRequest) {
        IniPecBatchRequestEntity entity = new IniPecBatchRequestEntity();
        if (iniPecBatchRequest.getId() != null) {
            entity.setId(new ObjectId(iniPecBatchRequest.getId()));
        }
        entity.setBatchId(iniPecBatchRequest.getBatchId());
        entity.setTimeStamp(iniPecBatchRequest.getTimeStamp());
        entity.setTtl(iniPecBatchRequest.getTtl());
        entity.setRetry(iniPecBatchRequest.getRetry());
        entity.setCf(iniPecBatchRequest.getCf());
        entity.setStatus(iniPecBatchRequest.getStatus());
        entity.setLastReserved(iniPecBatchRequest.getLastReserved());
        return entity;
    }
}
