package it.pagopa.selfcare.mscore.connector.dao;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import it.pagopa.selfcare.mscore.connector.dao.model.PecNotificationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.PecNotificationEntityMapper;
import it.pagopa.selfcare.mscore.model.pecnotification.PecNotification;

@ContextConfiguration(classes = {PecNotificationConnectorImpl.class})
@ExtendWith(MockitoExtension.class)
class PecNotificationConnectorImplTest {

    @Mock
    private PecNotificationRepository repository;

    @Mock
    private PecNotificationEntityMapper pecNotificationMapper;

    @InjectMocks
    private PecNotificationConnectorImpl pecNotificationConnector;

    private String institutionId;
    private String productId;
    private PecNotificationEntity pecNotificationEntity;
    private PecNotification pecNotification;

    @BeforeEach
    void setUp() {
        institutionId = UUID.randomUUID().toString();
        productId = UUID.randomUUID().toString();
        pecNotificationEntity = new PecNotificationEntity();
        pecNotificationEntity.setInstitutionId(institutionId);
        pecNotificationEntity.setProductId(productId);
        pecNotification = new PecNotification();
    }

    @Test
    void findAndDeletePecNotification_success() {
        when(repository.find(any(), eq(PecNotificationEntity.class)))
                .thenReturn(Collections.singletonList(pecNotificationEntity));

        boolean result = pecNotificationConnector.findAndDeletePecNotification(institutionId, productId);

        assertTrue(result);
        verify(repository, times(1)).delete(pecNotificationEntity);
    }

    @Test
    void findAndDeletePecNotification_multipleEntries() {
        when(repository.find(any(), eq(PecNotificationEntity.class)))
                .thenReturn(List.of(pecNotificationEntity, pecNotificationEntity));

        boolean result = pecNotificationConnector.findAndDeletePecNotification(institutionId, productId);

        assertFalse(result);
        verify(repository, never()).delete(any());
    }

    @Test
    void findAndDeletePecNotification_notExist() {
    	List<PecNotificationEntity> pecNotificationList = Collections.emptyList();
        when(repository.find(any(), eq(PecNotificationEntity.class)))
                .thenReturn(pecNotificationList);

        boolean result = pecNotificationConnector.findAndDeletePecNotification(institutionId, productId);

        assertFalse(result);
        verify(repository, never()).delete(any());
    }

    @Test
    void insertPecNotification_success() {
        when(pecNotificationMapper.convertToPecNotificationEntity(pecNotification)).thenReturn(pecNotificationEntity);
        when(repository.existsById(pecNotificationEntity.getId())).thenReturn(false);

        boolean result = pecNotificationConnector.insertPecNotification(pecNotification);

        assertTrue(result);
        verify(repository, times(1)).insert(pecNotificationEntity);
    }

    @Test
    void insertPecNotification_alreadyExists() {
        when(pecNotificationMapper.convertToPecNotificationEntity(pecNotification)).thenReturn(pecNotificationEntity);
        when(repository.existsById(pecNotificationEntity.getId())).thenReturn(true);

        boolean result = pecNotificationConnector.insertPecNotification(pecNotification);

        assertFalse(result);
        verify(repository, never()).insert(pecNotificationEntity);
        
    }
	
}
