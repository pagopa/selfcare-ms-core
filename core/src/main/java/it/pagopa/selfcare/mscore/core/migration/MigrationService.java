package it.pagopa.selfcare.mscore.core.migration;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;

import java.util.List;

public interface MigrationService {

    Token createToken(Token token);

    OnboardedUser createUser(OnboardedUser user);

    Institution createInstitution(Institution institution);

    List<Token> findToken();

    List<OnboardedUser> findUser();

    List<Institution> findInstitution();

    Token findTokenById(String id);

    OnboardedUser findUserById(String id);

    Institution findInstitutionById(String id);

    void deleteToken(String id);

    void deleteInstitution(String id);

    void deleteUser(String id);
}
