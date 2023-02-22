package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.GET_INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.INSTITUTION_NOT_FOUND;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String CURRENT_ONBOARDING = "current.";
    private static final String CURRENT_ONBOARDING_REFER = "$[current]";
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
        repository.deleteById(id);
    }

    @Override
    public Institution findById(String id) {
        return repository.findById(id)
                .map(institution -> {
                    log.info("Founded institution {}", institution.getExternalId());
                    return convertToInstitution(institution);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), id, null), INSTITUTION_NOT_FOUND.getCode()));
    }

    @Override
    public void findAndUpdateStatus(String institutionId, String productId, RelationshipState status) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        UpdateDefinition updateDefinition = new Update()
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), status)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), OffsetDateTime.now())
                .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.productId.name()).is(productId));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, updateDefinition, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public Institution findAndUpdate(String institutionId, Onboarding onboarding, List<GeographicTaxonomies> geographicTaxonomiesList) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        for (GeographicTaxonomies geo : geographicTaxonomiesList) {
            update.addToSet(InstitutionEntity.Fields.geographicTaxonomies.name(), geo);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public Institution findInstitutionProduct(String externalId, String productId) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)
                .and(constructQuery(Onboarding.Fields.productId.name())).is(productId));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_ERROR.getMessage(), externalId, productId),
                        GET_INSTITUTION_BILLING_ERROR.getCode()));
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)),
                        InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId))
                .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                        .elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                                .and(Onboarding.Fields.status.name()).in(validRelationshipStates)));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(InstitutionEntity.Fields.onboarding.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        if (entity != null) {
            institution.setId(entity.getId());
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
        }
        return institution;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(institution.getId());
        } else {
            entity.setId(UUID.randomUUID().toString());
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
