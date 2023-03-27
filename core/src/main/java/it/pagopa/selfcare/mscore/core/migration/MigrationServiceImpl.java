package it.pagopa.selfcare.mscore.core.migration;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MigrationServiceImpl implements MigrationService {

    private final TokenConnector tokenConnector;
    private final InstitutionConnector institutionConnector;
    private final UserConnector userConnector;

    public MigrationServiceImpl(TokenConnector tokenConnector, InstitutionConnector institutionConnector, UserConnector userConnector){
        this.tokenConnector = tokenConnector;
        this.institutionConnector = institutionConnector;
        this.userConnector = userConnector;
    }

    @Override
    public Token createToken(Token token){
        if(token.getInstitutionUpdate() != null) {
            return tokenConnector.save(token, token.getInstitutionUpdate().getGeographicTaxonomies());
        }else {
            return tokenConnector.save(token, null);
        }
    }

    @Override
    public Institution createInstitution(Institution institution){
        return institutionConnector.save(institution);
    }

    @Override
    public OnboardedUser createUser(OnboardedUser user){
        return userConnector.save(user);
    }

    @Override
    public List<Token> findToken(){
        return tokenConnector.findAll();
    }

    @Override
    public List<OnboardedUser> findUser(){
        return userConnector.findAll();
    }

    @Override
    public List<Institution> findInstitution(){
        return institutionConnector.findAll();
    }

    @Override
    public Token findTokenById(String id){
        return tokenConnector.findById(id);
    }

    @Override
    public OnboardedUser findUserById(String id){
        return userConnector.findById(id);
    }

    @Override
    public Institution findInstitutionById(String id){
        return institutionConnector.findById(id);
    }

    @Override
    public void deleteToken(String id) {
        tokenConnector.deleteById(id);
    }

    @Override
    public void deleteInstitution(String id) {
        institutionConnector.deleteById(id);
    }

    @Override
    public void deleteUser(String id) {
        userConnector.deleteById(id);
    }
}
