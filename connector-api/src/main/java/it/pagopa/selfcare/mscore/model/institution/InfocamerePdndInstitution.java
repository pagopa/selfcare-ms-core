package it.pagopa.selfcare.mscore.model.institution;

import lombok.Data;

@Data
public class InfocamerePdndInstitution {
    private String businessTaxId;
    private String businessName;
    private String legalNature;
    private String legalNatureDescription;
    private String cciaa;
    private String nRea;
    private String businessStatus;
    private String city;
    private String county;
    private String zipCode;
    private String address;
    private String digitalAddress;
}
