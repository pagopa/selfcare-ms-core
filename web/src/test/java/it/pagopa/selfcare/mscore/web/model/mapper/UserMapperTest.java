package it.pagopa.selfcare.mscore.web.model.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserMapperTest {
    /**
     * Method under test: {@link UserMapper#toPersonResponse(OnboardedUser, String)}
     */
    @Test
    void testToPersonResponse() {
        assertNull(UserMapper.toPersonResponse(new OnboardedUser(), "42").getId());
        assertThrows(ResourceNotFoundException.class, () -> UserMapper.toPersonResponse(null, "foo"));
    }

    /**
     * Method under test: {@link UserMapper#toResource(OnboardedUser)}
     */
    @Test
    void testToResource() {
        assertNull(UserMapper.toResource(new OnboardedUser()).getId());
    }

    /**
     * Method under test: {@link UserMapper#fromDto(Person)}
     */
    @Test
    void testFromDto() {
        Person person = new Person();
        person.setEmail("jane.doe@example.org");
        person.setId("42");
        person.setName("Name");
        person.setProductRole(List.of("Product Role"));
        person.setRole(PartyRole.MANAGER);
        person.setSurname("Doe");
        person.setTaxCode("Tax Code");
        assertEquals("42", UserMapper.fromDto(person).getId());
    }
}

