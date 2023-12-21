package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.core.config.SchedulerConfig;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {
    @InjectMocks
    private SchedulerServiceImpl schedulerService;
    @Mock
    private ContractService contractService;

    @Mock
    private UserConnector userConnector;
    @Mock
    private UserEventService userEventService;
    @Mock
    private SchedulerConfig scheduledConfig;
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

        schedulerService = new SchedulerServiceImpl(contractService, userEventService, scheduledConfig, tokenConnector, institutionConnector, null);


        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));

        when(scheduledConfig.getSendOldEvent()).thenReturn(true);
        //when
        Executable executable = () -> schedulerService.startScheduler(Optional.of(1), List.of("product"));
        //then
        assertDoesNotThrow(executable);
        verify(tokenConnector, times(1)).findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), "product", 0, 1);
        verify(institutionConnector, times(1)).findById(token.getInstitutionId());
        verify(contractService, times(1)).sendDataLakeNotification(institution, token, QueueEvent.ADD);
    }

    @Test
    void startSchedulerUpdate(){
       //given
        final Token token = mockInstance(new Token());
        token.setStatus(RelationshipState.DELETED);
        final InstitutionUpdate institutionUpdate = mockInstance(new InstitutionUpdate());
        token.setInstitutionUpdate(institutionUpdate);
        final Institution institution = mockInstance(new Institution());

        schedulerService = new SchedulerServiceImpl(contractService, userEventService, scheduledConfig, tokenConnector, institutionConnector, null);


        when(institutionConnector.findById(anyString())).thenReturn(institution);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));

        when(scheduledConfig.getSendOldEvent()).thenReturn(true);
        //when
        Executable executable = () -> schedulerService.startScheduler(Optional.of(1), List.of("product"));
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

        schedulerService = new SchedulerServiceImpl(contractService, userEventService, scheduledConfig, tokenConnector, institutionConnector, null);


        when(institutionConnector.findById(anyString())).thenThrow(ResourceNotFoundException.class);
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(List.of(token));

        when(scheduledConfig.getSendOldEvent()).thenReturn(true);
        //when
        Executable executable = () -> schedulerService.startScheduler(Optional.of(1), List.of("product"));
        //then
        assertDoesNotThrow(executable);
        verify(tokenConnector, times(1)).findByStatusAndProductId(EnumSet.of(RelationshipState.ACTIVE, RelationshipState.DELETED), "product", 0, 1);
        verify(institutionConnector, times(1)).findById(token.getInstitutionId());
    }

    @Test
    void schedulerNotStarted(){
        Executable executable = () -> schedulerService.startScheduler(Optional.of(1), List.of("product"));
        verifyNoInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void productsFilterNotPresent(){
        Executable executable = () -> schedulerService.startScheduler(Optional.of(1), null);
        verifyNoInteractions(tokenConnector, institutionConnector, contractService);
    }

    @Test
    void startUserScheduler(){
        //given
        final List<String> productIds = List.of("productId");
        final Optional<Integer> size = Optional.of(1);
        final Optional<Integer> page = Optional.of(0);
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        when(userConnector.findAllValidUsers(any(), any(), any())).thenReturn(List.of(onboardedUser));
        //when
        Executable executable = () -> schedulerService.startUsersScheduler(size, page, productIds, Optional.empty());
        //then
        assertDoesNotThrow(executable);
        verify(userEventService, times(1)).sendOnboardedUserNotification(onboardedUser, productIds.get(0));
    }

    @Test
    void startUserScheduler_singleUser(){
        //given
        final List<String> productIds = List.of("productId");
        final Optional<Integer> size = Optional.of(1);
        final Optional<Integer> page = Optional.of(0);
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        final Optional<String> userId = Optional.of("userId");
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        //when
        Executable executable = () -> schedulerService.startUsersScheduler(size, page, productIds, userId);
        //then
        assertDoesNotThrow(executable);
        verify(userEventService, times(1)).sendOnboardedUserNotification(onboardedUser, productIds.get(0));

    }

    @Test
    void startUserScheduler_noProducts(){
        //given
        final Optional<Integer> size = Optional.of(1);
        final Optional<Integer> page = Optional.of(0);
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        final Optional<String> userId = Optional.of("userId");
        //when
        Executable executable = () -> schedulerService.startUsersScheduler(size, page, null, null);
        //then
        assertDoesNotThrow(executable);
        verifyNoInteractions(userEventService, userConnector);

    }

    @Test
    void startUserScheduler_pageDefault(){
        //given
        final List<String> productIds = List.of("productId");
        final Optional<Integer> size = Optional.of(1);
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        when(userConnector.findAllValidUsers(any(), any(), any())).thenReturn(List.of(onboardedUser));
        //when
        Executable executable = () -> schedulerService.startUsersScheduler(Optional.empty(), Optional.empty(), productIds, Optional.empty());
        //then
        assertDoesNotThrow(executable);
        verify(userEventService, times(1)).sendOnboardedUserNotification(onboardedUser, productIds.get(0));

    }

    @Test
    void startScheduler_emptyProductList(){
        //given
        final List<String> productIds = new ArrayList<>();
        final Optional<Integer> size = Optional.of(1);
        //when
        Executable executable = () -> schedulerService.startUsersScheduler(Optional.empty(), Optional.empty(), productIds, Optional.empty());
        //then
        assertDoesNotThrow(executable);
        verifyNoInteractions(userEventService);
    }
}
