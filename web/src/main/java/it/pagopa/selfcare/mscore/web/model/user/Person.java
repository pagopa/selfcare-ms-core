package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
@Valid
public class Person {

    @NotEmpty(message = "User internal id is required")
    private String id;
    private String taxCode;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String productRole;

}
