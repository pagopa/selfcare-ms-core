package it.pagopa.selfcare.mscore.model;

import lombok.Data;

@Data
public class ContractImported {
    private String fileName;
    private String filePath;
    private String contractType;
}
