package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionProduct;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ProductMapper {

    public static OnboardedProducts toOnboardedProducts(List<InstitutionProduct> list) {
        OnboardedProducts onboardedProducts = new OnboardedProducts();
        onboardedProducts.setProducts(list);
        return onboardedProducts;
    }

    public static InstitutionProduct toResource(Onboarding onboarding) {
        InstitutionProduct products = new InstitutionProduct();

        return products;
    }
}
