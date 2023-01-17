package it.pagopa.selfcare.mscore.web.model.onboarding;

import it.pagopa.selfcare.mscore.web.model.institution.InstitutionProduct;
import lombok.Data;

import java.util.List;

@Data
public class OnboardedProducts {

    List<InstitutionProduct> products;

}
