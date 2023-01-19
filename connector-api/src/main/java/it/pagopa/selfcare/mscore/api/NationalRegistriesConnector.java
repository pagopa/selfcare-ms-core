package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressResponse;

public interface NationalRegistriesConnector {

    NationalRegistriesAddressResponse getLegalAddress(String taxCode);
}
