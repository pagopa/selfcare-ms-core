package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.BrokerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrokerMapper {

    @Mapping(target = "id", source = "taxCode")
    BrokerResponse toBroker(Institution institution);
    List<BrokerResponse> toBrokers(List<Institution> institutions);
}
