package it.pagopa.selfcare.mscore.model.onboarding;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.model.institution.Billing;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.constant.TokenType;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.GenericError.CONTRACT_PATH_ERROR;

@Data
public class OnboardingRequest {

    private String productId;
    private String productName;
    private List<UserToOnboard> users;
    private String institutionExternalId;
    private InstitutionUpdate institutionUpdate;
    private String pricingPlan;
    private Billing billingRequest;
    private Contract contract;

    private boolean signContract = true;
    private TokenType tokenType;

    public Contract getContract() {
        if (InstitutionType.PG == institutionUpdate.getInstitutionType()
        || (contract != null && StringUtils.hasText(contract.getPath()))){
            return contract;
        }else{
            throw new InvalidRequestException(CONTRACT_PATH_ERROR.getMessage(),CONTRACT_PATH_ERROR.getCode());
        }
    }
}