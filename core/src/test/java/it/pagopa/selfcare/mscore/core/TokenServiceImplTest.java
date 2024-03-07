package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.PaginatedToken;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static it.pagopa.selfcare.mscore.constant.CustomError.TOKEN_ALREADY_CONSUMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TokenServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    @Mock
    private ContractEventNotificationService contractService;
    @Mock
    private UserService userService;

    @Mock
    private TokenConnector tokenConnector;

    @Mock
    private InstitutionConnector institutionConnector;

    @Mock
    private OnboardingDao onboardingDao;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    @Test
    void verifyToken() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setExpiringDate(OffsetDateTime.now().plusDays(60));

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verifyNoMoreInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_nullExpiringDate() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setExpiringDate(null);

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verifyNoMoreInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_expired() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setExpiringDate(OffsetDateTime.now().minusDays(2));
        Institution institutionMock = mockInstance(new Institution());
        institutionMock.setId(tokenMock.getInstitutionId());

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        when(institutionConnector.findById(any())).thenReturn(institutionMock);
        doReturn(null).when(onboardingDao).persistForUpdate(any(), any(), any(), any());
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()),
                resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verify(institutionConnector, times(1)).findById(tokenMock.getInstitutionId());
        verify(onboardingDao, times(1)).persistForUpdate(tokenMock, institutionMock, RelationshipState.REJECTED, null);
        verifyNoMoreInteractions(tokenConnector, institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.REJECTED);

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()),
                resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed_nullUsers() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setUsers(null);

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()),
                resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed_emptyUsers() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.TOBEVALIDATED);
        tokenMock.setUsers(Collections.emptyList());

        when(tokenConnector.findById(any())).thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()),
                resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1)).findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyOnboarding() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        String institutionIdMock = "InstitutionIdMock";

        when(tokenConnector.findWithFilter(any(), any())).thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyOnboarding(institutionIdMock, tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1)).findWithFilter(institutionIdMock, tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
    }

    /**
     * Method under test:
     * {@link TokenServiceImpl#retrieveContractsFilterByStatus(List, Integer, Integer, String)}
     */
    @Test
    void testGetAllTokens_founded() {
        // Arrange
        Token tokenResult= new Token();
        tokenResult.setId("id");
        tokenResult.setStatus(RelationshipState.ACTIVE);
        tokenResult.setProductId("productId");

        Institution institution = mockInstance(new Institution());
        institution.setId(tokenResult.getInstitutionId());

        Onboarding onboarding = mockInstance(new Onboarding());
        onboarding.setProductId("productId");
        institution.setOnboarding(List.of(onboarding));

        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(tokenResult);

        when(tokenConnector.findByStatusAndProductId(Mockito.any(), Mockito.any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(tokenList);
        when(institutionConnector.findById(Mockito.any())).thenReturn(institution);
        when(contractService.toNotificationToSend((NotificationToSend) any(),any(), any())).thenReturn(new NotificationToSend());

        // Assert
        PaginatedToken paginatedToken = tokenServiceImpl.retrieveContractsFilterByStatus(List.of(RelationshipState.ACTIVE), 0, 1, null);
        Assertions.assertEquals(1, paginatedToken.getItems().size());
    }

    @Test
    void testGetAllTokens_NotFound() {

        when(tokenConnector.findByStatusAndProductId(Mockito.any(), Mockito.any(),
                Mockito.<Integer>any(), Mockito.<Integer>any())).thenReturn(Collections.emptyList());

        // Assert
        Assertions.assertDoesNotThrow(() -> tokenServiceImpl.retrieveContractsFilterByStatus(List.of(RelationshipState.DELETED, RelationshipState.ACTIVE), 0, 10, null));
    }
}

