package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.NationalRegistriesProfessionalAddress;

public interface NationalRegistriesConnector {

    NationalRegistriesProfessionalAddress getLegalAddress(String taxCode);
}
