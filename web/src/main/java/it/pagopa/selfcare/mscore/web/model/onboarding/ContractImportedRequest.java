package it.pagopa.selfcare.mscore.web.model.onboarding;

import lombok.Data;

@Data
public class ContractImportedRequest {
    private String fileName;
    private String filePath;
    private String contractType;
}
