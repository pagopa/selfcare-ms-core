package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.mockUtils.MockUtils;
import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.model.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMapperTest {

    @Test
    void convertToConfig() {
        // Given
        ConfigEntity entity = MockUtils.createConfig();
        // When
        Config result = ConfigMapper.convertToConfig(entity);
        // Then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getProductFilter(), result.getProductFilter());
        assertEquals(entity.isEnableKafkaScheduler(), result.isEnableKafkaScheduler());
    }

    @Test
    void convertToConfig_nullEntity() {
        // Given
        // When
        Config result = ConfigMapper.convertToConfig(null);
        // Then
        assertNull(result);
    }

}
