package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchRequestEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IniPecBatchRequestRepository extends MongoRepository<IniPecBatchRequestEntity, ObjectId>, MongoCustomConnector {
}
