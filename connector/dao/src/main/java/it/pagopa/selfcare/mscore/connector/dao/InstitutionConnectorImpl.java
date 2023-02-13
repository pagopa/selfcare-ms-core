package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String ONBOARDING = "onboarding";
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
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        log.info("START - find institution by ExternalId");
        return repository.find(Query.query(Criteria.where("externalId").is(externalId)),
                        InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("externalId").is(externalId),
                        Criteria.where(ONBOARDING).exists(true),
                        Criteria.where(constructQuery("productId")).is(productId)
                                .and(constructQuery("status")).in(validRelationshipStates)
                )
        );
        return repository.find(query, InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Institution> findById(String id) {
        return repository.findById(new ObjectId(id))
                .map(this::convertToInstitution);
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(ONBOARDING);
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
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

        institution.setRea(entity.getRea());
        institution.setShareCapital(entity.getShareCapital());
        institution.setBusinessRegisterPlace(entity.getBusinessRegisterPlace());
        institution.setSupportEmail(entity.getSupportEmail());
        institution.setSupportPhone(entity.getSupportPhone());
        institution.setImported(entity.isImported());
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

        entity.setRea(institution.getRea());
        entity.setShareCapital(institution.getShareCapital());
        entity.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        entity.setSupportEmail(institution.getSupportEmail());
        entity.setSupportPhone(institution.getSupportPhone());
        entity.setImported(institution.isImported());

        if (institution.getGeographicTaxonomies() != null) {
            entity.setGeographicTaxonomies(institution.getGeographicTaxonomies());
        }
        if (institution.getDataProtectionOfficer() != null) {
            entity.setDataProtectionOfficer(institution.getDataProtectionOfficer());
        }
        if (institution.getPaymentServiceProvider() != null) {
            entity.setPaymentServiceProvider(institution.getPaymentServiceProvider());
        }
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }
}
