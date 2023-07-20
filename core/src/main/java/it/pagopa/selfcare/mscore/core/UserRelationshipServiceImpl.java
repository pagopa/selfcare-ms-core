package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.mscore.constant.CustomError.*;

@Slf4j
@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final OnboardingDao onboardingDao;
    private final UserConnector userConnector;
    private final InstitutionService institutionService;
    private final UserNotificationService userNotificationService;

    public UserRelationshipServiceImpl(OnboardingDao onboardingDao,
                                       UserConnector userConnector,
                                       InstitutionService institutionService,
                                       UserNotificationService userNotificationService) {
        this.onboardingDao = onboardingDao;
        this.userConnector = userConnector;
        this.institutionService = institutionService;
        this.userNotificationService = userNotificationService;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public void activateRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.ACTIVE);
            userNotificationService.sendActivatedUserNotification(relationshipId, user.getId(), userBinding);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.SUSPENDED);
            userNotificationService.sendSuspendedUserNotification(relationshipId, user.getId(), userBinding);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    @Override
    public void deleteRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.DELETED);
        userNotificationService.sendDeletedUserNotification(relationshipId, user.getId(), userBinding);
    }

    @Override
    public RelationshipInfo retrieveRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        return userBinding.getProducts().stream()
                .filter(product -> relationshipId.equalsIgnoreCase(product.getRelationshipId()))
                .findAny()
                .map(product -> constructRelationshipInfo(userBinding.getInstitutionId(), user.getId(), product))
                .orElseThrow(() -> new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode()));
    }

    private RelationshipInfo constructRelationshipInfo(String institutionId, String userId, OnboardedProduct product) {
        Institution institution = institutionService.retrieveInstitutionById(institutionId);
        return new RelationshipInfo(institution, userId, product);
    }

    private UserBinding retrieveUserBinding(OnboardedUser user, String relationshipId) {
        if (user.getBindings() != null) {
            return user.getBindings().stream()
                    .filter(binding -> binding.getProducts().stream().anyMatch(product -> relationshipId.equalsIgnoreCase(product.getRelationshipId())))
                    .findAny()
                    .orElseThrow(() -> new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode()));
        }else{
            throw new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode());

        }
    }
}
