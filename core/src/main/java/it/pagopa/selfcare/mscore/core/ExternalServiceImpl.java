package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.constant.ErrorEnum;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.PartyRole;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.INSTITUTION_NOT_FOUND;

@Service
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    private final InstitutionConnector institutionConnector;
    private final UserConnector userConnector;

    public ExternalServiceImpl(InstitutionConnector institutionConnector, UserConnector userConnector) {
        this.institutionConnector = institutionConnector;
        this.userConnector = userConnector;
    }

    @Override
    public Institution getBillingByExternalId(String externalId, String productId) {
        log.info("Retrieving billing data for institution having externalId {} and productId {}", externalId, productId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            List<Onboarding> list = opt.get().getOnboarding();
            Optional<Onboarding> optInstitutionProduct = list.stream().filter(onboarding -> onboarding.getProductId().equalsIgnoreCase(productId))
                    .findAny();
            if (optInstitutionProduct.isPresent())
                return opt.get();
            else
                throw new ResourceNotFoundException(String.format("The institution having externalId %s has not billing data for product %s", externalId, productId), "");
        } else {
            throw new ResourceNotFoundException(String.format("Cannot find institution having externalId %s", externalId), "");
        }
    }

    @Override
    public OnboardedUser getManagerByExternalId(String externalId, String productId) {
        log.info("Getting manager for institution having externalId {}", externalId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            Institution institution = opt.get();
            List<OnboardedUser> list = userConnector.findAll(constructOnboardedUserExample(institution, productId));
            if (list != null && !list.isEmpty())
                return list.get(0);
        }
        throw new ResourceNotFoundException(String.format(ErrorEnum.GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), externalId, productId),
                ErrorEnum.GET_INSTITUTION_MANAGER_NOT_FOUND.getCode());
    }

    private OnboardedUser constructOnboardedUserExample(Institution institution, String productId) {
        OnboardedUser onboardedUser = new OnboardedUser();
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setProductId(productId);
        product.setRoles(List.of(PartyRole.MANAGER));
        product.setStatus(RelationshipState.ACTIVE);
      //  onboardedUser.setInstitutionId(institution.getId());
       // onboardedUser.setProducts(productList);
        return onboardedUser;
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        log.info("Retrieving institution for externalId {}", externalId);
        Institution institution = new Institution();
        institution.setExternalId(externalId);

        List<Institution> list = institutionConnector.findAll(institution);
        if (list == null || list.isEmpty()) {
            log.info("Cannot find institution having externalId {}", externalId);
            throw new ResourceNotFoundException(INSTITUTION_NOT_FOUND.getMessage(), INSTITUTION_NOT_FOUND.getCode());
        }
        // TODO cosa fare se sono più di uno?
        return list.get(0);
    }
}
