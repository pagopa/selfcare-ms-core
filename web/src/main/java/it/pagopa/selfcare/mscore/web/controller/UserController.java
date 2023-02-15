package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.PersonService;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.user.PersonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/persons")
@Api(tags = "Persons")
public class UserController {

    private final PersonService personService;

    public UserController(PersonService personService) {
        this.personService = personService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.info}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonResponse> infoUser(@PathVariable(value = "id") String uuid) {
        OnboardedUser onboardedUser = personService.findByUserId(uuid);
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(onboardedUser));
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String uuid) {
        personService.findByUserId(uuid);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.create}")
    @PostMapping(value = "")
    public ResponseEntity<PersonResponse> createUser(@RequestBody @Valid Person person) {
        OnboardedUser saved = personService.createUser(UserMapper.fromDto(person));
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(saved));
    }



}
