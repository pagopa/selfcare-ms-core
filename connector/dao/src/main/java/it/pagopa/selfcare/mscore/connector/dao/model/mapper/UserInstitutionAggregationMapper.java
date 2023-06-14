package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionAggregationEntity;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {InstitutionEntityMapper.class})
public interface UserInstitutionAggregationMapper {

    UserInstitutionAggregation constructUserInstitutionAggregation(UserInstitutionAggregationEntity entity);
}
