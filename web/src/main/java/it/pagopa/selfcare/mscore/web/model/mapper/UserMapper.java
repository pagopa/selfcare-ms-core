package it.pagopa.selfcare.mscore.web.model.mapper;


import it.pagopa.selfcare.mscore.model.institution.WorkContact;
import it.pagopa.selfcare.mscore.model.user.UserBinding;
import it.pagopa.selfcare.mscore.model.user.UserToOnboard;
import it.pagopa.selfcare.mscore.web.model.user.InstitutionProducts;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserToOnboard toUserToOnboard(Person p);

    InstitutionProducts toInstitutionProducts(UserBinding model);


    @Named("retrieveMailFromWorkContacts")
    default String retrieveMailFromWorkContacts(Map<String, WorkContact> map, String institutionId){
        if(map!=null && !map.isEmpty() && map.containsKey(institutionId)){
            return map.get(institutionId).getEmail();
        }
        return null;
    }
}
