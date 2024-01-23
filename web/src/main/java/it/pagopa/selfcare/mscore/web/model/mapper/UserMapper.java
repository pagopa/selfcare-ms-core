package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.UserNotificationToSend;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserInfo;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdateRequest;
import it.pagopa.selfcare.mscore.web.model.institution.UserInfoResponse;
import it.pagopa.selfcare.mscore.web.model.user.*;
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

    @Mapping(source = "userInfo.user.fiscalCode", target = "taxCode")
    @Mapping(source = "userInfo.user.familyName", target = "surname")
    @Mapping(source = "userInfo.user.name", target = "name")
    @Mapping(target = "email", expression = "java(retrieveMailFromWorkContacts(userInfo.getUser().getWorkContacts(), institutionId))")
    UserInfoResponse toUserInfoResponse(UserInfo userInfo, String institutionId);

    InstitutionProducts toInstitutionProducts(UserBinding model);

    InstitutionUpdate toInstitutionUpdate(InstitutionUpdateRequest request);

    UserNotificationResponse toUserNotification(UserNotificationToSend user);

    @Named("retrieveMailFromWorkContacts")
    default String retrieveMailFromWorkContacts(Map<String, WorkContact> map, String institutionId){
        if(map!=null && !map.isEmpty() && map.containsKey(institutionId)){
            return map.get(institutionId).getEmail();
        }
        return null;
    }
}
