package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomiesByExternalId(String externalId) {
        log.info("Retrieving geographic taxonomies for institution having externalId {}", externalId);
        Institution institution = institutionService.retrieveInstitutionByExternalId(externalId);
        return institution.getGeographicTaxonomies().stream()
                .map(GeographicTaxonomies::getCode)
                .map(geoTaxonomiesConnector::getExtByCode)
                .collect(Collectors.toList());
    }
}
