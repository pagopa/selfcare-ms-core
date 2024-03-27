package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.NotificationToSend;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.onboarding.PaginatedToken;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.pagopa.selfcare.commons.utils.TestUtils.mockInstance;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

