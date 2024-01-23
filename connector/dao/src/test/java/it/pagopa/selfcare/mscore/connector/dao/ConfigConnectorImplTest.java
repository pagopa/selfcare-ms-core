package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.utils.DaoMockUtils;
import it.pagopa.selfcare.mscore.model.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

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
    void findAndUpdate() {
        // Given
        String configId = "KafkaScheduler";
        ConfigEntity configEntityMock = DaoMockUtils.createConfigEntityMock();

        when(configRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(configEntityMock);
        // When
        Config result = configConnector.findAndUpdate(configId);
        // Then
        assertNotNull(result);
        verify(configRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(ConfigEntity.class));
        List<Query> capturedQuery = queryArgumentCaptor.getAllValues();
        assertEquals(1, capturedQuery.size());
        assertEquals(configId, capturedQuery.get(0).getQueryObject().get(ConfigEntity.Fields.id.name()));
        assertEquals(true, capturedQuery.get(0).getQueryObject().get(ConfigEntity.Fields.enableKafkaScheduler.name()));
        assertEquals(1, updateArgumentCaptor.getAllValues().size());
        Update update = updateArgumentCaptor.getAllValues().get(0);
        assertTrue(update.getUpdateObject().get("$set").toString().contains(ConfigEntity.Fields.productFilter.name()) &&
                update.getUpdateObject().get("$set").toString().contains(ConfigEntity.Fields.enableKafkaScheduler.name()));
        verifyNoMoreInteractions(configRepository);
    }

}
