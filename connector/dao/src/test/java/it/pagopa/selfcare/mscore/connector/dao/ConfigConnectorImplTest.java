package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.utils.DaoMockUtils;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.CustomError.CONFIG_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ConfigConnectorImpl.class})
@ExtendWith(MockitoExtension.class)
class ConfigConnectorImplTest {

    @InjectMocks
    ConfigConnectorImpl configConnector;
    @Mock
    ConfigRepository configRepository;

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    @Test
    void findById() {
        // Given
        String configId = "KafkaScheduler";
        Optional<ConfigEntity> configEntityMock = Optional.of(DaoMockUtils.createConfig());

        when(configRepository.findById(any()))
                .thenReturn(configEntityMock);
        // When
        Config result = configConnector.findById(configId);
        // Then
        assertEquals(configEntityMock.get().getId(), result.getId());
        assertEquals(configEntityMock.get().getProductFilter(), result.getProductFilter());
        assertEquals(configEntityMock.get().isEnableKafkaScheduler(), result.isEnableKafkaScheduler());
        verify(configRepository, times(1))
                .findById(configId);
        verifyNoMoreInteractions(configRepository);
    }

    @Test
    void findById_notFound() {
        // Given
        String configId = "KafkaScheduler";
        when(configRepository.findById(any()))
                .thenReturn(Optional.empty());
        // When
        Executable executable = () -> configConnector.findById(configId);
        // Then
        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(CONFIG_NOT_FOUND.getCode(), resourceNotFoundException.getCode());
        assertEquals(String.format(CONFIG_NOT_FOUND.getMessage(), configId), resourceNotFoundException.getMessage());
        verify(configRepository, times(1))
                .findById(configId);
        verifyNoMoreInteractions(configRepository);
    }

    @Test
    void resetConfiguration() {
        // Given
        String configId = "KafkaScheduler";
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId("KafkaScheduler");
        configEntity.setProductFilter("");
        configEntity.setEnableKafkaScheduler(true);

        when(configRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(configEntity);
        // When
        configConnector.resetConfiguration(configId);
        // Then
        verify(configRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(ConfigEntity.class));
        List<Query> capturedQuery = queryArgumentCaptor.getAllValues();
        assertEquals(1, capturedQuery.size());
        assertSame(capturedQuery.get(0).getQueryObject().get(ConfigEntity.Fields.id.name()), configId);
        assertEquals(1, updateArgumentCaptor.getAllValues().size());
        Update update = updateArgumentCaptor.getAllValues().get(0);
        assertTrue(update.getUpdateObject().get("$set").toString().contains(ConfigEntity.Fields.productFilter.name()) &&
                update.getUpdateObject().get("$set").toString().contains(ConfigEntity.Fields.enableKafkaScheduler.name()));
        verifyNoMoreInteractions(configRepository);
    }

}
