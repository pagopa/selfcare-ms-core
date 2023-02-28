package it.pagopa.selfcare.mscore.model;

import lombok.Data;

@Data
public class ResourceResponse {
    private byte[] data;
    private String fileName;
    private String mimetype;
}
