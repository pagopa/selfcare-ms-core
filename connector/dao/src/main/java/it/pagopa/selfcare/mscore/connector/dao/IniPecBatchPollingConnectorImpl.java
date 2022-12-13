package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.IniPecBatchPollingConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchPollingEntity;
import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchPolling;
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
public class IniPecBatchPollingConnectorImpl implements IniPecBatchPollingConnector {

    private final IniPecBatchPollingRepository repository;

    @Autowired
    public IniPecBatchPollingConnectorImpl(IniPecBatchPollingRepository repository) {
        this.repository = repository;
    }

    @Override
    public IniPecBatchPolling save(IniPecBatchPolling iniPecBatchPolling) {
        final IniPecBatchPollingEntity entity = new IniPecBatchPollingEntity(iniPecBatchPolling);
        return convertToIniPecBatchPolling(repository.save(entity));
    }

    @Override
    public List<IniPecBatchPolling> findByStatus(String status){
        Pageable pageableRequest = PageRequest.of(0, 1);
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("status").is(status)
                )
        );
        return repository.find(query, pageableRequest, IniPecBatchPollingEntity.class)
                .getContent()
                .stream()
                .map(this::convertToIniPecBatchPolling)
                .collect(Collectors.toList());
    }

    private IniPecBatchPolling convertToIniPecBatchPolling(IniPecBatchPollingEntity entity) {
        IniPecBatchPolling iniPecBatchPolling = new IniPecBatchPolling();
        iniPecBatchPolling.setId(entity.getId().toString());
        iniPecBatchPolling.setPollingId(entity.getPollingId());
        iniPecBatchPolling.setBatchId(entity.getBatchId());
        iniPecBatchPolling.setTimeStamp(entity.getTimeStamp());
        iniPecBatchPolling.setStatus(entity.getStatus());
        return iniPecBatchPolling;
    }

    public IniPecBatchPollingEntity convertToIniPecBatchPollingEntity(IniPecBatchPolling iniPecBatchPolling) {
        IniPecBatchPollingEntity entity = new IniPecBatchPollingEntity();
        if (iniPecBatchPolling.getId() != null) {
            entity.setId(new ObjectId(iniPecBatchPolling.getId()));
        }
        entity.setBatchId(iniPecBatchPolling.getBatchId());
        entity.setPollingId(iniPecBatchPolling.getPollingId());
        entity.setTimeStamp(iniPecBatchPolling.getTimeStamp());
        entity.setStatus(iniPecBatchPolling.getStatus());
        return entity;
    }
}
