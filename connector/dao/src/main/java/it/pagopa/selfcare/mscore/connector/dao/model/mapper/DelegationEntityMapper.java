package it.pagopa.selfcare.mscore.connector.dao.model.mapper;


import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface DelegationEntityMapper {

    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID().toString())")
    DelegationEntity convertToDelegationEntity(Delegation delegation);

    Delegation convertToDelegation(DelegationEntity entity);

}
