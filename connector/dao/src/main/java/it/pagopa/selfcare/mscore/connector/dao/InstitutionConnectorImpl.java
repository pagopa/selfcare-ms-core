package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.model.institutions.Institution;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstitutionConnectorImpl implements InstitutionConnector {

    private final InstitutionRepository repository;

    @Autowired
    public InstitutionConnectorImpl(InstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = new InstitutionEntity(institution);
        return convertToInstitution(repository.save(entity));
    }

    @Override
    public List<Institution> findAll() {
        return repository.findAll().stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findAll(Institution institution) {
        Example<InstitutionEntity> example = Example.of(convertToInstitutionEntity(institution), UntypedExampleMatcher.matching());
        //List<InstitutionEntity> tmp = repository.findByExternalId(institution.getExternalId());
        return repository.findAll(example).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(new ObjectId(id));
    }

    @Override
    public Optional<Institution> findById(String id) {
        return repository.findById(new ObjectId(id)).map(this::convertToInstitution);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    private Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        institution.setId(entity.getId().toString());
        institution.setExternalId(entity.getExternalId());
        return institution;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(new ObjectId(institution.getId()));
        }
        entity.setExternalId(institution.getExternalId());
        return entity;
    }
}
