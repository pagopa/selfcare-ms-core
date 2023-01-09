package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionInfoCamere;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String id);

    CategoryProxyInfo getCategory(String origin, String code);

    InstitutionInfoCamere getInstitutionFromInfoCamereById(String externalId);
}
