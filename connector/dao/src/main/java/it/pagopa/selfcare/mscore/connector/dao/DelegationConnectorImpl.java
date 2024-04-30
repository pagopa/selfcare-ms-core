package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.GetDelegationsMode;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationInstitution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelegationConnectorImpl implements DelegationConnector {

    public static final String INSTITUTION = "Institution";
    public static final String INSTITUTIONS = "institutions";
    public static final String DELEGATIONS = "Delegations";
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
    public List<Delegation> find(String from, String to, String productId, String search, String taxCode, GetDelegationsMode mode, Order order, Integer page, Integer size) {
        List<Criteria> criterias = new ArrayList<>();
        Criteria criteria = new Criteria();
        Pageable pageable = PageRequest.of(page, size);

        criterias.add(Criteria.where(DelegationEntity.Fields.status.name()).is(DelegationState.ACTIVE.name()));

        if (Objects.nonNull(from)) {
            criterias.add(Criteria.where(DelegationEntity.Fields.from.name()).is(from));
        }
        if (Objects.nonNull(to)) {
            criterias.add(Criteria.where(DelegationEntity.Fields.to.name()).is(to));
        }
        if (Objects.nonNull(productId)) {
            criterias.add(Criteria.where(DelegationEntity.Fields.productId.name()).is(productId));
        }
        if (Objects.nonNull(search)) {
            criterias.add(Criteria.where(DelegationEntity.Fields.institutionFromName.name()).regex("(?i)" + Pattern.quote(search)));
        }

        Sort.Direction sortDirection = order.equals(Order.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;

        if (GetDelegationsMode.FULL.equals(mode)) {

            GraphLookupOperation lookup = createInstitutionGraphLookupOperationBuilder(from).as(INSTITUTIONS);

            MatchOperation matchOperation = new MatchOperation(new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()])));

            long skipLimit = (long) page * size;
            SkipOperation skip = Aggregation.skip(skipLimit);
            LimitOperation limit = Aggregation.limit(size);
            Aggregation aggregation;

            if (Objects.nonNull(taxCode)) {
                MatchOperation matchTaxCodeOperation = getMatchTaxCodeOperation(taxCode);
                aggregation = Aggregation.newAggregation(matchOperation, lookup, matchTaxCodeOperation, skip, limit);
            }
            else {
                if (!order.equals(Order.NONE)) {
                    SortOperation sortOperation = new SortOperation(Sort.by(sortDirection, DelegationEntity.Fields.institutionFromName.name()));
                    aggregation = Aggregation.newAggregation(matchOperation, sortOperation, lookup, skip, limit);
                }
                else {
                    aggregation = Aggregation.newAggregation(matchOperation, lookup, skip, limit);
                }
            }

            List<DelegationInstitution> result = mongoTemplate.aggregate(aggregation, DELEGATIONS, DelegationInstitution.class).getMappedResults();
            return result.stream()
                    .map(Objects.nonNull(from) ?
                            delegationInstitutionMapper::convertToDelegationBroker :
                            delegationInstitutionMapper::convertToDelegationInstitution)
                    .collect(Collectors.toList());
        }

        Query query = Query.query(criteria.andOperator(criterias));
        if (!order.equals(Order.NONE)) {
            query = query.with(Sort.by(sortDirection, DelegationEntity.Fields.institutionFromName.name()));
        }

        return repository.find(query, pageable, DelegationEntity.class)
                .stream()
                .map(delegationMapper::convertToDelegation)
                .collect(Collectors.toList());
    }

    private static GraphLookupOperation.GraphLookupOperationBuilder createInstitutionGraphLookupOperationBuilder(String from) {
        return Aggregation.graphLookup(INSTITUTION)
                .startWith(Objects.nonNull(from) ? "to" : "from")
                .connectFrom(Objects.nonNull(from) ? "to" : "from")
                .connectTo("_id");
    }

    private static MatchOperation getMatchTaxCodeOperation(String taxCode) {
        Criteria taxCodeCriteria = Criteria.where("institutions." + InstitutionEntity.Fields.taxCode.name()).is(taxCode);
        return new MatchOperation(new Criteria().andOperator(taxCodeCriteria));
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
        List<DelegationEntity> opt = repository.findByToAndStatus(institutionId, DelegationState.ACTIVE).orElse(Collections.emptyList());
        return !opt.isEmpty();
    }
}
