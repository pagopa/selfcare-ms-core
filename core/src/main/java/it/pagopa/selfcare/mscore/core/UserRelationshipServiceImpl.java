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

    private final UserEventService userEventService;

    public UserRelationshipServiceImpl(OnboardingDao onboardingDao, UserConnector userConnector, InstitutionService institutionService, UserEventService userEventService) {
        this.onboardingDao = onboardingDao;
        this.userConnector = userConnector;
        this.institutionService = institutionService;
        this.userEventService = userEventService;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public void activateRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.ACTIVE);
            sendRelationshipEventNotification(relationshipId);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.SUSPENDED);
            sendRelationshipEventNotification(relationshipId);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    private void sendRelationshipEventNotification(String relationshipId){
        RelationshipInfo relationshipInfo = retrieveRelationship(relationshipId);
        userEventService.sendOperatorUserNotification(relationshipInfo);
    }
    @Override
    public void deleteRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, RelationshipState.DELETED);
        sendRelationshipEventNotification(relationshipId);
    }

    @Override
    public RelationshipInfo retrieveRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
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
}
