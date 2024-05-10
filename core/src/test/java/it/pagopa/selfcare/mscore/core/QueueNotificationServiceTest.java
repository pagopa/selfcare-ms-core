package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.aggregation.QueryCount;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class QueueNotificationServiceTest {
    @InjectMocks
    private QueueNotificationServiceImpl schedulerService;
    @Mock
    private ContractEventNotificationService contractService;

    @Mock
    private UserConnector userConnector;
    @Mock
    private UserEventService userEventService;
    @Mock
    private InstitutionConnector institutionConnector;


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

        schedulerService = new QueueNotificationServiceImpl(contractService, userEventService, institutionConnector, null);

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
    void startUserScheduler(){
        //given
        final List<String> productIds = List.of("productId");
        final Optional<Integer> size = Optional.of(1);
        final Optional<Integer> page = Optional.of(0);
        final OnboardedUser onboardedUser = mockInstance(new OnboardedUser());
        when(userConnector.findAllValidUsers(any(), any(), any())).thenReturn(List.of(onboardedUser));
        //when
        Executable executable = () -> schedulerService.sendUsers(size, page, productIds, Optional.empty());
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
        Executable executable = () -> schedulerService.sendUsers(size, page, productIds, userId);
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
        Executable executable = () -> schedulerService.sendUsers(size, page, null, null);
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
        Executable executable = () -> schedulerService.sendUsers(Optional.empty(), Optional.empty(), productIds, Optional.empty());
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
        Executable executable = () -> schedulerService.sendUsers(Optional.empty(), Optional.empty(), productIds, Optional.empty());
        //then
        assertDoesNotThrow(executable);
        verifyNoInteractions(userEventService);
    }

    @Test
    void countUser(){
        //when
        Executable executable = () -> schedulerService.countUsers();
        //then
        assertDoesNotThrow(executable);
        verify(userConnector, times(1)).countUsers();
    }

    @Test
    void countUserMock() {
        List<QueryCount> prodCount = new ArrayList<>();
        prodCount.add(new QueryCount("prod1", 1));
        prodCount.add(new QueryCount("prod2", 2));
        prodCount.add(new QueryCount("prod3", 3));
        when(userConnector.countUsers()).thenReturn(prodCount);
        List<QueryCount> responseCount = schedulerService.countUsers();
        assertNotNull(responseCount);
        assertFalse(responseCount.isEmpty());
        assertEquals(3, responseCount.size());
        assertEquals(2, responseCount.get(1).getCount());
    }
}
