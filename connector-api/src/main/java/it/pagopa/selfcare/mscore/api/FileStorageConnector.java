package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.exception.FileDownloadException;

public interface FileStorageConnector {

    String getTemplateFile(String templateName) throws FileDownloadException;

}
