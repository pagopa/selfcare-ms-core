package it.pagopa.selfcare.mscore.connector.rest.mapper;

import it.pagopa.selfcare.mscore.connector.rest.model.InfocamerePdndResponse;
import it.pagopa.selfcare.mscore.model.institution.InfocamerePdndInstitution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InfocamereMapper {
    InfocamerePdndInstitution toInstitution(InfocamerePdndResponse response);
}
