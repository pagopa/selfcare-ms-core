package it.pagopa.selfcare.mscore.api;

import it.pagopa.selfcare.mscore.model.onboarding.ResourceResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageConnector {
    ResourceResponse getFile(String fileName);
    String getTemplateFile(String templateName);
    void uploadContract(String id, MultipartFile contract);

}
