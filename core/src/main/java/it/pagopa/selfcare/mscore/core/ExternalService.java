package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.Institution;

import java.util.List;

public interface ExternalService {

    Institution createInstitution(Institution institution);

    List<Institution> getAllInstitution();

    Institution getInstitutionById(String id);

    Institution getInstitutionByExternalId(String externalId);

    void deleteInstitution(String id);

}
