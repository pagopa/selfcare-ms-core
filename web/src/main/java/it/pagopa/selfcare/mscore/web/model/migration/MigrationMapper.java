package it.pagopa.selfcare.mscore.web.model.migration;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedUser;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MigrationMapper {

    public static Token toToken(MigrationToken migrationToken) {
        Token token = new Token();
        token.setId(migrationToken.getId());
        token.setType(migrationToken.getType());
        token.setStatus(migrationToken.getStatus());
        token.setInstitutionId(migrationToken.getInstitutionId());
        token.setProductId(migrationToken.getProductId());
        token.setExpiringDate(migrationToken.getExpiringDate());
        token.setChecksum(migrationToken.getChecksum());
        token.setContractVersion(migrationToken.getContractVersion());
        token.setContractTemplate(migrationToken.getContractTemplate());
        token.setContractSigned(migrationToken.getContractSigned());
        token.setUsers(migrationToken.getUsers());
        token.setInstitutionUpdate(migrationToken.getInstitutionUpdate());
        token.setCreatedAt(migrationToken.getCreatedAt());
        token.setUpdatedAt(migrationToken.getUpdatedAt());
        token.setDeletedAt(migrationToken.getClosedAt());
        return token;
    }

    public static Institution toInstitution(MigrationInstitution institutionRequest) {
        Institution institution = new Institution();
        institution.setId(institutionRequest.getId());
        institution.setExternalId(institutionRequest.getExternalId());
        institution.setOrigin(institutionRequest.getOrigin());
        institution.setOriginId(institutionRequest.getOriginId());
        institution.setDescription(institutionRequest.getDescription());
        institution.setInstitutionType(institutionRequest.getInstitutionType());
        institution.setDigitalAddress(institutionRequest.getDigitalAddress());
        institution.setAddress(institutionRequest.getAddress());
        institution.setZipCode(institutionRequest.getZipCode());
        institution.setTaxCode(institutionRequest.getTaxCode());
        institution.setBilling(institutionRequest.getBilling());
        institution.setOnboarding(institutionRequest.getOnboarding());
        institution.setGeographicTaxonomies(institutionRequest.getGeographicTaxonomies());
        institution.setAttributes(institutionRequest.getAttributes());
        institution.setPaymentServiceProvider(institutionRequest.getPaymentServiceProvider());
        institution.setDataProtectionOfficer(institutionRequest.getDataProtectionOfficer());
        institution.setRea(institutionRequest.getRea());
        institution.setShareCapital(institutionRequest.getShareCapital());
        institution.setBusinessRegisterPlace(institutionRequest.getBusinessRegisterPlace());
        institution.setSupportEmail(institutionRequest.getSupportEmail());
        institution.setSupportPhone(institutionRequest.getSupportPhone());
        institution.setImported(institutionRequest.isImported());
        institution.setCreatedAt(institutionRequest.getCreatedAt());
        institution.setUpdatedAt(institutionRequest.getUpdatedAt());
        return institution;
    }

    public static OnboardedUser toOnboardedUser(MigrationOnboardedUser userRequest) {
        OnboardedUser onboardedUser = new OnboardedUser();
        onboardedUser.setId(userRequest.getId());
        onboardedUser.setBindings(userRequest.getBindings());
        onboardedUser.setCreatedAt(userRequest.getCreatedAt());
        return onboardedUser;
    }
}
