package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.institution.Institution;

public interface InstitutionService {

    Institution createInstitutionByExternalId(String externalId);

    Institution createInstitutionRaw(Institution institution, String externalId);

    Institution createPgInstitution(String taxId);

}
