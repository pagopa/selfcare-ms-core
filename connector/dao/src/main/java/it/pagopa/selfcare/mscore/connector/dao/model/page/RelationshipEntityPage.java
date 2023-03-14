package it.pagopa.selfcare.mscore.connector.dao.model.page;

import lombok.Data;

import java.util.List;

@Data
public class RelationshipEntityPage {
    private Integer total;
    private List<RelationshipEntityPageElement> data;
}
