package it.pagopa.selfcare.mscore.web.controller;

import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = {RequestMethod.HEAD}, value = "/{id}")
    public ResponseEntity<Void> verifyPerson(@PathVariable(value = "id") String id) {
        if (userService.verifyPerson(id))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @RequestMapping(method = {RequestMethod.GET}, value = "/{Id}")
    public ResponseEntity<Person> retrievePerson(@PathVariable(value = "id") String id) {
        OnboardedUser user = userService.retrievePerson(id);
        return ResponseEntity.ok().body(UserMapper.toResource(user));
    }

    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        OnboardedUser user = userService.createPerson(UserMapper.fromDto(person));
        return ResponseEntity.ok().body(UserMapper.toResource(user));
    }

}
