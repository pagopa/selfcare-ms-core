package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Product {

    private RelationshipState status;
    private String contract;
    private List<PartyRole> roles;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
