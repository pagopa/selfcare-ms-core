package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionProxyInfo;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String id);

    CategoryProxyInfo getCategory(String origin, String code);
}
