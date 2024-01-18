package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.TokenEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.InstitutionUpdateEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.TokenUserEntity;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import it.pagopa.selfcare.mscore.model.onboarding.TokenUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.connector.dao.model.mapper.InstitutionMapper.*;

@NoArgsConstructor(access = AccessLevel.NONE)
public class TokenMapper {

    public static TokenEntity convertToTokenEntity(Token token, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        TokenEntity entity = new TokenEntity();
        if (token.getId() != null) {
            entity.setId(token.getId());
        } else {
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setContractVersion(token.getContractVersion());
        entity.setContractTemplate(token.getContractTemplate());
        entity.setContractSigned(token.getContractSigned());
        entity.setChecksum(token.getChecksum());
        entity.setInstitutionId(token.getInstitutionId());
        entity.setStatus(token.getStatus());
        entity.setUsers(toTokenUserEntity(token.getUsers()));
        entity.setType(token.getType());
        entity.setExpiringDate(token.getExpiringDate());
        entity.setProductId(token.getProductId());
        entity.setCreatedAt(token.getCreatedAt());
        entity.setUpdatedAt(token.getUpdatedAt());
        entity.setActivatedAt(token.getActivatedAt());
        entity.setDeletedAt(token.getDeletedAt());
        entity.setContentType(token.getContentType());
        if (token.getInstitutionUpdate() != null) {
            entity.setInstitutionUpdate(toInstitutionUpdateEntity(token.getInstitutionUpdate(), geographicTaxonomies));
        }
        return entity;
    }

    private static InstitutionUpdateEntity toInstitutionUpdateEntity(InstitutionUpdate institutionUpdate, List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        InstitutionUpdateEntity entity = new InstitutionUpdateEntity();
        entity.setDescription(institutionUpdate.getDescription());
        entity.setDigitalAddress(institutionUpdate.getDigitalAddress());
        entity.setAddress(institutionUpdate.getAddress());
        entity.setTaxCode(institutionUpdate.getTaxCode());
        entity.setZipCode(institutionUpdate.getZipCode());
        entity.setCity(institutionUpdate.getCity());
        entity.setCounty(institutionUpdate.getCounty());
        entity.setCountry(institutionUpdate.getCountry());
        if (geographicTaxonomies != null && !geographicTaxonomies.isEmpty()) {
            entity.setGeographicTaxonomies(toGeoTaxonomyEntity(geographicTaxonomies));
        }else {
            entity.setGeographicTaxonomies(new ArrayList<>());
        }

        if (institutionUpdate.getPaymentServiceProvider() != null) {
            entity.setPaymentServiceProvider(toPaymentServiceProviderEntity(institutionUpdate.getPaymentServiceProvider()));
        }

        if (institutionUpdate.getDataProtectionOfficer() != null) {
            entity.setDataProtectionOfficer(toDataProtectionOfficerEntity(institutionUpdate.getDataProtectionOfficer()));
        }

        entity.setInstitutionType(institutionUpdate.getInstitutionType());
        entity.setRea(institutionUpdate.getRea());
        entity.setShareCapital(institutionUpdate.getShareCapital());
        entity.setBusinessRegisterPlace(institutionUpdate.getBusinessRegisterPlace());
        entity.setSupportEmail(institutionUpdate.getSupportEmail());
        entity.setSupportPhone(institutionUpdate.getSupportPhone());
        entity.setImported(institutionUpdate.isImported());
        if (institutionUpdate.getAdditionalInformations() != null) {
            entity.setAdditionalInformations(toAdditionalInformationsEntity(institutionUpdate.getAdditionalInformations()));
        }
        return entity;
    }

    public static Token convertToToken(TokenEntity tokenEntity) {
        Token token = new Token();
        token.setId(tokenEntity.getId());
        token.setContractTemplate(tokenEntity.getContractTemplate());
        token.setContractSigned(tokenEntity.getContractSigned());
        token.setContractVersion(tokenEntity.getContractVersion());
        token.setChecksum(tokenEntity.getChecksum());
        token.setInstitutionId(tokenEntity.getInstitutionId());
        token.setStatus(tokenEntity.getStatus());
        token.setType(tokenEntity.getType());
        if (tokenEntity.getUsers() != null) {
            token.setUsers(toTokenUser(tokenEntity.getUsers()));
        }
        token.setExpiringDate(tokenEntity.getExpiringDate());
        token.setCreatedAt(tokenEntity.getCreatedAt());
        token.setUpdatedAt(tokenEntity.getUpdatedAt());
        token.setActivatedAt(tokenEntity.getActivatedAt());
        token.setDeletedAt(token.getDeletedAt());
        token.setProductId(tokenEntity.getProductId());
        token.setContentType(tokenEntity.getContentType());
        if (tokenEntity.getInstitutionUpdate() != null) {
            token.setInstitutionUpdate(toInstitutionUpdate(tokenEntity.getInstitutionUpdate()));
        }
        return token;
    }

    private static InstitutionUpdate toInstitutionUpdate(InstitutionUpdateEntity entity) {
        InstitutionUpdate response = new InstitutionUpdate();
        response.setDescription(entity.getDescription());
        response.setDigitalAddress(entity.getDigitalAddress());
        response.setAddress(entity.getAddress());
        response.setTaxCode(entity.getTaxCode());
        response.setZipCode(entity.getZipCode());
        response.setCity(entity.getCity());
        response.setCounty(entity.getCounty());
        response.setCountry(entity.getCountry());
        if (entity.getGeographicTaxonomies() != null && !entity.getGeographicTaxonomies().isEmpty()) {
            response.setGeographicTaxonomies(entity.getGeographicTaxonomies().stream().map(geoTaxonomyEntity -> new InstitutionGeographicTaxonomies(geoTaxonomyEntity.getCode(), geoTaxonomyEntity.getDesc())).collect(Collectors.toList()));
        }

        if (entity.getPaymentServiceProvider() != null) {
            response.setPaymentServiceProvider(toPaymentServiceProvider(entity.getPaymentServiceProvider()));
        }

        if (entity.getDataProtectionOfficer() != null) {
            response.setDataProtectionOfficer(toDataProtectionOfficer(entity.getDataProtectionOfficer()));
        }

        response.setInstitutionType(entity.getInstitutionType());
        response.setRea(entity.getRea());
        response.setShareCapital(entity.getShareCapital());
        response.setBusinessRegisterPlace(entity.getBusinessRegisterPlace());
        response.setSupportEmail(entity.getSupportEmail());
        response.setSupportPhone(entity.getSupportPhone());
        response.setImported(entity.isImported());
        return response;
    }

    private static List<TokenUserEntity> toTokenUserEntity(List<TokenUser> users) {
        List<TokenUserEntity> response = new ArrayList<>();
        for (TokenUser user : users) {
            TokenUserEntity entity = new TokenUserEntity();
            entity.setRole(user.getRole());
            entity.setUserId(user.getUserId());
            response.add(entity);
        }
        return response;
    }

    public static List<TokenUser> toTokenUser(List<TokenUserEntity> users) {
        List<TokenUser> response = new ArrayList<>();
        for (TokenUserEntity user : users) {
            TokenUser tokenUser = new TokenUser();
            tokenUser.setRole(user.getRole());
            tokenUser.setUserId(user.getUserId());
            response.add(tokenUser);
        }
        return response;
    }
}
