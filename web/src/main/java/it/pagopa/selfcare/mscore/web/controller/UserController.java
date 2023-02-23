package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.constant.CustomErrorEnum;
import it.pagopa.selfcare.mscore.constant.GenericErrorEnum;
import it.pagopa.selfcare.mscore.core.UserService;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.web.model.mapper.UserMapper;
import it.pagopa.selfcare.mscore.web.model.user.Person;
import it.pagopa.selfcare.mscore.web.model.user.PersonResponse;
import it.pagopa.selfcare.mscore.web.util.CustomExceptionMessage;
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
     * @param userId String
     *
     * @return PersonResponse
     * * Code: 200, Message: successful operation, DataType: PersonResponse
     * * Code: 404, Message: Person not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.info}", notes = "${swagger.mscore.person.info}")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonResponse> infoUser(@PathVariable(value = "id") String userId) {
        OnboardedUser onboardedUser = userService.findByUserId(userId);
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(onboardedUser, userId));
    }

    /**
     * The function verify is user existes
     *
     * @param userId String
     *
     * @return void
     * * Code: 200, Message: successful operation, DataType: GeographicTaxonomies
     * * Code: 404, Message: Person not found, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "${swagger.mscore.person.verify}", notes = "${swagger.mscore.person.verify}")
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String userId) {
        userService.verifyUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * The function persist a new User
     *
     * @param person Person
     *
     * @return PersonResponse
     * * Code: 201, Message: successful operation, DataType: PersonResponse
     * * Code: 409, Message: Conflict while creating person, DataType: Problem
     * * Code: 400, Message: Error while creating person, DataType: Problem
     *
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "${swagger.mscore.person.create}", notes = "${swagger.mscore.person.create}")
    @PostMapping(value = "")
    public ResponseEntity<PersonResponse> createUser(@RequestBody @Valid Person person) {
        CustomExceptionMessage.setCustomMessage(GenericErrorEnum.CREATE_PERSON_ERROR);
        OnboardedUser saved = userService.createUser(UserMapper.fromDto(person));
        return ResponseEntity.ok().body(UserMapper.toPersonResponse(saved, person.getId()));
    }



}
