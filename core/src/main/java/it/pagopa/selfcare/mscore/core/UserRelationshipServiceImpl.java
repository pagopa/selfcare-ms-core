package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.RELATIONSHIP_ID_NOT_FOUND;

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final OnboardingDao onboardingDao;
    private final UserConnector userConnector;
    private final InstitutionService institutionService;

    public UserRelationshipServiceImpl(OnboardingDao onboardingDao, UserConnector userConnector, InstitutionService institutionService) {
        this.onboardingDao = onboardingDao;
        this.userConnector = userConnector;
        this.institutionService = institutionService;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public void activateRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.SUSPENDED), RelationshipState.ACTIVE);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.ACTIVE), RelationshipState.SUSPENDED);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    @Override
    public void deleteRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.values()), RelationshipState.DELETED);
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
