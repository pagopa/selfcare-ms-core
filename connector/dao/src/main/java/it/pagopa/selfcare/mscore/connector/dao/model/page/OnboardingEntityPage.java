package it.pagopa.selfcare.mscore.connector.dao.model.page;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.OnboardingEntity;
import lombok.Data;

import java.util.List;

@Data
public class OnboardingEntityPage {
    private Integer total;
    private List<OnboardingEntity> data;
}
