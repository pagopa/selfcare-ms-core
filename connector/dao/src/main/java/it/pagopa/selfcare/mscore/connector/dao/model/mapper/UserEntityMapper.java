package it.pagopa.selfcare.mscore.connector.dao.model.mapper;


import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.UserBindingEntity;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface UserEntityMapper {

    OnboardedProductMapper productMapper = Mappers.getMapper(OnboardedProductMapper.class);

    OnboardedUser toOnboardedUser(UserEntity entity);

    @Mapping(target = "products", expression = "java(setProducts(entity.getBindings(), institutionId))")
    UserInfo toUserInfoByFirstInstitution(UserEntity entity, String institutionId);

    @Named("setProducts")
    default List<OnboardedProduct> setProducts(List<UserBindingEntity> bindings, String institutionId) {
        if(Objects.nonNull(bindings) && !bindings.isEmpty()) {
            UserBindingEntity filteredEntity = bindings.stream().filter(el -> institutionId.equals(el.getInstitutionId())).findFirst().orElse(null);
            return Objects.nonNull(filteredEntity) ?
                    filteredEntity.getProducts().stream().map(productMapper::toOnboardedProduct).collect(Collectors.toList())
                    : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    @Mapping(target = "id", defaultExpression = "java(UUID.randomUUID().toString())")
    UserEntity toUserEntity(OnboardedUser user);
}
