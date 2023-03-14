package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "Persons")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The function verify is user exists
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
    @RequestMapping(method = { RequestMethod.HEAD}, value = "/persons/{id}")
    public ResponseEntity<Void> verifyUser(@PathVariable(value = "id") String userId) {
        userService.verifyUser(userId);
        return ResponseEntity.ok().build();
    }
}
