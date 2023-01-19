package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;

@Data
public class Person {

    private String id;
    private String taxCode;
    private String name;
    private String surname;
    private String role;
    private String productRole;

}
