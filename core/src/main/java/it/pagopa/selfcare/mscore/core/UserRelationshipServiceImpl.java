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

    private final UserEventService userEventService;

    public UserRelationshipServiceImpl(OnboardingDao onboardingDao, UserConnector userConnector, InstitutionService institutionService, UserEventService userEventService, UserNotificationService userNotificationService) {
        this.onboardingDao = onboardingDao;
        this.userConnector = userConnector;
        this.institutionService = institutionService;
        this.userNotificationService = userNotificationService;
        this.userEventService = userEventService;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public void activateRelationship(String relationshipId,String loggedUserName, String loggedUserSurname) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.ACTIVE);
            sendRelationshipEventNotification(user, relationshipId);
            userNotificationService.sendActivatedUserNotification(relationshipId, user.getId(), userBinding, loggedUserName, loggedUserSurname);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId, String loggedUserName, String loggedUserSurname) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.SUSPENDED);
            userNotificationService.sendSuspendedUserNotification(relationshipId, user.getId(), userBinding, loggedUserName, loggedUserSurname);
            sendRelationshipEventNotification(user, relationshipId);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    private void sendRelationshipEventNotification(OnboardedUser user, String relationshipId){
        RelationshipInfo relationshipInfo = getRelationshipInfoFromUser(user, relationshipId);
        userEventService.sendOperatorUserNotification(relationshipInfo);
    }
    @Override
    public void deleteRelationship(String relationshipId, String loggedUserName, String loggedUserSurname) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        UserBinding userBinding = retrieveUserBinding(user, relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.DELETED);
        sendRelationshipEventNotification(user, relationshipId);
        userNotificationService.sendDeletedUserNotification(relationshipId, user.getId(), userBinding, loggedUserName, loggedUserSurname);
    }

    @Override
    public RelationshipInfo retrieveRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        return getRelationshipInfoFromUser(user, relationshipId);
    }

    private RelationshipInfo getRelationshipInfoFromUser(OnboardedUser user, String relationshipId){
        for (UserBinding userBinding : user.getBindings()) {
            for (OnboardedProduct product : userBinding.getProducts()) {
                if (relationshipId.equalsIgnoreCase(product.getRelationshipId())) {
                    Institution institution = institutionService.retrieveInstitutionById(userBinding.getInstitutionId());
                    return new RelationshipInfo(institution, user.getId(), product);
                }
            }
        }
        throw new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode());
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
