package it.pagopa.selfcare.mscore;

import it.pagopa.selfcare.commons.base.security.PartyRole;
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
    private List<String> productRole;
}
