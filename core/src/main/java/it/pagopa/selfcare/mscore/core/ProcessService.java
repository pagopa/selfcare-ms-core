package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institutions.Institution;

import java.util.List;

public interface ProcessService {

    Institution createInstitution(Institution institution);

    List<Institution> getAllInstitution();

    Institution getInstitutionById(String id);

    Institution getInstitutionByExternalId(String externalId);

    void deleteInstitution(String id);

}
