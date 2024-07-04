package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.PecNotificationEntity;
import it.pagopa.selfcare.mscore.model.pecnotification.PecNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface PecNotificationEntityMapper {

    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID().toString())")
    PecNotificationEntity convertToPecNotificationEntity(PecNotification institution);

    PecNotification convertToPecNotification(PecNotificationEntity entity);
}
