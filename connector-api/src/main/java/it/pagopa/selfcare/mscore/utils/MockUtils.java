package it.pagopa.selfcare.mscore.utils;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.constant.TokenType;
import it.pagopa.selfcare.mscore.model.Config;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MockUtils {

    public static Config createConfigMock(boolean enabled, String productFilter) {
        Config configMock = new Config();
        configMock.setProductFilter(productFilter);
        configMock.setFirstPage(0);
        configMock.setLastPage(10);
        return configMock;
    }

    public static Token createTokenMock(Integer bias, RelationshipState status, InstitutionType institutionType) {
        Token tokenMock = new Token();
        tokenMock.setId("TokenId" + (bias == null ? "" : bias));
        tokenMock.setType(TokenType.INSTITUTION);
        tokenMock.setStatus(status);
        tokenMock.setInstitutionId("InstitutionId" + (bias == null ? "" : bias));
        tokenMock.setProductId("ProductId" + (bias == null ? "" : bias));
        tokenMock.setExpiringDate(OffsetDateTime.now().plusDays(60));
        tokenMock.setChecksum("Checksum");
        tokenMock.setContractVersion("ContractVersion");
        tokenMock.setContractTemplate("ContractTemplate");
        tokenMock.setContractSigned("ContractPath/" + tokenMock.getId() + "/FileName");
        tokenMock.setContentType("application/pdf");
        tokenMock.setUsers(List.of(createTokenUserMock(1, PartyRole.MANAGER), createTokenUserMock(2, PartyRole.DELEGATE)));
        tokenMock.setInstitutionUpdate(createInstitutionUpdateMock(bias, institutionType));
        tokenMock.setCreatedAt(OffsetDateTime.now().minusDays(2));
        tokenMock.setUpdatedAt(OffsetDateTime.now().minusDays(1));
        tokenMock.setDeletedAt((status.equals(RelationshipState.DELETED) ? OffsetDateTime.now() : null));
        return tokenMock;
    }

    public static List<Token> createTokenListMock(Integer numberOfTokens, Integer startingBias, RelationshipState status, InstitutionType institutionType) {
        List<Token> tokensMock = new ArrayList<>();

        numberOfTokens += startingBias;
        for (int i = startingBias; i < numberOfTokens; i++) {
            tokensMock.add(createTokenMock(i, status, institutionType));
        }

        return tokensMock;
    }

    public static TokenUser createTokenUserMock(Integer bias, PartyRole partyRole) {
        return new TokenUser("TokenUserId" + (bias == null ? "" : bias), partyRole);
    }

    public static InstitutionUpdate createInstitutionUpdateMock(Integer bias, InstitutionType institutionType) {
        InstitutionUpdate institutionUpdateMock = new InstitutionUpdate();
        institutionUpdateMock.setInstitutionType(institutionType);
        institutionUpdateMock.setDescription("Description" + (bias == null ? "" : bias));
        institutionUpdateMock.setDigitalAddress("DigitalAddress" + (bias == null ? "" : bias));
        institutionUpdateMock.setAddress("Address" + (bias == null ? "" : bias));
        institutionUpdateMock.setTaxCode("TaxCode" + (bias == null ? "" : bias));
        institutionUpdateMock.setZipCode("ZipCode" + (bias == null ? "" : bias));
        institutionUpdateMock.setGeographicTaxonomies(List.of(createInstitutionGeographicTaxonomiesMock(1)));
        institutionUpdateMock.setSupportEmail("SupportEmail" + (bias == null ? "" : bias));
        institutionUpdateMock.setImported(false);
        return institutionUpdateMock;
    }

    public static InstitutionGeographicTaxonomies createInstitutionGeographicTaxonomiesMock(Integer bias) {
        return new InstitutionGeographicTaxonomies("InstitutionGeographicTaxonomiesCode" + (bias == null ? "" : bias), "InstitutionGeographicTaxonomiesDesc" + (bias == null ? "" : bias));
    }

    public static Institution createInstitutionMock(Integer bias, RelationshipState status, InstitutionType institutionType) {
        Institution institutionMock = new Institution();
        institutionMock.setId("InstitutionId" + (bias == null ? "" : bias));
        institutionMock.setExternalId("InstitutionExternalId" + (bias == null ? "" : bias));
        institutionMock.setOrigin("Origin");
        institutionMock.setOriginId("OriginId");
        institutionMock.setDescription("Description" + (bias == null ? "" : bias));
        institutionMock.setInstitutionType(institutionType);
        institutionMock.setDigitalAddress("DigitalAddress" + (bias == null ? "" : bias));
        institutionMock.setAddress("Address" + (bias == null ? "" : bias));
        institutionMock.setTaxCode("TaxCode" + (bias == null ? "" : bias));
        institutionMock.setZipCode("ZipCode" + (bias == null ? "" : bias));
        institutionMock.setBilling(createBilling(bias));
        institutionMock.setOnboarding(List.of(createOnboarding(bias, status)));
        institutionMock.setGeographicTaxonomies(List.of(createInstitutionGeographicTaxonomiesMock(null), createInstitutionGeographicTaxonomiesMock(1)));
        institutionMock.setSupportEmail("SupportEmail" + (bias == null ? "" : bias));
        institutionMock.setCreatedAt(OffsetDateTime.now().minusDays(2));
        institutionMock.setUpdatedAt(OffsetDateTime.now().minusDays(1));
        return institutionMock;
    }

    public static List<Institution> createInstitutionListMock(Integer numberOfInstitutions, Integer startingBias, RelationshipState status, InstitutionType institutionType) {
        List<Institution> institutionListMock = new ArrayList<>();

        numberOfInstitutions += startingBias;
        for (int i = startingBias; i < numberOfInstitutions; i++) {
            institutionListMock.add(createInstitutionMock(i, status, institutionType));
        }

        return institutionListMock;
    }

    public static Billing createBilling(Integer bias) {
        Billing billingMock = new Billing();
        billingMock.setVatNumber("VatNumber" + (bias == null ? "" : bias));
        billingMock.setRecipientCode("RecipientCode" + (bias == null ? "" : bias));
        billingMock.setPublicServices(false);
        return billingMock;
    }

    public static Onboarding createOnboarding(Integer bias, RelationshipState status) {
        Onboarding onboardingMock = new Onboarding();
        onboardingMock.setTokenId("TokenId" + (bias == null ? "" : bias));
        onboardingMock.setStatus(status);
        onboardingMock.setContract("Contract");
        onboardingMock.setPricingPlan("PricingPlan");
        return onboardingMock;
    }

}
