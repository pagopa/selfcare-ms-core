package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static OnboardedUser toOnboardedUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setId(entity.getId());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setCreatedAt(entity.getCreatedAt());
        if (entity.getBindings() != null) {
            user.setBindings(toBindings(entity.getBindings()));
        }
        return user;
    }

    private static List<UserBinding> toBindings(List<UserBindingEntity> bindings) {
        List<UserBinding> list = new ArrayList<>();
        for (UserBindingEntity entity : bindings) {
            UserBinding binding = new UserBinding();
            binding.setInstitutionId(entity.getInstitutionId());
            binding.setCreatedAt(entity.getCreatedAt());
            if (entity.getProducts() != null) {
                binding.setProducts(toOnboardedProduct(entity.getProducts()));
            }
            list.add(binding);
        }
        return list;
    }

    private static List<OnboardedProduct> toOnboardedProduct(List<OnboardedProductEntity> products) {
        List<OnboardedProduct> productList = new ArrayList<>();
        for (OnboardedProductEntity entity : products) {
            OnboardedProduct product = new OnboardedProduct();
            product.setProductId(entity.getProductId());
            product.setProductRoles(entity.getProductRoles());
            product.setRole(entity.getRole());
            product.setStatus(entity.getStatus());
            product.setEnv(entity.getEnv());
            product.setRelationshipId(entity.getRelationshipId());
            product.setContract(entity.getContract());
            product.setUpdatedAt(entity.getUpdatedAt());
            product.setCreatedAt(entity.getCreatedAt());
            productList.add(product);
        }
        return productList;
    }
}
