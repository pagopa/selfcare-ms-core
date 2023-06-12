package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.institution.OnboardingResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {InstitutionUpdateMapper.class})
public interface OnboardingResourceMapper {

    OnboardingResponse toResponse(Onboarding onboarding);

    @Mapping(target = "contractFilePath", source = "contractImported.filePath")
    @Mapping(target = "contractCreatedAt", source = "contractImported.createdAt")
    @Mapping(target = "billingRequest", source = "billing")
    OnboardingRequest toOnboardingRequest(OnboardingInstitutionRequest onboardingInstitutionRequest);
}
