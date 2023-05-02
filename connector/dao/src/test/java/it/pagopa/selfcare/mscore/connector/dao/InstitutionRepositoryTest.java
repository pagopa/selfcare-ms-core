package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.config.DaoConfigTest;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.AttributesEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardingEntity;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@EnableAutoConfiguration
@ContextConfiguration(classes = {InstitutionEntity.class, OnboardingEntity.class, InstitutionRepository.class, DaoConfigTest.class})
class InstitutionRepositoryTest {

    @Autowired
    private InstitutionRepository repository;


    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    void create() {
        // Given
        InstitutionEntity institutionEntity = mockInstance(new InstitutionEntity());
        institutionEntity.setOrigin(Origin.IPA);
        institutionEntity.setInstitutionType(InstitutionType.PA);
        institutionEntity.setCreatedAt(OffsetDateTime.parse("2020-11-01T02:15:30Z"));
        institutionEntity.setUpdatedAt(OffsetDateTime.parse("2021-12-12T02:15:30Z"));
        OnboardingEntity onboardingEntity1 = mockInstance(new OnboardingEntity(), 1);
        onboardingEntity1.setProductId("prod-io");
        onboardingEntity1.setStatus(RelationshipState.ACTIVE);
        onboardingEntity1.setCreatedAt(OffsetDateTime.parse("2020-11-01T02:15:30Z"));
        onboardingEntity1.setUpdatedAt(null);
        onboardingEntity1.setClosedAt(null);
        OnboardingEntity onboardingEntity2 = mockInstance(new OnboardingEntity(), 2);
        onboardingEntity1.setProductId("prod-pagopa");
        onboardingEntity2.setStatus(RelationshipState.ACTIVE);
        onboardingEntity2.setCreatedAt(OffsetDateTime.parse("2021-12-12T02:15:30Z"));
        onboardingEntity2.setUpdatedAt(null);
        onboardingEntity2.setClosedAt(null);
        institutionEntity.setOnboarding(List.of(onboardingEntity1, onboardingEntity2));
        institutionEntity.setGeographicTaxonomies(List.of(mockInstance(new GeoTaxonomyEntity())));
        institutionEntity.setAttributes(List.of(mockInstance(new AttributesEntity())));
        // When
        InstitutionEntity savedInstitutionEntity = repository.save(institutionEntity);
        // Then
        assertFalse(repository.findAll().isEmpty());
        assertNotNull(savedInstitutionEntity);
        reflectionEqualsByName(institutionEntity, savedInstitutionEntity);
    }

}
