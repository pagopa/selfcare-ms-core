package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.user.User;

import java.util.EnumSet;

public interface UserRegistryConnector {

    User getUserByInternalId(String userId, EnumSet<User.Fields> fieldList);

    User getUserByFiscalCode(String fiscalCode);

    User persistUserUsingPatch(String name, String familyName, String fiscalCode, String email, String institutionId);

}
