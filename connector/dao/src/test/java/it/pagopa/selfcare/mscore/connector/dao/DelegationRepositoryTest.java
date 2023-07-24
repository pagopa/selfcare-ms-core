package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.config.DaoConfigTest;
import it.pagopa.selfcare.mscore.connector.dao.model.DelegationEntity;
import it.pagopa.selfcare.mscore.constant.DelegationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {DelegationEntity.class, DaoConfigTest.class})
class DelegationRepositoryTest {

    @Autowired
    private DelegationRepository repository;

    @Test
    void create() {
        // Given
        DelegationEntity delegationEntity = mockInstance(new DelegationEntity());
        delegationEntity.setFrom("from");
        delegationEntity.setTo("to");
        delegationEntity.setType(DelegationType.PT);
        delegationEntity.setProductId("productId");
        // When
        DelegationEntity savedDelegationEntity = repository.save(delegationEntity);
        // Then
        assertFalse(repository.findAll().isEmpty());
        assertNotNull(savedDelegationEntity);
        reflectionEqualsByName(delegationEntity, savedDelegationEntity);
    }

}
