package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.user.UserResponse;
import it.pagopa.selfcare.mscore.web.model.user.InstitutionProducts;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.user.UserProductsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.fiscalCode", target = "taxCode")
    @Mapping(source = "user.familyName", target = "surname")
    @Mapping(target = "email", expression = "java(retrieveMailFromWorkContacts(user.getWorkContacts(), institutionId))")
    UserResponse toUserResponse(User user, String institutionId);

    UserToOnboard toUserToOnboard(Person p);

    UserProductsResponse toEntity(OnboardedUser model);

    InstitutionProducts toInstitutionProducts(UserBinding model);

    InstitutionUpdate toInstitutionUpdate(InstitutionUpdateRequest request);

    @Named("retrieveMailFromWorkContacts")
    default String retrieveMailFromWorkContacts(Map<String, WorkContact> map, String institutionId){
        if(map!=null && !map.isEmpty() && map.containsKey(institutionId)){
            return map.get(institutionId).getEmail();
        }
        return null;
    }
}
