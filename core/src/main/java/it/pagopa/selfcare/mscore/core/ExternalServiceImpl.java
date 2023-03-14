package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.OnboardingPage;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.model.user.RelationshipInfo;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import static it.pagopa.selfcare.mscore.constant.GenericError.INSTITUTION_MANAGER_ERROR;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionService institutionService;
    private final TokenService tokenService;
    private final UserService userService;

    public ExternalServiceImpl(InstitutionService institutionService, TokenService tokenService, UserService userService) {
        this.institutionService = institutionService;
        this.tokenService = tokenService;
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
    public String retrieveRelationship(ProductManagerInfo manager, String productId) {
        return tokenService.findActiveContract(manager.getInstitution().getId(), manager.getUserId(), productId);
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionService.retrieveInstitutionProduct(externalId, productId);
    }

    @Override
    public OnboardingPage retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states, Pageable pageable) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institutionService.retrieveInstitutionProducts(institution, states, pageable);
    }

    @Override
    public List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId, Pageable pageable) {
        log.info("Retrieving geographic taxonomies for institution having externalId {}", externalId);
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institutionService.retrieveInstitutionGeoTaxonomies(institution, pageable).getData();
    }

    @Override
    public List<RelationshipInfo> getUserInstitutionRelationships(String externalId,
                                                                  String userId,
                                                                  String personId,
                                                                  List<PartyRole> roles,
                                                                  List<RelationshipState> states,
                                                                  List<String> products,
                                                                  List<String> productRoles,
                                                                  Pageable pageable) {
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institutionService.getUserInstitutionRelationships(institution, userId, personId, roles, states, products, productRoles, pageable);
    }
}
