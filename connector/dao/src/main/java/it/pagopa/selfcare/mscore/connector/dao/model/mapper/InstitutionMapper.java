package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.inner.AdditionalInformationsEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.DataProtectionOfficerEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.GeoTaxonomyEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.PaymentServiceProviderEntity;
import it.pagopa.selfcare.mscore.model.institution.AdditionalInformations;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/** Deprecated: Use InstitutionEntityMapper */
@Deprecated
@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static DataProtectionOfficer toDataProtectionOfficer(DataProtectionOfficerEntity dataProtectionOfficer) {
        DataProtectionOfficer data = new DataProtectionOfficer();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    public static AdditionalInformations toAdditionalInformations(AdditionalInformationsEntity additionalInformations) {
        AdditionalInformations additionalInfo = new AdditionalInformations();
        additionalInfo.setIpa(additionalInformations.isIpa());
        additionalInfo.setIpaCode(additionalInformations.getIpaCode());
        additionalInfo.setAgentOfPublicService(additionalInfo.isAgentOfPublicService());
        additionalInfo.setAgentOfPublicServiceNote(additionalInformations.getAgentOfPublicServiceNote());
        additionalInfo.setBelongRegulatedMarket(additionalInformations.isBelongRegulatedMarket());
        additionalInfo.setRegulatedMarketNote(additionalInformations.getRegulatedMarketNote());
        additionalInfo.setEstablishedByRegulatoryProvision(additionalInformations.isEstablishedByRegulatoryProvision());
        additionalInfo.setEstablishedByRegulatoryProvisionNote(additionalInformations.getEstablishedByRegulatoryProvisionNote());
        additionalInfo.setOtherNote(additionalInformations.getOtherNote());
        return additionalInfo;
    }

    public static PaymentServiceProvider toPaymentServiceProvider(PaymentServiceProviderEntity paymentServiceProvider) {
        PaymentServiceProvider provider = new PaymentServiceProvider();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }

    public static List<GeoTaxonomyEntity> toGeoTaxonomyEntity(List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        List<GeoTaxonomyEntity> list = new ArrayList<>();
        for(InstitutionGeographicTaxonomies geo : geographicTaxonomies){
            GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
            entity.setDesc(geo.getDesc());
            entity.setCode(geo.getCode());
            list.add(entity);
        }
        return list;
    }

    public static DataProtectionOfficerEntity toDataProtectionOfficerEntity(DataProtectionOfficer dataProtectionOfficer) {
        DataProtectionOfficerEntity data = new DataProtectionOfficerEntity();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    public static AdditionalInformationsEntity toAdditionalInformationsEntity(AdditionalInformations additionalInformations) {
        AdditionalInformationsEntity additionalInfo = new AdditionalInformationsEntity();
        additionalInfo.setIpa(additionalInformations.isIpa());
        additionalInfo.setIpaCode(additionalInformations.getIpaCode());
        additionalInfo.setAgentOfPublicService(additionalInfo.isAgentOfPublicService());
        additionalInfo.setAgentOfPublicServiceNote(additionalInformations.getAgentOfPublicServiceNote());
        additionalInfo.setBelongRegulatedMarket(additionalInformations.isBelongRegulatedMarket());
        additionalInfo.setRegulatedMarketNote(additionalInformations.getRegulatedMarketNote());
        additionalInfo.setEstablishedByRegulatoryProvision(additionalInformations.isEstablishedByRegulatoryProvision());
        additionalInfo.setEstablishedByRegulatoryProvisionNote(additionalInformations.getEstablishedByRegulatoryProvisionNote());
        additionalInfo.setOtherNote(additionalInformations.getOtherNote());
        return additionalInfo;
    }

    public static PaymentServiceProviderEntity toPaymentServiceProviderEntity(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderEntity provider = new PaymentServiceProviderEntity();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }
}
