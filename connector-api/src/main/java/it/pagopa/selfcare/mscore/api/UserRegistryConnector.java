package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.user.User;

public interface UserRegistryConnector {

    User getUserByInternalId(String userId);

    User getUserByFiscalCode(String fiscalCode);

    User persistUserUsingPatch(String name, String familyName, String fiscalCode, String email, String institutionId);

}
