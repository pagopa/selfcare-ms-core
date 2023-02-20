package it.pagopa.selfcare.mscore.model;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RelationshipInfo {

    Institution institution;
    String userId;
    Map<String, OnboardedProductInfo> onboardedProductsInfo;
}
