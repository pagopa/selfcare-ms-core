package it.pagopa.selfcare.mscore.api;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageConnector {
    byte[] getFile(String fileName);
    String getTemplateFile(String templateName);
    void uploadContract(String id, MultipartFile contract);

}
