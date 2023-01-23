package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.TokenConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.Token;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionConnector institutionConnector;
    private final UserConnector userConnector;
    private final TokenConnector tokenConnector;

    public ExternalServiceImpl(InstitutionConnector institutionConnector,
                               UserConnector userConnector,
                               TokenConnector tokenConnector) {
        this.institutionConnector = institutionConnector;
        this.userConnector = userConnector;
        this.tokenConnector = tokenConnector;
    }

    @Override
    public Institution getBillingByExternalId(Institution institution, String productId) {

        List<Onboarding> list = Optional.ofNullable(institution.getOnboarding()).orElse(new ArrayList<>());
        Optional<Onboarding> optInstitutionProduct = list.stream().filter(onboarding -> onboarding.getProductId().equalsIgnoreCase(productId))
                .findAny();
        if (optInstitutionProduct.isPresent()) {
            return institution;
        }else {
            throw new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_NOT_FOUND.getMessage(), institution.getExternalId(), productId),
                    GET_INSTITUTION_BILLING_NOT_FOUND.getCode());
        }
    }

    @Override
    public OnboardedUser getInstitutionManager(Institution institution, String productId) {
        List<OnboardedUser> list = userConnector.findOnboardedManager(institution.getId(), productId);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        throw new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), institution.getExternalId(), productId),
                GET_INSTITUTION_MANAGER_NOT_FOUND.getCode());
    }

    @Override
    public String getRelationShipToken(String institutionId, String userId, String productId) {
        List<Token> tokenList = tokenConnector.findActiveContract(institutionId, userId, productId);
        if(!tokenList.isEmpty()){
            return tokenList.get(0).getId();
        }else{
            throw new ResourceNotFoundException("","");
        }
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        log.info("Retrieving institution for externalId {}", externalId);
        Institution institution = new Institution();
        institution.setExternalId(externalId);

        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isEmpty()) {
            log.info("Cannot find institution having externalId {}", externalId);
            throw new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(),"not defined",externalId),
                    INSTITUTION_NOT_FOUND.getCode());
        }
        return opt.get();
    }
}
