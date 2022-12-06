package it.pagopa.selfcare.mscore.connector.dao.model;

import it.pagopa.selfcare.mscore.model.PartyRole;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("User")
public class ProductEntity {
    private String productId;
    private RelationshipState status;
    private String contract;
    private List<PartyRole> roles;
}
