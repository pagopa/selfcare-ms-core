package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;

@Data
public class AdditionalInformations {
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
