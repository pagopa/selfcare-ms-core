package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.PRODUCTS_NOT_FOUND_ERROR;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionService institutionService;
    private final TokenService tokenService;
    private final UserService userService;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;

    public ExternalServiceImpl(InstitutionService institutionService, TokenService tokenService, UserService userService, GeoTaxonomiesConnector geoTaxonomiesConnector) {
        this.institutionService = institutionService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        return institutionService.retrieveInstitutionByExternalId(externalId);
    }

    @Override
    public OnboardedUser retrieveInstitutionManager(Institution institution, String productId) {
        return userService.findOnboardedManager(institution.getId(), productId, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public String retrieveRelationship(String institutionId, String userId, String productId) {
        return tokenService.findActiveContract(institutionId, userId, productId);
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionService.getInstitutionProduct(externalId, productId);
    }

    @Override
    public List<Onboarding> retrieveInstitutionProductsByExternalId(String externalId, List<RelationshipState> states){
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        if (institution.getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                return institution.getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus()))
                        .collect(Collectors.toList());
            } else {
                return institution.getOnboarding();
            }
        } else {
            throw new ResourceNotFoundException(String.format(PRODUCTS_NOT_FOUND_ERROR.getMessage(), externalId), PRODUCTS_NOT_FOUND_ERROR.getCode());
        }
    }

    @Override
    public List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId) {
        log.info("Retrieving geographic taxonomies for institution having externalId {}", externalId);
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institution.getGeographicTaxonomies().stream()
                .map(GeographicTaxonomies::getCode)
                .map(geoTaxonomiesConnector::getExtByCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<RelationshipInfo> getUserInstitutionRelationships(EnvEnum env, String externalId, String userId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles){
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        List<OnboardedUser> adminRelationships = userService.retrieveAdminUsers(institution.getId(), userId, env);
        List<OnboardedUser> institutionRelationships = userService.retrieveUsers(institution.getId(), personId, env, roles, states, products, productRoles);
        if(!adminRelationships.isEmpty()){
            return toOnboardingInfo(institutionRelationships, institution);
        }else{
            List<OnboardedUser> filterInstitutionRelationships = institutionRelationships.stream().filter(user -> userId.equalsIgnoreCase(user.getId())).collect(Collectors.toList());
            return toOnboardingInfo(filterInstitutionRelationships, institution);
        }
    }

    private List<RelationshipInfo> toOnboardingInfo(List<OnboardedUser> institutionRelationships, Institution institution) {
       List<RelationshipInfo> relationshipInfoList = new ArrayList<>();
        for (OnboardedUser user : institutionRelationships) {
            for(UserBinding binding : user.getBindings()){

                Map<String, OnboardedProductInfo> map = binding.getProducts().stream()
                        .collect(Collectors.toMap(OnboardedProduct::getProductId, onboardedProduct -> constructOnboardedProductsInfo(onboardedProduct, institution.getId()), (x, y) -> y));
                 relationshipInfoList.add(new RelationshipInfo(institution, user.getId(), map));
            }
        }
        return relationshipInfoList;
    }

    private OnboardedProductInfo constructOnboardedProductsInfo(OnboardedProduct onboardedProduct, String id) {
        String tokenId = tokenService.retrieveToken(id, onboardedProduct.getProductId()).getId();
        return new OnboardedProductInfo(tokenId, onboardedProduct);
    }
}
