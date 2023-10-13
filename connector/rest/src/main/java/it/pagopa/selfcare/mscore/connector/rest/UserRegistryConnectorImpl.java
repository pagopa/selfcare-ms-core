package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;
import it.pagopa.selfcare.mscore.connector.rest.mapper.UserMapperClient;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.EnumSet;
import java.util.Map;



@Slf4j
@Service
public class UserRegistryConnectorImpl implements UserRegistryConnector {

    private final UserRegistryRestClient restClient;
    private final UserMapperClient userMapper;
    public static final String USERS_FIELD_LIST = "fiscalCode,name,familyName,workContacts";

    @Autowired
    public UserRegistryConnectorImpl(UserRegistryRestClient restClient, UserMapperClient userMapper) {
        this.restClient = restClient;
        this.userMapper = userMapper;
    }

    @Override
    public User getUserByInternalId(String userId, EnumSet<User.Fields> fieldList) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getUserByInternalId userId = {}", userId);
        Assert.hasText(userId, "A userId is required");
        Assert.notEmpty(fieldList, "At least one user fields is required");
        ResponseEntity<UserResource> result = restClient._findByIdUsingGET(USERS_FIELD_LIST, userId);
        User user = userMapper.toUser(result.getBody());
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getUserByInternalId result = {}", result);
        return user;
    }

    @Override
    public User getUserByFiscalCode(String fiscalCode) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getUserByFiscalCode fiscalCode = {}", fiscalCode);
        Assert.hasText(fiscalCode, "A userId is required");
        UserResource result = restClient._searchUsingPOST(USERS_FIELD_LIST, new UserSearchDto().fiscalCode(fiscalCode))
                .getBody();
        User user = userMapper.toUser(result);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getUserByFiscalCode result = {}", result);
        return user;
    }

    @Override
    public User persistUserUsingPatch(String name, String familyName, String fiscalCode, String email, String institutionId) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "persistUserByFiscalCode fiscalCode = {}", fiscalCode);
        Assert.hasText(fiscalCode, "A fiscalCode is required");
        UserId result = restClient._saveUsingPATCH(SaveUserDto.builder()
                        .name(CertifiableFieldResourceOfstring.builder()
                                .value(name)
                                .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                                .build())
                        .familyName(CertifiableFieldResourceOfstring.builder()
                                .value(familyName)
                                .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                                .build())
                        .fiscalCode(fiscalCode)
                        .workContacts(Map.of(institutionId, WorkContactResource.builder()
                                .email(CertifiableFieldResourceOfstring.builder()
                                        .value(email)
                                        .certification(CertifiableFieldResourceOfstring.CertificationEnum.NONE)
                                        .build())
                                .build()))
                        .build())
                        .getBody();
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "persistUserByFiscalCode result = {}", result);
        return userMapper.fromUserId(result);
    }


}
