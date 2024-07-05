package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.PecNotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PecNotificationRepository  extends MongoRepository<PecNotificationEntity, String>, MongoCustomConnector {
}
