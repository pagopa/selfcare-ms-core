package it.pagopa.selfcare.mscore.connector.dao.model.inner;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants(asEnum = true)
public class AdditionalInformationsEntity {
    private boolean belongRegulatedMarket;
    private String regulatedMarketNote;
    private boolean isIpa;
    private String ipaCode;
    private boolean establishedByRegulatoryProvision;
    private String establishedByRegulatoryProvisionNote;
    private boolean isAgentOfPublicService;
    private String agentOfPublicServiceNote;
    private String otherNote;
}
