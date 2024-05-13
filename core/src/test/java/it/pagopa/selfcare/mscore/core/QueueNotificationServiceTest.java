package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

        schedulerService = new QueueNotificationServiceImpl(contractService, institutionConnector);

        when(institutionConnector.findById(institutionId)).thenReturn(institution);
        //when
        Executable executable = () -> schedulerService.sendContractsNotificationsByInstitutionIdAndTokenId(tokenId, institutionId);
        //then
        assertDoesNotThrow(executable);
        verify(institutionConnector, times(1)).findById(institutionId);
        verify(contractService, times(2)).sendDataLakeNotification(any(), any(), any());
        verifyNoMoreInteractions(contractService);
    }


}
