package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.user.User;

import java.util.EnumSet;

public interface UserRegistryConnector {

    User getUserByInternalId(String userId, EnumSet<User.Fields> fieldList);

}
