package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.SearchMode;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.ValidInstitution;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapper.addGeographicTaxonomies;
import static it.pagopa.selfcare.mscore.constant.CustomError.GET_INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomError.INSTITUTION_NOT_FOUND;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String CURRENT_ONBOARDING = "current.";
    private static final String CURRENT_ONBOARDING_REFER = "$[current]";

    private final InstitutionRepository repository;

    public InstitutionConnectorImpl(InstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Institution> findAll(){
        return repository.findAll().stream().map(InstitutionMapper::convertToInstitution).collect(Collectors.toList());
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = InstitutionMapper.convertToInstitutionEntity(institution);
        return InstitutionMapper.convertToInstitution(repository.save(entity));
    }

    @Override
    public Institution saveOrRetrievePnPg(Institution institution) {
        final InstitutionEntity entity = InstitutionMapper.convertToInstitutionEntity(institution);
        return findByExternalId(institution.getExternalId())
                .orElse(InstitutionMapper.convertToInstitution(repository.save(entity)));
    }

    @Override
    public List<String> findByExternalIdAndProductId(List<ValidInstitution> validInstitutionList, String productId) {
        List<String> externalIds = validInstitutionList.stream().map(ValidInstitution::getId).collect(Collectors.toList());
        Query query = Query.query(Criteria.where(constructQuery(Onboarding.Fields.productId.name())).is(productId)
                .and(InstitutionEntity.Fields.externalId.name()).in(externalIds));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionEntity::getExternalId)
                .collect(Collectors.toList());
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
                    return InstitutionMapper.convertToInstitution(institution);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), id, "UNDEFINED"), INSTITUTION_NOT_FOUND.getCode()));
    }

    @Override
    public Institution findAndUpdateStatus(String institutionId, String tokenId, RelationshipState status) {
        OffsetDateTime now = OffsetDateTime.now();

        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update()
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), status)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), now)
                .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.tokenId.name()).is(tokenId));
        if (status == RelationshipState.DELETED) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.closedAt.name()), now);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return InstitutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public Institution findAndUpdate(String institutionId, Onboarding onboarding, List<InstitutionGeographicTaxonomies> geographicTaxonomiesList) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        addGeographicTaxonomies(geographicTaxonomiesList, update);

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return InstitutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    //TODO refactor: new onboarding (onboarding != null) and updates on onboarding (state and token != null) cause conflicts
    @Override
    public Institution findAndUpdateInstitutionData(String institutionId, Token token, Onboarding onboarding, RelationshipState state) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        if (state != null) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), state)
                    .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), OffsetDateTime.now())
                    .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.tokenId.name()).is(token.getId()));
            if (state == RelationshipState.DELETED) {
                update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.closedAt.name()), OffsetDateTime.now());
            }
        }
        if (token.getContractSigned() != null) {
            update.set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.contract.name()), token.getContractSigned());
        }
        InstitutionUpdate institutionUpdate = token.getInstitutionUpdate();
        if (institutionUpdate != null) {
            Map<String, Object> map = InstitutionMapper.getNotNullField(institutionUpdate);
            map.forEach(update::set);
            addGeographicTaxonomies(institutionUpdate.getGeographicTaxonomies(), update);
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return InstitutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }



    @Override
    public Institution findAndUpdateInstitutionDataWithNewOnboarding(String institutionId, InstitutionUpdate institutionUpdate, Onboarding onboarding) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);

        if (institutionUpdate != null) {
            Map<String, Object> map = InstitutionMapper.getNotNullField(institutionUpdate);
            map.forEach(update::set);
            addGeographicTaxonomies(institutionUpdate.getGeographicTaxonomies(), update);
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return InstitutionMapper.convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public List<Institution> findByGeotaxonomies(List<String> geo, SearchMode searchMode) {
        Query query = constructQueryWithSearchMode(geo, searchMode);
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findByProductId(String productId) {
        Query query = Query.query(Criteria.where(constructQuery(Onboarding.Fields.productId.name())).is(productId));
        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findAllByIds(List<String> ids) {
        List<Institution> list = new ArrayList<>();
        repository.findAllById(ids)
                .forEach(entity -> list.add(InstitutionMapper.convertToInstitution(entity)));
        return list;
    }

    @Override
    public Institution findInstitutionProduct(String externalId, String productId) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)
                .and(constructQuery(Onboarding.Fields.productId.name())).is(productId));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_ERROR.getMessage(), externalId, productId),
                        GET_INSTITUTION_BILLING_ERROR.getCode()));
    }

    @Override
    public void findAndRemoveOnboarding(String institutionId, Onboarding onboarding) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.pull(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)),
                        InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId))
                .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                        .elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                                .and(Onboarding.Fields.status.name()).in(validRelationshipStates)));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(InstitutionMapper::convertToInstitution)
                .collect(Collectors.toList());
    }


    private Query constructQueryWithSearchMode(List<String> geo, SearchMode searchMode) {
        String geoQuery = InstitutionEntity.Fields.geographicTaxonomies.name()
                + "." + GeoTaxonomyEntity.Fields.code.name();
        switch (searchMode){
            case ALL:
                return Query.query(Criteria.where(geoQuery).all(geo));
            case ANY:
                return Query.query(Criteria.where(geoQuery).in(geo));
            case EXACT:
                return Query.query(Criteria.where(geoQuery).all(geo)
                        .and(InstitutionEntity.Fields.geographicTaxonomies.name()).size(geo.size()));
            default:
                throw new InvalidRequestException("Invalid search mode","0000");
        }
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(InstitutionEntity.Fields.onboarding.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }
}
