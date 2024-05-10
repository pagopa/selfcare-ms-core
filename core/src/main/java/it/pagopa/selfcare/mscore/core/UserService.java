package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.user.User;

public interface UserService {

    User retrieveUserFromUserRegistry(String userId);

    User retrieveUserFromUserRegistryByFiscalCode(String fiscalCode);

    User persistUserRegistry(String name, String familyName, String fiscalCode, String email, String institutionId);

    User persistWorksContractToUserRegistry(String fiscalCode, String email, String institutionId);
}
