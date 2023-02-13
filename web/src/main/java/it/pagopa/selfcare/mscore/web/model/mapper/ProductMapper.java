package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionProduct;
import it.pagopa.selfcare.mscore.web.model.institution.ProductState;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ProductMapper {

    public static OnboardedProducts toOnboardedProducts(List<InstitutionProduct> list) {
        OnboardedProducts onboardedProducts = new OnboardedProducts();
        onboardedProducts.setProducts(list);
        return onboardedProducts;
    }

    public static InstitutionProduct toResource(Onboarding onboarding) {
        InstitutionProduct products = new InstitutionProduct();
        products.setId(onboarding.getProductId());
        products.setState(ProductState.valueOf(onboarding.getStatus().name()));
        return products;
    }

    public static List<InstitutionProduct> toInstitutionProducts(List<Onboarding> onboardings, List<String> states){
        List<InstitutionProduct> institutionProducts = onboardings.stream().map(ProductMapper::toResource).collect(Collectors.toList());
        if(states!=null && !states.isEmpty())
            institutionProducts = institutionProducts.stream().filter(institutionProduct -> states.contains(institutionProduct.getState().name())).collect(Collectors.toList());
        return institutionProducts;
    }
}
