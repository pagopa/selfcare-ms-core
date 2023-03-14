package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.springframework.stereotype.Service;
import static it.pagopa.selfcare.mscore.constant.CustomError.RELATIONSHIP_ID_NOT_FOUND;

@Service
public class UserRelationshipServiceImpl implements UserRelationshipService {

    private final UserConnector userConnector;
    private final InstitutionService institutionService;

    public UserRelationshipServiceImpl(UserConnector userConnector, InstitutionService institutionService) {
        this.userConnector = userConnector;
        this.institutionService = institutionService;
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
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
