package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionConnector institutionConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;

    public ExternalServiceImpl(InstitutionConnector institutionConnector, GeoTaxonomiesConnector geoTaxonomiesConnector) {
        this.institutionConnector = institutionConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
    }

    @Override
    public Institution createInstitution(Institution institution) {
        return institutionConnector.save(institution);
    }

    @Override
    public List<Institution> getAllInstitution() {
        return institutionConnector.findAll();
    }

    @Override
    public Institution getInstitutionById(String id) {
        return institutionConnector.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("institution not found"));
    }

    @Override
    public void deleteInstitution(String id) {
        institutionConnector.deleteById(id);
    }

    @Override
    public List<GeographicTaxonomies> getGeoTaxonomies(String externalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        List<GeographicTaxonomies> response = new ArrayList<>();
        if (opt.isPresent()) {
            List<GeographicTaxonomies> geographicTaxonomiesList = opt.get().getGeographicTaxonomies();
            for (GeographicTaxonomies g : geographicTaxonomiesList) {
                response.add(geoTaxonomiesConnector.getExtByCode(g.getCode()));
            }
        }
        return response;
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);

        List<Institution> list = institutionConnector.findAll(institution);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException("Institution not found");
        }
        // TODO cosa fare se sono più di uno?
        return list.get(0);
    }
}
