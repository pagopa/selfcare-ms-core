package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.core.strategy.input.OnboardingInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;

import java.util.ArrayList;
import java.util.function.Consumer;

public class OnboardingInstitutionStrategy {

    private final Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy;
    private final Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy;
    private final Consumer<OnboardingInstitutionStrategyInput> persitOnboardingInstitutionStrategy;
    private final Consumer<OnboardingInstitutionStrategyInput> emailsOnboardingInstitutionStrategy;

    public OnboardingInstitutionStrategy(Consumer<OnboardingInstitutionStrategyInput> verifyAndFillInstitutionAttributeStrategy,
                                         Consumer<OnboardingInstitutionStrategyInput> digestOnboardingInstitutionStrategy,
                                         Consumer<OnboardingInstitutionStrategyInput> persitOnboardingInstitutionStrategy,
                                         Consumer<OnboardingInstitutionStrategyInput> emailsOnboardingInstitutionStrategy) {
        this.verifyAndFillInstitutionAttributeStrategy = verifyAndFillInstitutionAttributeStrategy;
        this.digestOnboardingInstitutionStrategy = digestOnboardingInstitutionStrategy;
        this.persitOnboardingInstitutionStrategy = persitOnboardingInstitutionStrategy;
        this.emailsOnboardingInstitutionStrategy = emailsOnboardingInstitutionStrategy;
    }


    public void onboardingInstitution(OnboardingRequest request, SelfCareUser principal) {

        request.setTokenType(TokenType.INSTITUTION);

        OnboardingInstitutionStrategyInput institutionStrategyInput = OnboardingInstitutionStrategyInput.builder()
                .principal(principal)
                .onboardingRequest(request)
                .toDelete(new ArrayList<>())
                .toUpdate(new ArrayList<>())
                .build();

        verifyAndFillInstitutionAttributeStrategy.accept(institutionStrategyInput);

        digestOnboardingInstitutionStrategy.accept(institutionStrategyInput);

        persitOnboardingInstitutionStrategy.accept(institutionStrategyInput);

        emailsOnboardingInstitutionStrategy.accept(institutionStrategyInput);
    }
}
