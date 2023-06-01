package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionAggregationEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.aggregation.UserInstitutionBindingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardedProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionBinding;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.aggregation.UserInstitutionAggregation;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class UserMapper {

    public static OnboardedUser toOnboardedUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setId(entity.getId());
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
            OnboardedProduct product = toOnboardedProduct(entity);
            productList.add(product);
        }
        return productList;
    }

    private static OnboardedProduct toOnboardedProduct(OnboardedProductEntity entity) {
        OnboardedProduct product = new OnboardedProduct();
        product.setProductId(entity.getProductId());
        product.setRole(entity.getRole());
        product.setTokenId(entity.getTokenId());
        product.setContract(entity.getContract());
        product.setEnv(entity.getEnv());
        product.setRelationshipId(entity.getRelationshipId());
        product.setProductRole(entity.getProductRole());
        product.setStatus(entity.getStatus());
        product.setUpdatedAt(entity.getUpdatedAt());
        product.setCreatedAt(entity.getCreatedAt());
        return product;
    }

    public static UserEntity toUserEntity(OnboardedUser example) {
        UserEntity user = new UserEntity();
        if (example.getId() != null) {
            user.setId(example.getId());
        } else {
            user.setId(UUID.randomUUID().toString());
        }
        user.setCreatedAt(example.getCreatedAt());
        if (example.getBindings() != null) {
            user.setBindings(toBindingsEntity(example.getBindings()));
        }
        return user;
    }

    private static List<UserBindingEntity> toBindingsEntity(List<UserBinding> bindings) {
        List<UserBindingEntity> list = new ArrayList<>();
        for (UserBinding binding : bindings) {
            UserBindingEntity entity = new UserBindingEntity();
            entity.setInstitutionId(binding.getInstitutionId());
            if (binding.getProducts() != null) {
                entity.setProducts(toOnboardedProductEntity(binding.getProducts()));
            }
            list.add(entity);
        }
        return list;
    }

    private static List<OnboardedProductEntity> toOnboardedProductEntity(List<OnboardedProduct> products) {
        List<OnboardedProductEntity> productList = new ArrayList<>();
        for (OnboardedProduct product : products) {
            OnboardedProductEntity entity = toOnboardedProductEntity(product);
            productList.add(entity);
        }
        return productList;
    }

    private static OnboardedProductEntity toOnboardedProductEntity(OnboardedProduct entity) {
        OnboardedProductEntity product = new OnboardedProductEntity();
        product.setProductId(entity.getProductId());
        product.setRole(entity.getRole());
        product.setTokenId(entity.getTokenId());
        product.setContract(entity.getContract());
        product.setEnv(entity.getEnv());
        product.setRelationshipId(entity.getRelationshipId());
        product.setProductRole(entity.getProductRole());
        product.setStatus(entity.getStatus());
        product.setUpdatedAt(entity.getUpdatedAt());
        product.setCreatedAt(entity.getCreatedAt());
        return product;
    }

    public static List<UserInstitutionAggregation> toUserInstitutionAggregation(List<UserInstitutionAggregationEntity> entity) {
        return entity.stream().map(UserMapper::constructUserInstitutionAggregation).collect(Collectors.toList());
    }

    private static UserInstitutionAggregation constructUserInstitutionAggregation(UserInstitutionAggregationEntity entity) {
        UserInstitutionAggregation userInstitutionAggregation = new UserInstitutionAggregation();
        userInstitutionAggregation.setId(entity.getId());
        if (entity.getBindings() != null) {
            userInstitutionAggregation.setBindings(toBinding(entity.getBindings()));
        }
        if (entity.getInstitutions() != null && !entity.getInstitutions().isEmpty()) {
            userInstitutionAggregation.setInstitutions(InstitutionMapper.convertToInstitutionList(entity.getInstitutions()));
        }
        return userInstitutionAggregation;
    }

    private static UserInstitutionBinding toBinding(UserInstitutionBindingEntity bindings) {
        UserInstitutionBinding binding = new UserInstitutionBinding();
        binding.setInstitutionId(bindings.getInstitutionId());
        if (bindings.getProducts() != null) {
            binding.setProducts(toOnboardedProduct(bindings.getProducts()));
        }
        return binding;
    }
}
