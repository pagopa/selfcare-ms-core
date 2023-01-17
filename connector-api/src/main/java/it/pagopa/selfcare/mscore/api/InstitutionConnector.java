package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;
import java.util.Optional;

public interface InstitutionConnector {

    List<Institution> findAll(Institution example);

    Optional<Institution> findByExternalId(String externalId);


}
