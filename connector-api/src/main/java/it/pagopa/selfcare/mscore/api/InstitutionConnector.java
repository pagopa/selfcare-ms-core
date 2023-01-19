package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.institution.Institution;

import java.util.List;
import java.util.Optional;

public interface InstitutionConnector {

    Institution save(Institution example);

    List<Institution> findAll(Institution example);

    void deleteById(String id);

    Optional<Institution> findByExternalId(String externalId);
}
