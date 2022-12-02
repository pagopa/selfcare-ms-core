package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.core.model.Institution;
import it.pagopa.selfcare.mscore.model.institutions.Institution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    private final InstitutionConnector institutionConnector;

    public ProcessServiceImpl(InstitutionConnector institutionConnector) {
        this.institutionConnector = institutionConnector;
    }

    @Override
    public Institution getInstitutionByExternalId(String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);

        List<Institution> list = institutionConnector.findAll(institution);
        if (list != null && !list.isEmpty())
            return list.get(0);
        else
            return null;
    }
}
