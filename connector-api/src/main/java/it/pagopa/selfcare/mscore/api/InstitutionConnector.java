package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.institutions.Institution;

import java.util.List;

public interface InstitutionConnector {
    List<Institution> findAll(Institution example);
}
