package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.NameEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NameRepository extends MongoRepository<NameEntity, String> {//TODO change Name
}
