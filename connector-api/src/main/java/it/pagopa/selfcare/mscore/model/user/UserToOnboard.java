package it.pagopa.selfcare.mscore.model.user;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.EnvEnum;
import lombok.Data;

import java.util.List;

@Data
public class UserToOnboard {

    private String id;
    private String taxCode;
    private String name;
    private String surname;
    private String email;
    private PartyRole role;
    private String productRole;
    private EnvEnum env;
}
