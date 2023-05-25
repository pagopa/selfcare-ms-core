package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.OnboardingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OnboardingResourceMapper {

    OnboardingResponse toResponse(Onboarding onboarding);
}
