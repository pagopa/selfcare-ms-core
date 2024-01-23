package it.pagopa.selfcare.mscore.core.migration;

import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MigrationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class MigrationServiceImplTest {
    @MockBean
    private InstitutionConnector institutionConnector;

    @Autowired
    private MigrationServiceImpl migrationServiceImpl;

    @MockBean
    private TokenConnector tokenConnector;

    @MockBean
    private UserConnector userConnector;

    @Test
    void testCreateToken2(){
        Token token = new Token();
        token.setChecksum("Checksum");
        token.setDeletedAt(null);
        token.setContractSigned("Contract Signed");
        token.setContractTemplate("Contract Template");
        token.setCreatedAt(null);
        token.setExpiringDate(null);
        token.setId("42");
        token.setInstitutionId("42");
        token.setProductId("42");
        token.setStatus(RelationshipState.PENDING);
        token.setType(TokenType.INSTITUTION);
        token.setUpdatedAt(null);
        token.setUsers(new ArrayList<>());
        when(tokenConnector.save(any(), any())).thenReturn(token);

        assertNotNull(migrationServiceImpl.createToken(token));
    }

    /**
     * Method under test: {@link MigrationServiceImpl#createToken(Token)}
     */
    @Test
    void testCreateToken() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        token.setDeletedAt(null);
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
        when(tokenConnector.save(any(), any())).thenReturn(token);

        InstitutionUpdate institutionUpdate1 = new InstitutionUpdate();
        institutionUpdate1.setAddress("42 Main St");
        institutionUpdate1.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate1
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate1.setDescription("The characteristics of someone or something");
        institutionUpdate1.setDigitalAddress("42 Main St");
        institutionUpdate1.setGeographicTaxonomies(new ArrayList<>());
        institutionUpdate1.setImported(true);
        institutionUpdate1.setInstitutionType(InstitutionType.PA);
        institutionUpdate1
                .setPaymentServiceProvider(new PaymentServiceProvider("Abi Code", "42", "Legal Register Name", "42", true));
        institutionUpdate1.setRea("Rea");
        institutionUpdate1.setShareCapital("Share Capital");
        institutionUpdate1.setSupportEmail("jane.doe@example.org");
        institutionUpdate1.setSupportPhone("6625550144");
        institutionUpdate1.setTaxCode("Tax Code");
        institutionUpdate1.setZipCode("21654");

        Token token1 = new Token();
        token1.setChecksum("Checksum");
        token1.setDeletedAt(null);
        token1.setContractSigned("Contract Signed");
        token1.setContractTemplate("Contract Template");
        token1.setCreatedAt(null);
        token1.setExpiringDate(null);
        token1.setId("42");
        token1.setInstitutionId("42");
        token1.setInstitutionUpdate(institutionUpdate1);
        token1.setProductId("42");
        token1.setStatus(RelationshipState.PENDING);
        token1.setType(TokenType.INSTITUTION);
        token1.setUpdatedAt(null);
        token1.setUsers(new ArrayList<>());
        assertSame(token, migrationServiceImpl.createToken(token1));
        verify(tokenConnector).save(any(), any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#createInstitution(Institution)}
     */
    @Test
    void testCreateInstitution() {
        Institution institution = new Institution();
        when(institutionConnector.save(any())).thenReturn(institution);
        assertSame(institution, migrationServiceImpl.createInstitution(new Institution()));
        verify(institutionConnector).save(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#createUser(OnboardedUser)}
     */
    @Test
    void testCreateUser() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.save(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, migrationServiceImpl.createUser(new OnboardedUser()));
        verify(userConnector).save(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findToken()}
     */
    @Test
    void testFindToken() {
        ArrayList<Token> tokenList = new ArrayList<>();
        when(tokenConnector.findAll()).thenReturn(tokenList);
        List<Token> actualFindTokenResult = migrationServiceImpl.findToken();
        assertSame(tokenList, actualFindTokenResult);
        assertTrue(actualFindTokenResult.isEmpty());
        verify(tokenConnector).findAll();
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findUser()}
     */
    @Test
    void testFindUser() {
        ArrayList<OnboardedUser> onboardedUserList = new ArrayList<>();
        when(userConnector.findAll()).thenReturn(onboardedUserList);
        List<OnboardedUser> actualFindUserResult = migrationServiceImpl.findUser();
        assertSame(onboardedUserList, actualFindUserResult);
        assertTrue(actualFindUserResult.isEmpty());
        verify(userConnector).findAll();
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findInstitution()}
     */
    @Test
    void testFindInstitution() {
        ArrayList<Institution> institutionList = new ArrayList<>();
        when(institutionConnector.findAll()).thenReturn(institutionList);
        List<Institution> actualFindInstitutionResult = migrationServiceImpl.findInstitution();
        assertSame(institutionList, actualFindInstitutionResult);
        assertTrue(actualFindInstitutionResult.isEmpty());
        verify(institutionConnector).findAll();
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findTokenById(String)}
     */
    @Test
    void testFindTokenById() {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setAddress("42 Main St");
        institutionUpdate.setBusinessRegisterPlace("Business Register Place");
        institutionUpdate
                .setDataProtectionOfficer(new DataProtectionOfficer("42 Main St", "jane.doe@example.org", "Pec"));
        institutionUpdate.setDescription("The characteristics of someone or something");
        institutionUpdate.setDigitalAddress("42 Main St");
        institutionUpdate.setGeographicTaxonomies(new ArrayList<>());
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
        token.setDeletedAt(null);
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
        when(tokenConnector.findById(any())).thenReturn(token);
        assertSame(token, migrationServiceImpl.findTokenById("42"));
        verify(tokenConnector).findById(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findUserById(String)}
     */
    @Test
    void testFindUserById() {
        OnboardedUser onboardedUser = new OnboardedUser();
        when(userConnector.findById(any())).thenReturn(onboardedUser);
        assertSame(onboardedUser, migrationServiceImpl.findUserById("42"));
        verify(userConnector).findById(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#findInstitutionById(String)}
     */
    @Test
    void testFindInstitutionById() {
        Institution institution = new Institution();
        when(institutionConnector.findById(any())).thenReturn(institution);
        assertSame(institution, migrationServiceImpl.findInstitutionById("42"));
        verify(institutionConnector).findById(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#deleteToken(String)}
     */
    @Test
    void testDeleteToken() {
        doNothing().when(tokenConnector).deleteById(any());
        migrationServiceImpl.deleteToken("42");
        verify(tokenConnector).deleteById(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#deleteInstitution(String)}
     */
    @Test
    void testDeleteInstitution() {
        doNothing().when(institutionConnector).deleteById(any());
        migrationServiceImpl.deleteInstitution("42");
        verify(institutionConnector).deleteById(any());
    }

    /**
     * Method under test: {@link MigrationServiceImpl#deleteUser(String)}
     */
    @Test
    void testDeleteUser() {
        doNothing().when(userConnector).deleteById(any());
        migrationServiceImpl.deleteUser("42");
        verify(userConnector).deleteById(any());
    }
}

