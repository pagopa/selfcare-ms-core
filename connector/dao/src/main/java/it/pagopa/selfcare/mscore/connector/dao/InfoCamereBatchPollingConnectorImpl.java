package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InfoCamereBatchPollingConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchPollingEntity;
import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchPolling;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoCamereBatchPollingConnectorImpl implements InfoCamereBatchPollingConnector {

    private final InfoCamereBatchPollingRepository repository;

    @Autowired
    public InfoCamereBatchPollingConnectorImpl(InfoCamereBatchPollingRepository repository) {
        this.repository = repository;
    }

    @Override
    public InfoCamereBatchPolling save(InfoCamereBatchPolling infoCamereBatchPolling) {
        final InfoCamereBatchPollingEntity entity = new InfoCamereBatchPollingEntity(infoCamereBatchPolling);
        return convertToIniPecBatchPolling(repository.save(entity));
    }

    @Override
    public List<InfoCamereBatchPolling> findByStatus(String status){
        Pageable pageableRequest = PageRequest.of(0, 1);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("status").is(status)
                )
        );
        return repository.find(query, pageableRequest, InfoCamereBatchPollingEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchPolling)
                .collect(Collectors.toList());
    }

    private InfoCamereBatchPolling convertToIniPecBatchPolling(InfoCamereBatchPollingEntity entity) {
        InfoCamereBatchPolling infoCamereBatchPolling = new InfoCamereBatchPolling();
        infoCamereBatchPolling.setId(entity.getId().toString());
        infoCamereBatchPolling.setPollingId(entity.getPollingId());
        infoCamereBatchPolling.setBatchId(entity.getBatchId());
        infoCamereBatchPolling.setTimeStamp(entity.getTimeStamp());
        infoCamereBatchPolling.setStatus(entity.getStatus());
        return infoCamereBatchPolling;
    }

    public InfoCamereBatchPollingEntity convertToIniPecBatchPollingEntity(InfoCamereBatchPolling infoCamereBatchPolling) {
        InfoCamereBatchPollingEntity entity = new InfoCamereBatchPollingEntity();
        if (infoCamereBatchPolling.getId() != null) {
            entity.setId(new ObjectId(infoCamereBatchPolling.getId()));
        }
        entity.setBatchId(infoCamereBatchPolling.getBatchId());
        entity.setPollingId(infoCamereBatchPolling.getPollingId());
        entity.setTimeStamp(infoCamereBatchPolling.getTimeStamp());
        entity.setStatus(infoCamereBatchPolling.getStatus());
        return entity;
    }
}
