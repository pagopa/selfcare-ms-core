package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.PecNotificationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.PecNotificationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.PecNotificationEntityMapper;
import it.pagopa.selfcare.mscore.model.pecnotification.PecNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PecNotificationConnectorImpl implements PecNotificationConnector {

    private final PecNotificationRepository repository;
    private final PecNotificationEntityMapper pecNotificationMapper;

    public PecNotificationConnectorImpl(PecNotificationRepository repository, PecNotificationEntityMapper pecNotificationMapper) {
        this.repository = repository;
        this.pecNotificationMapper = pecNotificationMapper;
    }

    @Override
    public boolean findAndDeletePecNotification(String institutionId, String productId) {

        Query query = Query.query(Criteria.where(PecNotificationEntity.Fields.institutionId.name()).is(institutionId)
                .and(PecNotificationEntity.Fields.productId.name()).is(productId));

        List<PecNotificationEntity> pecNotificationEntityList = repository.find(query, PecNotificationEntity.class);

        if(Objects.nonNull(pecNotificationEntityList) && pecNotificationEntityList.size() == 1) {
            repository.delete(pecNotificationEntityList.get(0));
            log.trace("Deleted PecNotification with institutionId: {} and productId: {}", institutionId, productId);
            return true;
        }
        
        if (Objects.nonNull(pecNotificationEntityList) && pecNotificationEntityList.size() > 1) {
        	log.trace("Cannot delete PecNotification with institutionId: {} and productId: {}, because there are multiple entries", institutionId, productId);
        	return false;
        }

        log.trace("Cannot delete PecNotification with institutionId: {} and productId: {}, because it does not exist", institutionId, productId);
        return false;
    }

    @Override
    public boolean insertPecNotification(PecNotification pecNotification){

        PecNotificationEntity pecNotificationEntity = this.pecNotificationMapper.convertToPecNotificationEntity(pecNotification);

        boolean exist = repository.existsById(pecNotificationEntity.getId());

        if (exist){
        	log.trace("Cannot insert the PecNotification: {}, as it already exists in the collection", pecNotification.toString());
            return false;
        }

        repository.insert(pecNotificationEntity);
        log.trace("Inserted PecNotification: {}", pecNotification.toString());
        return true;
    }

}
