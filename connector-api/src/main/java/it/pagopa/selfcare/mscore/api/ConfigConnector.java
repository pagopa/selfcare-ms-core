package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.Config;

public interface ConfigConnector {

    Config findById(String id);

    void resetConfiguration(String id);

}
