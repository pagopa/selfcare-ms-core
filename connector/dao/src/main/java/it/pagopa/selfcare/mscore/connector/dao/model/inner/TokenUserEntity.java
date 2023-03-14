package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import lombok.Data;

@Data
public class TokenUserEntity {
    private String userId;
    private PartyRole role;
}
