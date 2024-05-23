package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationInstitutionMapper;
import it.pagopa.selfcare.mscore.constant.DelegationState;
import it.pagopa.selfcare.mscore.constant.Order;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import it.pagopa.selfcare.mscore.model.delegation.DelegationWithPagination;
import it.pagopa.selfcare.mscore.model.delegation.GetDelegationParameters;
import it.pagopa.selfcare.mscore.model.delegation.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Pattern;

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
    public boolean checkIfExistsWithStatus(Delegation delegation, DelegationState status) {
        Optional<DelegationEntity> opt = repository.findByFromAndToAndProductIdAndTypeAndStatus(
                delegation.getFrom(),
                delegation.getTo(),
                delegation.getProductId(),
                delegation.getType(),
                status
        );
        return opt.isPresent();
    }

    private List<Criteria> getCriterias(String from, String to, String productId, String search, String taxCode) {
        List<Criteria> criterias = new ArrayList<>();

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
        if (Objects.nonNull(taxCode)) {
            criterias.add(Criteria.where(DelegationEntity.Fields.fromTaxCode.name()).is(taxCode));
        }
        return criterias;
    }

    @Override
    public List<Delegation> find(String from, String to, String productId, String search, String taxCode, Order order, Integer page, Integer size) {
        Criteria criteria = new Criteria();
        Pageable pageable = PageRequest.of(page, size);
        List<Criteria> criterias = getCriterias(from, to, productId, search, taxCode);

        Sort.Direction sortDirection = order.equals(Order.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Query query = Query.query(criteria.andOperator(criterias));

        if (!order.equals(Order.NONE)) {
            query = query.with(Sort.by(sortDirection, DelegationEntity.Fields.institutionFromName.name()));
        }

        return repository.find(query, pageable, DelegationEntity.class)
                .stream()
                .map(delegationMapper::convertToDelegation)
                .toList();
    }

    @Override
    public DelegationWithPagination findAndCount(GetDelegationParameters delegationParameters) {

        List<Delegation> delegations = find(delegationParameters.getFrom(), delegationParameters.getTo(), delegationParameters.getProductId(),
                delegationParameters.getSearch(), delegationParameters.getTaxCode(), delegationParameters.getOrder(),
                delegationParameters.getPage(), delegationParameters.getSize());

        Query query = Query.query(new Criteria().andOperator(getCriterias(delegationParameters.getFrom(), delegationParameters.getTo(),
                delegationParameters.getProductId(), delegationParameters.getSearch(), delegationParameters.getTaxCode())));

        long count = mongoTemplate.count(query, DelegationEntity.class);

        Pageable pageable = PageRequest.of(delegationParameters.getPage(), delegationParameters.getSize());
        Page<Delegation> result = PageableExecutionUtils.getPage(delegations, pageable, () -> count);

        PageInfo pageInfo = new PageInfo(result.getSize(), result.getNumber(), result.getTotalElements(), result.getTotalPages());
        return new DelegationWithPagination(delegations, pageInfo);
    }

    private static GraphLookupOperation.GraphLookupOperationBuilder createInstitutionGraphLookupOperationBuilder(String from) {
        return Aggregation.graphLookup(INSTITUTION)
                .startWith(Objects.nonNull(from) ? "to" : "from")
                .connectFrom(Objects.nonNull(from) ? "to" : "from")
                .connectTo("_id").maxDepth(1);
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

    @Override
    public Delegation findAndActivate(String from, String to, String productId) {
        Query query = Query.query(Criteria.where(DelegationEntity.Fields.from.name()).is(from).and(DelegationEntity.Fields.to.name()).is(to).and(DelegationEntity.Fields.productId.name()).is(productId));
        Update update = new Update();
        update.set(DelegationEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        update.set(DelegationEntity.Fields.status.name(), DelegationState.ACTIVE);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return delegationMapper.convertToDelegation(repository.findAndModify(query, update, findAndModifyOptions, DelegationEntity.class));
    }
}
