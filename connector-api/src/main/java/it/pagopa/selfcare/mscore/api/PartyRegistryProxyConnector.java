package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;

import java.util.List;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String id);

    CategoryProxyInfo getCategory(String origin, String code);

    List<InstitutionByLegal> getInstitutionsByLegal(String taxId);

}
