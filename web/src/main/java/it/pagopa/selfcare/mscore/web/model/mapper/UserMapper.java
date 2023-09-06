package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.UserResponse;
import it.pagopa.selfcare.mscore.web.model.user.InstitutionProducts;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.user.UserProductsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "fiscalCode", target = "taxCode")
    @Mapping(source = "familyName", target = "surname")
    UserResponse toUserResponse(User user);

    UserToOnboard toUserToOnboard(Person p);

    UserProductsResponse toEntity(OnboardedUser model);

    InstitutionProducts toInstitutionProducts(UserBinding model);
}
