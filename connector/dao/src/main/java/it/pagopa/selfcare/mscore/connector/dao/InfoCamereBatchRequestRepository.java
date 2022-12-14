package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InfoCamereBatchRequestEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoCamereBatchRequestRepository extends MongoRepository<InfoCamereBatchRequestEntity, ObjectId>, MongoCustomConnector {
}
