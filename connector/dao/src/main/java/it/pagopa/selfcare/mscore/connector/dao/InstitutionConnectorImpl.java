package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {
    private final InstitutionRepository repository;

    @Autowired
    public InstitutionConnectorImpl(InstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = convertToInstitutionEntity(institution);
        return convertToInstitution(repository.save(entity));
    }

    @Override
    public List<Institution> findAll(Institution institution) {
        Example<InstitutionEntity> example = Example.of(convertToInstitutionEntity(institution), UntypedExampleMatcher.matching());
        return repository.findAll(example).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);
        institution.setOnboarding(null);
        List<Institution> result = findAll(institution);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    private Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        institution.setId(entity.getId().toString());
        institution.setExternalId(entity.getExternalId());
        institution.setDescription(entity.getDescription());
        institution.setInstitutionType(entity.getInstitutionType());
        institution.setIpaCode(entity.getIpaCode());
        institution.setDigitalAddress(entity.getDigitalAddress());
        institution.setAddress(entity.getAddress());
        institution.setZipCode(entity.getZipCode());
        institution.setTaxCode(entity.getTaxCode());
        institution.setOnboarding(entity.getOnboarding());
        institution.setDataProtectionOfficer(entity.getDataProtectionOfficer());
        institution.setPaymentServiceProvider(entity.getPaymentServiceProvider());
        institution.setAttributes(entity.getAttributes());
        institution.setGeographicTaxonomies(entity.getGeographicTaxonomies());
        institution.setCreatedAt(entity.getCreatedAt());
        institution.setUpdatedAt(entity.getUpdatedAt());
        return institution;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(new ObjectId(institution.getId()));
        }
        entity.setCreatedAt(institution.getCreatedAt());
        entity.setExternalId(institution.getExternalId());
        entity.setDescription(institution.getDescription());
        entity.setIpaCode(institution.getIpaCode());
        entity.setInstitutionType(institution.getInstitutionType());
        entity.setDigitalAddress(institution.getDigitalAddress());
        entity.setAddress(institution.getAddress());
        entity.setZipCode(institution.getZipCode());
        entity.setTaxCode(institution.getTaxCode());
        entity.setOnboarding(institution.getOnboarding());
        entity.setUpdatedAt(institution.getUpdatedAt());
        if(institution.getGeographicTaxonomies() != null) {
            entity.setGeographicTaxonomies(institution.getGeographicTaxonomies());
        }
        if(institution.getDataProtectionOfficer() != null) {
            entity.setDataProtectionOfficer(institution.getDataProtectionOfficer());
        }
        if(institution.getPaymentServiceProvider() != null) {
            entity.setPaymentServiceProvider(institution.getPaymentServiceProvider());
        }

        return entity;
    }

    public Institution findById(String id) {
        Optional<InstitutionEntity> entity = this.repository.findById(new ObjectId(id));
        return this.convertToInstitution(entity.get());
    }
}
