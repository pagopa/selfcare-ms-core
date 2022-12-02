package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.model.institutions.Institution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstitutionConnectorImpl implements InstitutionConnector {

    private final InstitutionRepository repository;

    @Autowired
    public InstitutionConnectorImpl(InstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Institution> findAll(Institution institution) {
        Example<InstitutionEntity> example2 = Example.of(convertToInstitutionEntity(institution));
        List<Institution> response = new ArrayList<>();
        repository.findAll(example2).forEach(institutionEntity -> response.add(convertToInstitution(institutionEntity)));
        return response;
    }

    private Institution convertToInstitution(InstitutionEntity institutionEntity) {
        Institution institution = new Institution();
        institution.setId(institutionEntity.getId());
        return institution;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setExternalId(institution.getExternalId());
        return institutionEntity;
    }
}
