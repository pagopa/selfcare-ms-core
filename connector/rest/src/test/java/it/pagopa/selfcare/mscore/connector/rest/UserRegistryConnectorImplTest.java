package it.pagopa.selfcare.mscore.connector.rest;


import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;
import it.pagopa.selfcare.mscore.connector.rest.mapper.UserMapperClientImpl;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.EnumSet;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserRegistryConnectorImpl.class, UserMapperClientImpl.class})
@ExtendWith(SpringExtension.class)
class UserRegistryConnectorImplTest {
    @Autowired
    private UserRegistryConnectorImpl userRegistryConnector;

    @MockBean
    private UserRegistryRestClient userRegistryRestClient;

    /**
     * Method under test: {@link UserRegistryConnectorImpl#getUserByFiscalCode(String)}
     */
    @Test
    void testGetUserByFiscalCode() {
        UserResource userResource = new UserResource();
        userResource.setFiscalCode("42");
        ResponseEntity<UserResource> userIdResponseEntity = ResponseEntity.ok(userResource);
        when(userRegistryRestClient._searchUsingPOST(any(), any())).thenReturn(userIdResponseEntity);
        User user = userRegistryConnector.getUserByFiscalCode("42");
        assertEquals(user.getFiscalCode(), userResource.getFiscalCode());
        verify(userRegistryRestClient)._searchUsingPOST("fiscalCode,name,familyName,workContacts", UserSearchDto.builder().fiscalCode("42").build());
    }

    @Test
    void testGetUserByInternalId() {
        UUID id = UUID.randomUUID();
        UserResource userResource = new UserResource();
        userResource.setId(id);
        EnumSet<User.Fields> userFields = EnumSet.allOf(User.Fields.class);
        ResponseEntity<UserResource> userIdResponseEntity = ResponseEntity.ok(userResource);
        when(userRegistryRestClient._findByIdUsingGET(any(), any())).thenReturn(userIdResponseEntity);
        User user = userRegistryConnector.getUserByInternalId(id.toString(), userFields);
        assertEquals(user.getId(), userResource.getId().toString());
        verify(userRegistryRestClient)._findByIdUsingGET(userFields.toString(), id.toString());
    }

    @Test
    void testPersistUserUsingPatch() {
        UUID id = UUID.randomUUID();
        UserId userId = new UserId();
        userId.setId(id);
        ResponseEntity<UserId> userIdResponseEntity = ResponseEntity.ok(userId);
        when(userRegistryRestClient._saveUsingPATCH(any())).thenReturn(userIdResponseEntity);
        User user = userRegistryConnector.persistUserUsingPatch("name", "familyName", "fiscalCode", "email","institutionId");
        assertEquals(user.getId(), id.toString());
        SaveUserDto saveUserDto = SaveUserDto.builder()
                .name(CertifiableFieldResourceOfstring.builder()
                        .value("name")
                        .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                        .build())
                .familyName(CertifiableFieldResourceOfstring.builder()
                        .value("familyName")
                        .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                        .build())
                .fiscalCode("fiscalCode")
                .workContacts(Map.of("institutionId", WorkContactResource.builder()
                        .email(CertifiableFieldResourceOfstring.builder()
                                .value("email")
                                .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                                .build())
                        .build()))
                .build();
        verify(userRegistryRestClient)._saveUsingPATCH(saveUserDto);
    }


}

