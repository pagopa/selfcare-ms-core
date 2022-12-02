package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.core.model.Institution;

public interface ProcessService {


    Institution getInstitutionByExternalId(String externalId);

}
