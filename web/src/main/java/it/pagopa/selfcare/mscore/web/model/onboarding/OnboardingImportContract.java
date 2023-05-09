package it.pagopa.selfcare.mscore.web.model.onboarding;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OnboardingImportContract {

    private String fileName;
    private String filePath;
    private String contractType;
    private LocalDateTime createdAt;
}
