package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.model.Institution;

public interface InstitutionService {

    Institution createInstitutionByExternalId(String externalId);

}
