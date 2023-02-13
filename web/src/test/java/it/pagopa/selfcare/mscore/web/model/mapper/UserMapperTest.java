package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {


    @Test
    void testToPersonResponse() {
        assertNull(UserMapper.toPersonResponse(new OnboardedUser()).getUserId());
    }

    @Test
    void testToResource() {
        assertNull(UserMapper.toResource(new OnboardedUser()).getId());
    }

    @Test
    void testFromDto() {
        Person person = new Person();
        person.setEmail("jane.doe@example.org");
        person.setId("42");
        person.setName("Name");
        person.setProductRole("Product Role");
        person.setRole("Role");
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");
        assertEquals("42", UserMapper.fromDto(person).getUser());
    }
}

