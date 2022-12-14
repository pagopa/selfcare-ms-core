package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchPollingEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoCamereBatchPollingRepository extends MongoRepository<InfoCamereBatchPollingEntity, ObjectId>, MongoCustomConnector  {

}
