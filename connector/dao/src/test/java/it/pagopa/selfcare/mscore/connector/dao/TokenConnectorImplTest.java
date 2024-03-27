package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.utils.DaoMockUtils;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TokenConnectorImplTest {

    @InjectMocks
    TokenConnectorImpl tokenConnectorImpl;

    @Mock
    TokenRepository tokenRepository;

    @Captor
    ArgumentCaptor<Query> queryArgumentCaptor;

    @Captor
    ArgumentCaptor<Update> updateArgumentCaptor;

    @Captor
    ArgumentCaptor<FindAndModifyOptions> findAndModifyOptionsArgumentCaptor;

    @Captor
    ArgumentCaptor<Pageable> pageableArgumentCaptor;

    /**
     * Method under test: {@link TokenConnectorImpl#findById(String)}
     */
    @Test
    void testFindById() {
        when(tokenRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> tokenConnectorImpl.findById("42"));
        verify(tokenRepository).findById(any());
    }

    @Test
    void testFindById2() {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsers(new ArrayList<>());
        when(tokenRepository.findById(any())).thenReturn(Optional.of(tokenEntity));
        Token response = tokenConnectorImpl.findById("tokenId");
        assertNotNull(response);
    }

    @Test
    void findWithFilter() {
        TokenEntity token = new TokenEntity();
        token.setId("507f1f77bcf86cd799439011");
        token.setUsers(new ArrayList<>());
        when(tokenRepository.find(any(), any())).thenReturn(List.of(token));
        Token tokens = tokenConnectorImpl.findWithFilter("42", "42");
        Assertions.assertEquals("507f1f77bcf86cd799439011", tokens.getId());
    }

    @Test
    void updateOnboardedProductCreatedAt() {
        // Given
        String tokenIdMock = "tokenIdMock";
        OffsetDateTime createdAt = OffsetDateTime.parse("2020-11-01T02:15:30+01:00");
        OffsetDateTime activatedAt = OffsetDateTime.parse("2020-11-02T02:15:30+01:00");
        TokenEntity updatedTokenMock = mockInstance(new TokenEntity());
        updatedTokenMock.setId(tokenIdMock);
        when(tokenRepository.findAndModify(any(), any(), any(), any()))
                .thenReturn(updatedTokenMock);
        // When
        Token result = tokenConnectorImpl.updateTokenCreatedAt(tokenIdMock, createdAt, activatedAt);
        // Then
        assertNotNull(result);
        assertEquals(result.getId(), tokenIdMock);
        verify(tokenRepository, times(1))
                .findAndModify(queryArgumentCaptor.capture(), updateArgumentCaptor.capture(), findAndModifyOptionsArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertSame(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()), tokenIdMock);
        assertSame(capturedQuery.getQueryObject().get(TokenEntity.Fields.id.name()), tokenIdMock);
        Update capturedUpdate = updateArgumentCaptor.getValue();
        assertTrue(capturedUpdate.getUpdateObject().get("$set").toString().contains("updatedAt") &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains("updatedAt") &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(createdAt.toString()) &&
                capturedUpdate.getUpdateObject().get("$set").toString().contains(activatedAt.toString()));
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        String productId = "prod-io";
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, productId, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertEquals(productId, capturedQuery.getQueryObject().get(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId_productIdNull() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, null, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertFalse(capturedQuery.getQueryObject().toString().contains(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }

    @Test
    void findByStatusAndProductId_productIdBlank() {
        // Given
        EnumSet<RelationshipState> status = EnumSet.of(RelationshipState.ACTIVE);
        String productId = "";
        Integer pageNumber = 0;
        List<TokenEntity> tokenEntities = List.of(DaoMockUtils.createTokenEntityMock(null, RelationshipState.ACTIVE));
        Page<TokenEntity> tokenEntityPage = new PageImpl<>(tokenEntities);

        doReturn(tokenEntityPage)
                .when(tokenRepository)
                .find(any(), any(), any());
        // When
        List<Token> result = tokenConnectorImpl.findByStatusAndProductId(status, productId, pageNumber, 100);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tokenRepository, times(1))
                .find(queryArgumentCaptor.capture(), pageableArgumentCaptor.capture(), Mockito.eq(TokenEntity.class));
        Query capturedQuery = queryArgumentCaptor.getValue();
        assertFalse(capturedQuery.getQueryObject().toString().contains(TokenEntity.Fields.productId.name()));
        assertTrue(capturedQuery.getQueryObject().get(TokenEntity.Fields.status.name()).toString().contains(status.toString()));
        Pageable capturedPage = pageableArgumentCaptor.getValue();
        assertEquals(pageNumber, capturedPage.getPageNumber());
        verifyNoMoreInteractions(tokenRepository);
    }
}
