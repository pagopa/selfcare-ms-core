package it.pagopa.selfcare.mscore.web.model.user;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
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
    private PartyRole role;
    private String productRole;
    private EnvEnum env = EnvEnum.ROOT;

}
