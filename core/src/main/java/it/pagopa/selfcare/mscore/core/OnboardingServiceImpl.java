package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.model.OnboardingData;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.INSTITUTION_NOT_ONBOARDED;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final ExternalService externalService;
    private final UserConnector userConnector;
    private final InstitutionConnector institutionConnector;

    public OnboardingServiceImpl(ExternalService externalService, UserConnector userConnector, InstitutionConnector institutionConnector) {
        this.externalService = externalService;
        this.userConnector = userConnector;
        this.institutionConnector = institutionConnector;
    }

    private final List<RelationshipState> validRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.DELETED,
                    RelationshipState.SUSPENDED);

    private final List<RelationshipState> defaultRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.PENDING);


    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        log.info("Verifying onboarding for institution having externalId {} on product {}", externalId, productId);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        List<Onboarding> response = institution.getOnboarding().stream()
                .filter(onboarding -> productId.equalsIgnoreCase(onboarding.getProductId())
                            && validRelationshipStates.contains(onboarding.getStatus()))
                .collect(Collectors.toList());
        if (response.isEmpty())
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId), INSTITUTION_NOT_ONBOARDED.getCode());
    }

    @Override
    public OnboardingData getOnboardingInfo(String institutionId, String institutionExternalId, String[] states, String userId) {
        Institution institution = new Institution();
        Optional<Institution> opt = null;
        if (StringUtils.hasText(institutionId))
            opt = institutionConnector.findById(institutionId);
        else if (StringUtils.hasText(institutionExternalId))
            opt = institutionConnector.findByExternalId(institutionExternalId);

        if(opt!=null && opt.isPresent())
             institution = opt.get();

        List<RelationshipState> stateList;
        if (states == null || states.length == 0)
            stateList = defaultRelationshipStates;
        else {
            stateList = new ArrayList<>();
            Arrays.stream(states).forEach(s -> stateList.add(RelationshipState.valueOf(s)));
        }

        OnboardedUser onboardedUser= new OnboardedUser();
        List<UserInstitution> userInstitutionList = new ArrayList<>();

        List<OnboardedUser> onboardedUserList = userConnector.findForGetOnboardingInfo(userId, institution.getId(), stateList);
        if(onboardedUserList!=null && !onboardedUserList.isEmpty()) {
            onboardedUser = onboardedUserList.get(0);
        }

        if (onboardedUser.getInstitutions() != null && !onboardedUser.getInstitutions().isEmpty()) {
             userInstitutionList = onboardedUser.getInstitutions().stream()
                    .filter(userInstitution -> userInstitution.getProducts().stream()
                            .anyMatch(product -> stateList.contains(product.getStatus())) &&
                            userInstitution.getProducts() != null && userInstitution.getProducts().isEmpty()).collect(Collectors.toList());
        }

        List<OnboardingData> onboardingDataList = new ArrayList<>();
        userInstitutionList.forEach(userInstitution -> {
            OnboardingData onboardingData = new OnboardingData();
            onboardingDataList.add(onboardingData);
        });

        return new OnboardingData();

    }

    private List<OnboardedUser> retrieveUser(String to, String productId, List<RelationshipState> states) {
        return userConnector.findForVerifyOnboardingInfo(to, states, productId);
    }
}
