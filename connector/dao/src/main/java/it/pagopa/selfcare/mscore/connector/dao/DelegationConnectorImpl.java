package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationInstitution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelegationConnectorImpl implements DelegationConnector {

    private final DelegationRepository repository;
    private final DelegationEntityMapper delegationMapper;
    private final DelegationInstitutionMapper delegationInstitutionMapper;
    private final MongoTemplate mongoTemplate;

    public DelegationConnectorImpl(DelegationRepository repository,
                                   DelegationEntityMapper delegationMapper,
                                   MongoTemplate mongoTemplate,
                                   DelegationInstitutionMapper delegationInstitutionMapper) {
        this.repository = repository;
        this.delegationMapper = delegationMapper;
        this.mongoTemplate = mongoTemplate;
        this.delegationInstitutionMapper = delegationInstitutionMapper;
    }

    @Override
    public Delegation save(Delegation delegation) {
        final DelegationEntity entity = delegationMapper.convertToDelegationEntity(delegation);
        return delegationMapper.convertToDelegation(repository.save(entity));
    }

    @Override
    public boolean checkIfExists(Delegation delegation) {
        Optional<DelegationEntity> opt = repository.findByFromAndToAndProductIdAndType(
                delegation.getFrom(),
                delegation.getTo(),
                delegation.getProductId(),
                delegation.getType()

        );
        return opt.isPresent();
    }

    @Override
    public List<Delegation> find(String from, String to, String productId, GetDelegationsMode mode) {

        Criteria criteria = new Criteria();

        List<Criteria> criterias = new ArrayList<>();
        if(Objects.nonNull(from))
            criterias.add(Criteria.where(DelegationEntity.Fields.from.name()).is(from));
        if(Objects.nonNull(to))
            criterias.add(Criteria.where(DelegationEntity.Fields.to.name()).is(to));
        if(Objects.nonNull(productId))
            criterias.add(Criteria.where(DelegationEntity.Fields.productId.name()).is(productId));

        if(GetDelegationsMode.FULL.equals(mode)) {

            GraphLookupOperation.GraphLookupOperationBuilder lookup = Aggregation.graphLookup("Institution")
                    .startWith(Objects.nonNull(from) ? "to" : "from")
                    .connectFrom(Objects.nonNull(from) ? "to" : "from")
                    .connectTo("_id");

            MatchOperation matchOperation = new MatchOperation(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));
            Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookup.as("institutions"));

            List<DelegationInstitution> result = mongoTemplate.aggregate(aggregation, "Delegations", DelegationInstitution.class).getMappedResults();
            return result.stream()
                    .map(Objects.nonNull(from) ?
                            delegationInstitutionMapper::convertToDelegationBroker :
                            delegationInstitutionMapper::convertToDelegationInstitution)
                    .collect(Collectors.toList());
        }

        return repository.find(Query.query(criteria.andOperator(criterias)), DelegationEntity.class).stream()
                .map(delegationMapper::convertToDelegation)
                .collect(Collectors.toList());
    }

    @Override
    public Delegation findByIdAndModifyStatus(String delegationId, DelegationState status) {
        Query query = Query.query(Criteria.where(DelegationEntity.Fields.id.name()).is(delegationId));
        Update update = new Update();
        update.set(DelegationEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        update.set(DelegationEntity.Fields.status.name(), status);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false);
        return delegationMapper.convertToDelegation(repository.findAndModify(query, update, findAndModifyOptions, DelegationEntity.class));
    }

    @Override
    public boolean checkIfDelegationsAreActive(String institutionId) {
        Optional<DelegationEntity> opt = repository.findByToAndStatus(institutionId, DelegationState.ACTIVE);
        return opt.isPresent();
    }
}
