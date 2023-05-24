package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.ConfigConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.SchedulerConfig;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Config;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @InjectMocks
    private SchedulerService scheduler;
    @Mock
    private ContractService contractService;
    @Mock
    private TokenConnector tokenConnector;
    @Mock
    private InstitutionConnector institutionConnector;
    @Mock
    private ConfigConnector configConnector;
    @Mock
    private SchedulerConfig schedulerConfig;

    @Test
    void regenerateQueueNotifcations_notEnabled() {
        // Given
        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(false);
        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();

        verifyNoInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void regenerateQueueNotifcations_configNotFound() {
        // Given
        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(true);
        when(schedulerConfig.getRegenerateKafkaQueueConfigName()).thenReturn("SchedulerId");

        when(configConnector.findById(any()))
                .thenThrow(ResourceNotFoundException.class);
        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();
        verify(configConnector, times(1))
                .findById(schedulerConfig.getRegenerateKafkaQueueConfigName());

        verifyNoMoreInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void regenerateQueueNotifcations_configNotEnabled() {
        // Given
        String schedulerId = "SchedulerId";
        Config configMock = MockUtils.createConfigMock(false, "prod-io");

        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(true);
        when(schedulerConfig.getRegenerateKafkaQueueConfigName()).thenReturn(schedulerId);

        when(configConnector.findById(any()))
                .thenReturn(configMock);
        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();
        verify(configConnector, times(1))
                .findById(schedulerId);

        verifyNoMoreInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void regenerateQueueNotifcations_onePage() {
        // Given
        String schedulerId = "SchedulerId";
        Config configMock = MockUtils.createConfigMock(true, "");
        List<Token> tokensMock = MockUtils.createTokenListMock(40, 0, RelationshipState.ACTIVE, InstitutionType.PA);
        tokensMock.addAll(MockUtils.createTokenListMock(7, 40, RelationshipState.DELETED, InstitutionType.PA));
        List<Institution> institutionsMock = MockUtils.createInstitutionListMock(40, 0, RelationshipState.ACTIVE, InstitutionType.GSP);
        institutionsMock.addAll(MockUtils.createInstitutionListMock(7, 40, RelationshipState.DELETED, InstitutionType.GSP));

        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(true);
        when(schedulerConfig.getRegenerateKafkaQueueConfigName()).thenReturn(schedulerId);

        when(configConnector.findById(any()))
                .thenReturn(configMock);
        doNothing()
                .when(configConnector)
                .resetConfiguration(any());
        doReturn(tokensMock)
                .when(tokenConnector)
                .findByStatusAndProductId(any(), any(), any());
        for (int i = 0; i < tokensMock.size(); i++) {
            doReturn(institutionsMock.get(i))
                    .when(institutionConnector)
                    .findById(tokensMock.get(i).getInstitutionId());
        }
        doNothing()
                .when(contractService)
                .sendDataLakeNotification(any(), any(), any());

        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();
        verify(configConnector, times(1))
                .findById(schedulerId);
        verify(configConnector, times(1))
                .resetConfiguration(schedulerId);
        verify(tokenConnector, times(1))
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 0);
        for (int i = 0; i < tokensMock.size(); i++) {
            verify(institutionConnector, times(1))
                    .findById(tokensMock.get(i).getInstitutionId());
            verify(contractService, times(1))
                    .sendDataLakeNotification(institutionsMock.get(i), tokensMock.get(i), QueueEvent.ADD);
            if (!tokensMock.get(i).getStatus().equals(RelationshipState.ACTIVE)) {
                verify(contractService, times(1))
                        .sendDataLakeNotification(institutionsMock.get(i), tokensMock.get(i), QueueEvent.UPDATE);
            }
        }
        verifyNoMoreInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void regenerateQueueNotifcations_moreThanOnePage() {
        // Given
        String schedulerId = "SchedulerId";
        Config configMock = MockUtils.createConfigMock(true, null);
        List<Token> tokensMockPageOne = MockUtils.createTokenListMock(100, 0, RelationshipState.ACTIVE, InstitutionType.PA);
        List<Institution> institutionsMockPageOne = MockUtils.createInstitutionListMock(100, 0, RelationshipState.ACTIVE, InstitutionType.PA);
        List<Token> tokensMockPageTwo = MockUtils.createTokenListMock(21, 100, RelationshipState.ACTIVE, InstitutionType.PA);
        List<Institution> institutionsMockPageTwo = MockUtils.createInstitutionListMock(21, 100, RelationshipState.ACTIVE, InstitutionType.PA);

        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(true);
        when(schedulerConfig.getRegenerateKafkaQueueConfigName()).thenReturn(schedulerId);

        when(configConnector.findById(any()))
                .thenReturn(configMock);
        doNothing()
                .when(configConnector)
                .resetConfiguration(any());
        doReturn(tokensMockPageOne)
                .when(tokenConnector)
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 0);
        doReturn(tokensMockPageTwo)
                .when(tokenConnector)
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 1);
        for (int i = 0; i < tokensMockPageOne.size(); i++) {
            doReturn(institutionsMockPageOne.get(i))
                    .when(institutionConnector)
                    .findById(tokensMockPageOne.get(i).getInstitutionId());
        }
        for (int i = 0; i < tokensMockPageTwo.size(); i++) {
            doReturn(institutionsMockPageTwo.get(i))
                    .when(institutionConnector)
                    .findById(tokensMockPageTwo.get(i).getInstitutionId());
        }
        doNothing()
                .when(contractService)
                .sendDataLakeNotification(any(), any(), any());

        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();
        verify(configConnector, times(1))
                .findById(schedulerId);
        verify(configConnector, times(1))
                .resetConfiguration(schedulerId);
        verify(tokenConnector, times(1))
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 0);
        verify(tokenConnector, times(1))
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 1);
        for (int i = 0; i < tokensMockPageOne.size(); i++) {
            verify(institutionConnector, times(1))
                    .findById(tokensMockPageOne.get(i).getInstitutionId());
            verify(contractService, times(1))
                    .sendDataLakeNotification(institutionsMockPageOne.get(i), tokensMockPageOne.get(i), QueueEvent.ADD);
            if (!tokensMockPageOne.get(i).getStatus().equals(RelationshipState.ACTIVE)) {
                verify(contractService, times(1))
                        .sendDataLakeNotification(institutionsMockPageOne.get(i), tokensMockPageOne.get(i), QueueEvent.UPDATE);
            }
        }
        for (int i = 0; i < tokensMockPageTwo.size(); i++) {
            verify(institutionConnector, times(1))
                    .findById(tokensMockPageTwo.get(i).getInstitutionId());
            verify(contractService, times(1))
                    .sendDataLakeNotification(institutionsMockPageTwo.get(i), tokensMockPageTwo.get(i), QueueEvent.ADD);
            if (!tokensMockPageTwo.get(i).getStatus().equals(RelationshipState.ACTIVE)) {
                verify(contractService, times(1))
                        .sendDataLakeNotification(institutionsMockPageTwo.get(i), tokensMockPageTwo.get(i), QueueEvent.UPDATE);
            }
        }
        verifyNoMoreInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void regenerateQueueNotifcations_institutionNotFound() {
        // Given
        String schedulerId = "SchedulerId";
        Config configMock = MockUtils.createConfigMock(true, "prod-io");
        List<Token> tokensMock = MockUtils.createTokenListMock(1, 0, RelationshipState.ACTIVE, InstitutionType.GSP);

        when(schedulerConfig.getRegnerateKafkaQueueEnabled()).thenReturn(true);
        when(schedulerConfig.getRegenerateKafkaQueueConfigName()).thenReturn("SchedulerId");

        when(configConnector.findById(any()))
                .thenReturn(configMock);
        doNothing()
                .when(configConnector)
                .resetConfiguration(any());
        doReturn(tokensMock)
                .when(tokenConnector)
                .findByStatusAndProductId(any(), any(), any());
        when(institutionConnector.findById(any()))
                .thenThrow(ResourceNotFoundException.class);
        // When
        scheduler.regenerateQueueNotifications();
        // Then
        verify(schedulerConfig, times(1))
                .getRegnerateKafkaQueueEnabled();
        verify(configConnector, times(1))
                .findById(schedulerId);
        verify(configConnector, times(1))
                .resetConfiguration(schedulerId);
        verify(tokenConnector, times(1))
                .findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED, RelationshipState.SUSPENDED), configMock.getProductFilter(), 0);
        verify(institutionConnector, times(1))
                .findById(tokensMock.get(0).getInstitutionId());
        verifyNoMoreInteractions(tokenConnector, institutionConnector, contractService);
    }

}
