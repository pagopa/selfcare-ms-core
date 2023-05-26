package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.Config;

public interface ConfigConnector {

    Config findAndUpdate(String id);

}
