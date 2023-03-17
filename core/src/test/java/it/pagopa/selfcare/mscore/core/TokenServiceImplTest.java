package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenRelationships;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import it.pagopa.selfcare.mscore.constant.RelationshipState;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {
    @Mock
    private UserService userService;

    @Mock
    private TokenConnector tokenConnector;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    @Test
    void verifyToken() {
        Token token = new Token();
        ArrayList<TokenUser> users = new ArrayList<>();
        users.add(new TokenUser());
        token.setUsers(users);
        token.setStatus(RelationshipState.ACTIVE);
        when(tokenConnector.findById(any())).thenReturn(token);
        assertThrows(ResourceConflictException.class, () -> tokenServiceImpl.verifyToken("42"));
    }

    @Test
    void verifyToken2() {
        Token token = new Token();
        ArrayList<TokenUser> stringList = new ArrayList<>();
        stringList.add(new TokenUser());
        token.setUsers(stringList);
        token.setStatus(RelationshipState.TOBEVALIDATED);
        when(tokenConnector.findById(any())).thenReturn(token);
        Token response = tokenServiceImpl.verifyToken("token");
        assertNotNull(response);
    }

    @Test
    void verifyOnboarding() {
        Token token = new Token();
        when(tokenConnector.findWithFilter(any(), any())).thenReturn(token);
        Assertions.assertDoesNotThrow(() -> tokenServiceImpl.verifyOnboarding("42", "42"));
    }

    /**
     * Method under test: {@link TokenServiceImpl#retrieveToken}
     */
    @Test
    void testRetrieveToken() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        ArrayList<InstitutionGeographicTaxonomies> institutionGeographicTaxonomiesList = new ArrayList<>();
        institutionUpdate.setGeographicTaxonomies(institutionGeographicTaxonomiesList);
        institutionUpdate.setImported(true);
        institutionUpdate.setInstitutionType(InstitutionType.PA);
        institutionUpdate
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate.setRea("Rea");
        institutionUpdate.setShareCapital("Share Capital");
        institutionUpdate.setSupportEmail("jane.doe@example.org");
        institutionUpdate.setSupportPhone("6625550144");
        institutionUpdate.setTaxCode("Tax Code");
        institutionUpdate.setZipCode("21654");

        Token token = new Token();
        token.setChecksum("Checksum");
        token.setClosedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setInstitutionUpdate(institutionUpdate);
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenConnector.findById( any())).thenReturn(token);
        when(userService.findAllByIds(any())).thenReturn(new ArrayList<>());
        TokenRelationships actualRetrieveTokenResult = tokenServiceImpl.retrieveToken("42");
        assertEquals("Checksum", actualRetrieveTokenResult.getChecksum());
        assertEquals("42", actualRetrieveTokenResult.getTokenId());
        assertEquals("42", actualRetrieveTokenResult.getProductId());
        assertEquals("42", actualRetrieveTokenResult.getInstitutionId());
    }

}

