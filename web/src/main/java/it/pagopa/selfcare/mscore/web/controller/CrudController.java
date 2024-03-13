package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.pagopa.selfcare.mscore.core.migration.MigrationService;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.web.model.migration.MigrationInstitution;
import it.pagopa.selfcare.mscore.web.model.migration.MigrationMapper;
import it.pagopa.selfcare.mscore.web.model.migration.MigrationOnboardedUser;
import it.pagopa.selfcare.mscore.web.model.migration.MigrationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/migration", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Migration")
public class CrudController {

    private final MigrationService migrationService;

    public CrudController(MigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.save.token}", notes = "${swagger.mscore.migration.save.token}")
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> createToken(@RequestBody @Valid MigrationToken migrationTokenRequest) {
        Token token = migrationService.createToken(MigrationMapper.toToken(migrationTokenRequest));
        return ResponseEntity.ok().body(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.save.institution}", notes = "${swagger.mscore.migration.save.institution}")
    @PostMapping(value = "/institution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Institution> createInstitution(@RequestBody @Valid MigrationInstitution institutionRequest) {
        Institution institution =  migrationService.createInstitution(MigrationMapper.toInstitution(institutionRequest));
        return ResponseEntity.ok().body(institution);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.save.user}", notes = "${swagger.mscore.migration.save.user}")
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OnboardedUser> createUser(@RequestBody @Valid MigrationOnboardedUser userRequest) {
        OnboardedUser user = migrationService.createUser(MigrationMapper.toOnboardedUser(userRequest));
        return ResponseEntity.ok().body(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.find.institution}", notes = "${swagger.mscore.migration.find.institution}")
    @GetMapping(value = "/institutions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Institution>> findInstitutions() {
        List<Institution> institutions = migrationService.findInstitution();
        return ResponseEntity.ok().body(institutions);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.find.token}", notes = "${swagger.mscore.migration.find.token}")
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OnboardedUser>> findUsers() {
        List<OnboardedUser> onboardedUsers = migrationService.findUser();
        return ResponseEntity.ok().body(onboardedUsers);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.findbyid.token}", notes = "${swagger.mscore.migration.findbyid.token}")
    @GetMapping(value = "/token/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> findTokenById(@PathVariable("id") String id) {
        Token token = migrationService.findTokenById(id);
        return ResponseEntity.ok().body(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.findbyid.institution}", notes = "${swagger.mscore.migration.findbyid.institution}")
    @GetMapping(value = "/institution/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Institution> findInstitutionById(@PathVariable("id") String id) {
        Institution institution = migrationService.findInstitutionById(id);
        return ResponseEntity.ok().body(institution);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="${swagger.mscore.migration.findbyid.user}", notes = "${swagger.mscore.migration.findbyid.user}")
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OnboardedUser> findUserById(@PathVariable("id") String id) {
        OnboardedUser onboardedUser = migrationService.findUserById(id);
        return ResponseEntity.ok().body(onboardedUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value ="${swagger.mscore.migration.delete.token}", notes = "${swagger.mscore.migration.delete.token}")
    @DeleteMapping(value = "/token/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Token> deleteToken(@PathVariable("id") String id) {
        migrationService.deleteToken(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value ="${swagger.mscore.migration.delete.institution}", notes = "${swagger.mscore.migration.delete.institution}")
    @DeleteMapping(value = "/institution/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Institution> deleteInstitution(@PathVariable("id") String id) {
        migrationService.deleteInstitution(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value ="${swagger.mscore.migration.delete.user}", notes = "${swagger.mscore.migration.delete.user}")
    @DeleteMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OnboardedUser> deleteUser(@PathVariable("id") String id) {
        migrationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
