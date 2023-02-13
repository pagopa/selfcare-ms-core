package it.pagopa.selfcare.mscore.model;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OnboardedProduct {

    private String id;
    private String name;
    private RelationshipState status;
    private String contract;
    private List<String> roles;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
