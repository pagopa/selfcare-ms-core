package it.pagopa.selfcare.mscore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnboardedProductInfo {
    private String tokenId;
    private OnboardedProduct onboardedProduct;
}
