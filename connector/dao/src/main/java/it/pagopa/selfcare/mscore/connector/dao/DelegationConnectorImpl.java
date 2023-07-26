package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DelegationConnectorImpl implements DelegationConnector {

    private final DelegationRepository repository;
    private final DelegationEntityMapper delegationMapper;

    public DelegationConnectorImpl(DelegationRepository repository,
                                   DelegationEntityMapper delegationMapper) {
        this.repository = repository;
        this.delegationMapper = delegationMapper;
    }

    @Override
    public Delegation save(Delegation delegation) {
        final DelegationEntity entity = delegationMapper.convertToDelegationEntity(delegation);
        return delegationMapper.convertToDelegation(repository.save(entity));
    }

    @Override
    public List<Delegation> find(String from, String to, String productId) {
        Criteria criteria = new Criteria();

        List<Criteria> criterias = new ArrayList<>();
        if(Objects.nonNull(from))
            criterias.add(Criteria.where(DelegationEntity.Fields.from.name()).is(from));
        if(Objects.nonNull(to))
            criterias.add(Criteria.where(DelegationEntity.Fields.to.name()).is(to));
        if(Objects.nonNull(productId))
            criterias.add(Criteria.where(DelegationEntity.Fields.productId.name()).is(productId));

        return repository.find(Query.query(criteria.andOperator(criterias)), DelegationEntity.class).stream()
                .map(delegationMapper::convertToDelegation)
                .collect(Collectors.toList());
    }
}
