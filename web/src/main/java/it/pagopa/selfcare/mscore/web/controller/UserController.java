package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.UserService;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The function retrieves person by id
     *
     * @param uuid String
     *
     * @return PersonResponse
     * * Code: 200, Message: successful operation, DataType: PersonResponse
     * * Code: 404, Message: Person not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.info}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonResponse> infoUser(@PathVariable(value = "id") String uuid) {
        OnboardedUser onboardedUser = userService.findByUserId(uuid);
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(onboardedUser, uuid));
    }

    /**
     * The function verify is user existes
     *
     * @param uuid String
     *
     * @return void
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: Person not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String uuid) {
        userService.findByUserId(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * The function persist a new User
     *
     * @param person Person
     *
     * @return PersonResponse
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 400, Message: User already exists, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "", notes = "${swagger.mscore.person.create}")
    @PostMapping(value = "")
    public ResponseEntity<PersonResponse> createUser(@RequestBody @Valid Person person) {
        OnboardedUser saved = userService.createUser(UserMapper.fromDto(person));
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(saved, person.getId()));
    }



}
