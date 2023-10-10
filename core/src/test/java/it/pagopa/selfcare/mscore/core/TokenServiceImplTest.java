package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static it.pagopa.selfcare.commons.utils.TestUtils.reflectionEqualsByName;
import static it.pagopa.selfcare.mscore.constant.CustomError.TOKEN_ALREADY_CONSUMED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
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

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verifyNoMoreInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_nullExpiringDate() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setExpiringDate(null);

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
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

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        when(institutionConnector.findById(any()))
                .thenReturn(institutionMock);
        doReturn(null)
                .when(onboardingDao)
                .persistForUpdate(any(), any(), any(), any());
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()), resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verify(institutionConnector, times(1))
                .findById(tokenMock.getInstitutionId());
        verify(onboardingDao, times(1))
                .persistForUpdate(tokenMock, institutionMock, RelationshipState.REJECTED, null);
        verifyNoMoreInteractions(tokenConnector, institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.REJECTED);

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()), resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed_nullUsers() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.PENDING);
        tokenMock.setUsers(null);

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()), resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyToken_tokenAlreadyConsumed_emptyUsers() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setStatus(RelationshipState.TOBEVALIDATED);
        tokenMock.setUsers(Collections.emptyList());

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        Executable executable = () -> tokenServiceImpl.verifyToken(tokenMock.getId());
        // Then
        ResourceConflictException resourceConflictException = assertThrows(ResourceConflictException.class, executable);
        assertEquals(String.format(TOKEN_ALREADY_CONSUMED.getMessage(), tokenMock.getId()), resourceConflictException.getMessage());
        assertEquals(TOKEN_ALREADY_CONSUMED.getCode(), resourceConflictException.getCode());

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
        verifyNoInteractions(institutionConnector, onboardingDao);
    }

    @Test
    void verifyOnboarding() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        String institutionIdMock = "InstitutionIdMock";

        when(tokenConnector.findWithFilter(any(), any()))
                .thenReturn(tokenMock);
        // When
        Token result = tokenServiceImpl.verifyOnboarding(institutionIdMock, tokenMock.getId());
        // Then
        assertNotNull(result);
        reflectionEqualsByName(tokenMock, result);

        verify(tokenConnector, times(1))
                .findWithFilter(institutionIdMock, tokenMock.getId());
        verifyNoMoreInteractions(tokenConnector);
    }

    @Test
    void retrieveToken() {
        // Given
        Token tokenMock = mockInstance(new Token());
        tokenMock.setUsers(List.of(mockInstance(new TokenUser())));
        List<OnboardedUser> onboardedUsersMock = List.of(mockInstance(new OnboardedUser()));
        onboardedUsersMock.get(0).setBindings(List.of(mockInstance(new UserBinding())));

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        when(userService.findAllByIds(any(), anyBoolean()))
                .thenReturn(onboardedUsersMock);
        // When
        TokenRelationships result = tokenServiceImpl.retrieveToken(tokenMock.getId(), false);
        // Then
        assertNotNull(result);
        assertEquals(tokenMock.getId(), result.getTokenId());
        assertEquals(tokenMock.getChecksum(), result.getChecksum());
        assertEquals(tokenMock.getInstitutionId(), result.getInstitutionId());
        assertEquals(tokenMock.getProductId(), result.getProductId());
        reflectionEqualsByName(onboardedUsersMock.get(0), result.getUsers().get(0));

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verify(userService, times(1))
                .findAllByIds(List.of(tokenMock.getUsers().get(0).getUserId()), false);
    }

    @Test
    void retrieveToken_nullUsers() {
        // Given
        Token tokenMock = mockInstance(new Token());

        when(tokenConnector.findById(any()))
                .thenReturn(tokenMock);
        // When
        TokenRelationships result = tokenServiceImpl.retrieveToken(tokenMock.getId(), false);
        // Then
        assertNotNull(result);
        assertEquals(tokenMock.getId(), result.getTokenId());
        assertEquals(tokenMock.getChecksum(), result.getChecksum());
        assertEquals(tokenMock.getInstitutionId(), result.getInstitutionId());
        assertEquals(tokenMock.getProductId(), result.getProductId());
        assertTrue(result.getUsers().isEmpty());

        verify(tokenConnector, times(1))
                .findById(tokenMock.getId());
        verifyNoInteractions(userService);
    }

    @Test
    void getToken(){
        //given
        String institutionId = "institutionId";
        String productId = "productId";
        Token token = mockInstance(new Token());
        when(tokenConnector.findWithFilter(anyString(), anyString())).thenReturn(token);
        //when
        Token result = tokenServiceImpl.getToken(institutionId, productId);
        //then
        assertEquals(token, result);
        verify(tokenConnector, times(1)).findWithFilter(institutionId, productId);
    }

    /**
     * Method under test: {@link TokenServiceImpl#getTokensByProductId(String, Integer, Integer)}
     */
    @Test
    void testInstitutionsInstitutionsByProductId() {
        List<Token> tokens = new ArrayList<>();
        when(tokenConnector.findByStatusAndProductId(any(), any(), any(), any())).thenReturn(tokens);
        List<TokenRelationships> tokensResult = tokenServiceImpl.getTokensByProductId("id", 0, 1);
        assertTrue(tokensResult.isEmpty());
    }
}

