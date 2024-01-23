package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingUsersRequest;
import it.pagopa.selfcare.mscore.web.model.institution.BillingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionOnboardingRequest;
import it.pagopa.selfcare.mscore.web.model.institution.OnboardingResponse;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionRequest;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardingInstitutionUsersRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = {InstitutionUpdateMapper.class})
public interface OnboardingResourceMapper {

    OnboardingResponse toResponse(Onboarding onboarding);

    @Mapping(target = "contractFilePath", source = "contractImported.filePath")
    @Mapping(target = "contractCreatedAt", source = "contractImported.createdAt")
    @Mapping(target = "billingRequest", source = "billing")
    @Mapping(target = "signContract", source = "signContract", qualifiedByName = "mapSignContract")
    @Mapping(target = "contractActivatedAt", source = "contractImported.activatedAt")
    OnboardingRequest toOnboardingRequest(OnboardingInstitutionRequest onboardingInstitutionRequest);

    OnboardingUsersRequest toOnboardingUsersRequest(OnboardingInstitutionUsersRequest request);

    @Named("mapSignContract")
    default Boolean mapSignContract(Boolean signContract) {
        return Optional.ofNullable(signContract).orElse(true);
    }

    @Mapping(target = "contract", source = "contractPath")
    Onboarding toOnboarding(InstitutionOnboardingRequest onboardingRequest);
}
