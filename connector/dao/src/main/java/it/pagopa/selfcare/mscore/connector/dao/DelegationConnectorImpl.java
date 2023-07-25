package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.DelegationConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.DelegationEntityMapper;
import it.pagopa.selfcare.mscore.model.delegation.Delegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public List<Delegation> find(String from) {
        return repository.findByFromAndProductId(from, null).stream()
                .map(delegationMapper::convertToDelegation)
                .collect(Collectors.toList());
    }
}
