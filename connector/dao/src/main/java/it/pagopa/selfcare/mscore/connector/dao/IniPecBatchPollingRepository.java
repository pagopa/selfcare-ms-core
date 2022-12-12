package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.IniPecBatchPollingEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IniPecBatchPollingRepository extends MongoRepository<IniPecBatchPollingEntity, ObjectId>, MongoCustomConnector  {

}
