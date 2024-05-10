package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class QueueNotificationServiceTest {
    @InjectMocks
    private QueueNotificationServiceImpl schedulerService;
    @Mock
    private ContractEventNotificationService contractService;

    @Mock
    private InstitutionConnector institutionConnector;
    @Mock
    private TokenConnector tokenConnector;


    @Test
    void startScheduler() {
        //given
        final Token token = mockInstance(new Token());
        token.setStatus(RelationshipState.ACTIVE);
        final InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        token.setInstitutionUpdate(institutionUpdate);
        final Institution institution = mockInstance(new Institution());

        schedulerService = new QueueNotificationServiceImpl(contractService,tokenConnector, institutionConnector);


        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));
        //when
        Executable executable = () -> schedulerService.sendContracts(Optional.of(1), List.of("product"));
        //then
        assertDoesNotThrow(executable);
        verify(tokenConnector, times(1)).findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), "product", 0, 1);
        verify(institutionConnector, times(1)).findById(token.getInstitutionId());
        verify(contractService, times(1)).sendDataLakeNotification(institution, token, QueueEvent.ADD);
    }


    @Test
    void sendContractsNotificationsByInstitutionIdAndTokenId() {
        //given
        String institutionId = "institutionId";
        String tokenId = "tokenId";
        final Institution institution = mockInstance(new Institution());
        Onboarding onboardingActive = new Onboarding();
        onboardingActive.setTokenId(tokenId);
        onboardingActive.setStatus(RelationshipState.ACTIVE);
        Onboarding onboarding = new Onboarding();
        onboarding.setTokenId(tokenId);
        onboarding.setStatus(RelationshipState.PENDING);
        Onboarding onboardingDeleted = new Onboarding();
        onboardingDeleted.setTokenId(tokenId);
        onboardingDeleted.setStatus(RelationshipState.DELETED);
        institution.setOnboarding(List.of(onboardingActive, onboardingDeleted, onboarding));

        schedulerService = new QueueNotificationServiceImpl(contractService,tokenConnector, institutionConnector);

        when(institutionConnector.findById(institutionId)).thenReturn(institution);
        //when
        Executable executable = () -> schedulerService.sendContractsNotificationsByInstitutionIdAndTokenId(tokenId, institutionId);
        //then
        assertDoesNotThrow(executable);
        verify(institutionConnector, times(1)).findById(institutionId);
        verify(contractService, times(2)).sendDataLakeNotification(any(), any(), any());
        verifyNoMoreInteractions(contractService);
    }

    @Test
    void startSchedulerUpdate(){
       //given
        final Token token = mockInstance(new Token());
        token.setStatus(RelationshipState.DELETED);
        final InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        token.setInstitutionUpdate(institutionUpdate);
        final Institution institution = mockInstance(new Institution());

        schedulerService = new QueueNotificationServiceImpl(contractService, tokenConnector, institutionConnector);


        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));
        //when
        Executable executable = () -> schedulerService.sendContracts(Optional.of(1), List.of("product"));
        //then
        assertDoesNotThrow(executable);
        verify(tokenConnector, times(1)).findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), "product", 0, 1);
        verify(institutionConnector, times(1)).findById(token.getInstitutionId());
        verify(contractService, times(1)).sendDataLakeNotification(institution, token, QueueEvent.UPDATE);
    }


    @Test
    void institutionNotFound(){
        //given
        final Token token = mockInstance(new Token());
        token.setStatus(RelationshipState.DELETED);

        schedulerService = new QueueNotificationServiceImpl(contractService, tokenConnector, institutionConnector);


        when(institutionConnector.findById(anyString())).thenThrow(ResourceNotFoundException.class);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));

        //when
        Executable executable = () -> schedulerService.sendContracts(Optional.of(1), List.of("product"));
        //then
        assertDoesNotThrow(executable);
        verify(tokenConnector, times(1)).findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), "product", 0, 1);
        verify(institutionConnector, times(1)).findById(token.getInstitutionId());
    }

    @Test
    void schedulerNotStarted(){
        Executable executable = () -> schedulerService.sendContracts(Optional.of(1), List.of("product"));
        verifyNoInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void productsFilterNotPresent(){
        Executable executable = () -> schedulerService.sendContracts(Optional.of(1), null);
        verifyNoInteractions(tokenConnector, institutionConnector, contractService);
    }

}
