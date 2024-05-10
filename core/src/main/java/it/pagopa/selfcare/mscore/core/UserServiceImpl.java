package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private static final List<String> VALID_USER_RELATIONSHIPS = List.of(RelationshipState.ACTIVE.name(), RelationshipState.DELETED.name(), RelationshipState.SUSPENDED.name());
    private final UserRegistryConnector userRegistryConnector;
    private static final int USER_PAGE_SIZE = 100;

    public UserServiceImpl(UserRegistryConnector userRegistryConnector) {
        this.userRegistryConnector = userRegistryConnector;
    }

    @Override
    public User retrieveUserFromUserRegistry(String userId) {
        return userRegistryConnector.getUserByInternalIdWithFiscalCode(userId);
    }

    @Override
    public User retrieveUserFromUserRegistryByFiscalCode(String fiscalCode) {
        return userRegistryConnector.getUserByFiscalCode(fiscalCode);
    }

    @Override
    public User persistUserRegistry(String name, String familyName, String fiscalCode, String email, String institutionId) {
        return userRegistryConnector.persistUserUsingPatch(name ,familyName ,fiscalCode ,email , institutionId);
    }

    @Override
    public User persistWorksContractToUserRegistry(String fiscalCode, String email, String institutionId) {
        return userRegistryConnector.persistUserWorksContractUsingPatch(fiscalCode ,email , institutionId);
    }
}
