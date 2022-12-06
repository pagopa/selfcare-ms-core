package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OnboardingServiceImpl implements OnboardingService{

    private final ExternalService externalService;
    private final UserConnector userConnector;

    public OnboardingServiceImpl(ExternalService externalService, UserConnector userConnector) {
        this.externalService = externalService;
        this.userConnector = userConnector;
    }

    private final List<RelationshipState> validRelationshipStates =
    List.of(RelationshipState.ACTIVE,
            RelationshipState.DELETED,
            RelationshipState.SUSPENDED);

    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        List<User> response = retrieveUser(institution.getId(), productId);
        if(response.isEmpty())
           throw new ResourceNotFoundException("Relationship not found");
    }

    private List<User> retrieveUser(String to, String productId) {
        User user = new User();
        user.setInstitutionId(to);

        Product product = new Product();
        product.setProductId(productId);
        product.setRoles(List.of(PartyRole.MANAGER));

        user.setProducts(List.of(product));

        return userConnector.find(user,validRelationshipStates, productId);
    }
}
