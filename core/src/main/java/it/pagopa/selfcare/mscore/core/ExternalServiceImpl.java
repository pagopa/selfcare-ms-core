package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionConnector institutionConnector;

    public ExternalServiceImpl(InstitutionConnector institutionConnector) {
        this.institutionConnector = institutionConnector;
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
