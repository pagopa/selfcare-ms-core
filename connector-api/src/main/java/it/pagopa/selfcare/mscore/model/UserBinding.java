package it.pagopa.selfcare.mscore.model;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@FieldNameConstants(asEnum = true)
public class UserBinding {
    private String institutionId;
    private List<OnboardedProduct> products;
    private OffsetDateTime createdAt;
}
