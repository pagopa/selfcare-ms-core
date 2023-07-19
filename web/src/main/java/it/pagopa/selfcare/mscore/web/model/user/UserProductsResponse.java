package it.pagopa.selfcare.mscore.web.model.user;

import lombok.Data;

import java.util.List;

@Data
public class UserProductsResponse {

    private String id;
    private List<InstitutionProducts> bindings;
}
