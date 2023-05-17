package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.institution.*;

import java.util.List;

public interface PartyRegistryProxyConnector {

    InstitutionProxyInfo getInstitutionById(String id);

    CategoryProxyInfo getCategory(String origin, String code);

    List<InstitutionByLegal> getInstitutionsByLegal(String taxId);

    NationalRegistriesProfessionalAddress getLegalAddress(String taxId);

    GeographicTaxonomies getExtByCode(String code);
}
