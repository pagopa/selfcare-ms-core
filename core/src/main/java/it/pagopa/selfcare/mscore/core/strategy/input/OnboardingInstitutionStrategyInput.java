package it.pagopa.selfcare.mscore.core.strategy.input;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRollback;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class OnboardingInstitutionStrategyInput {

    private OnboardingRequest onboardingRequest;
    private SelfCareUser principal;

    private List<String> toUpdate;
    private List<String>  toDelete;
    private Institution institution;
    private List<InstitutionGeographicTaxonomies> institutionUpdateGeographicTaxonomies;

    private File pdf;
    private String digest;

    private OnboardingRollback onboardingRollback;
}
