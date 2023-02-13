package it.pagopa.selfcare.mscore.api;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageConnector {

    String getTemplateFile(String templateName);

    void uploadContract(String id, MultipartFile contract);

}
