package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.UserRegistryRestClient;
import it.pagopa.selfcare.mscore.connector.rest.mapper.UserMapperClient;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.UserResource;
import it.pagopa.selfcare.user_registry.generated.openapi.v1.dto.UserSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.EnumSet;
import java.util.UUID;

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
        User result = restClient.getUserByInternalId(UUID.fromString(userId), fieldList);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getUserByInternalId result = {}", result);
        return result;
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

}
