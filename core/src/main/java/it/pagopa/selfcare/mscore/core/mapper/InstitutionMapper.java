package it.pagopa.selfcare.mscore.core.mapper;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.NationalRegistriesProfessionalAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "origin", ignore = true)
    Institution fromInstitutionProxyInfo(InstitutionProxyInfo proxyInfo);

    Institution fromProfessionalAddress(NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress);
}
