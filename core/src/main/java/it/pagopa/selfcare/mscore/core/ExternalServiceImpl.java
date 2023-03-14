package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import static it.pagopa.selfcare.mscore.constant.GenericError.INSTITUTION_MANAGER_ERROR;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionService institutionService;
    private final UserService userService;

    public ExternalServiceImpl(InstitutionService institutionService, UserService userService) {
        this.institutionService = institutionService;
        this.userService = userService;
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        return institutionService.retrieveInstitutionByExternalId(externalId);
    }

    @Override
    public ProductManagerInfo retrieveInstitutionManager(String externalId, String productId) {
       Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
       if(institution.getOnboarding()!=null) {
           OnboardedUser onboardedUser = userService.findOnboardedManager(institution.getId(), productId, List.of(RelationshipState.ACTIVE));
           for (UserBinding userBinding : onboardedUser.getBindings()) {
               if (institution.getId().equalsIgnoreCase(userBinding.getInstitutionId())) {
                   return new ProductManagerInfo(onboardedUser.getId(), institution, userBinding.getProducts());
               }
           }
       }
       throw new InvalidRequestException(INSTITUTION_MANAGER_ERROR.getMessage(),INSTITUTION_MANAGER_ERROR.getCode());
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionService.retrieveInstitutionProduct(externalId, productId);
    }

    @Override
    public List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states){
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institutionService.retrieveInstitutionProducts(institution, states);
    }

    @Override
    public Institution createPnPgInstitution(String taxId, String description) {
        return institutionService.createPnPgInstitution(taxId, description);
    }

    @Override
    public List<RelationshipInfo> getUserInstitutionRelationships(String externalId,
                                                                  String userId,
                                                                  String personId,
                                                                  List<PartyRole> roles,
                                                                  List<RelationshipState> states,
                                                                  List<String> products,
                                                                  List<String> productRoles) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institutionService.retrieveUserInstitutionRelationships(institution, userId, personId, roles, states, products, productRoles);
    }
}
