package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        if (entity != null) {
            institution.setId(entity.getId());
            institution.setExternalId(entity.getExternalId());
            institution.setDescription(entity.getDescription());
            institution.setInstitutionType(entity.getInstitutionType());
            institution.setOriginId(entity.getOriginId());
            institution.setDigitalAddress(entity.getDigitalAddress());
            institution.setAddress(entity.getAddress());
            institution.setZipCode(entity.getZipCode());
            institution.setTaxCode(entity.getTaxCode());
            if (entity.getOnboarding() != null) {
                institution.setOnboarding(toOnboarding(entity.getOnboarding()));
            }
            if (entity.getDataProtectionOfficer() != null) {
                institution.setDataProtectionOfficer(toDataProtectionOfficer(entity.getDataProtectionOfficer()));
            }
            if (entity.getPaymentServiceProvider() != null) {
                institution.setPaymentServiceProvider(toPaymentServiceProvider(entity.getPaymentServiceProvider()));
            }
            if (entity.getAttributes() != null) {
                institution.setAttributes(toAttributes(entity.getAttributes()));
            }
            if (entity.getGeographicTaxonomies() != null) {
                institution.setGeographicTaxonomies(toGeographicTaxonomies(entity.getGeographicTaxonomies()));
            }
            institution.setCreatedAt(entity.getCreatedAt());
            institution.setUpdatedAt(entity.getUpdatedAt());

            institution.setRea(entity.getRea());
            institution.setShareCapital(entity.getShareCapital());
            institution.setBusinessRegisterPlace(entity.getBusinessRegisterPlace());
            institution.setSupportEmail(entity.getSupportEmail());
            institution.setSupportPhone(entity.getSupportPhone());
            institution.setImported(entity.isImported());
        }
        return institution;
    }

    private static List<Onboarding> toOnboarding(List<OnboardingEntity> onboarding) {
        List<Onboarding> list = new ArrayList<>();
        for (OnboardingEntity onboardingEntity : onboarding) {
            Onboarding o = new Onboarding();
            o.setProductId(onboardingEntity.getProductId());
            o.setStatus(onboardingEntity.getStatus());
            o.setContract(onboardingEntity.getContract());
            o.setPricingPlan(onboardingEntity.getPricingPlan());
            if (onboardingEntity.getBilling() != null) {
                o.setBilling(toBilling(onboardingEntity.getBilling()));
            }
            o.setCreatedAt(onboardingEntity.getCreatedAt());
            o.setUpdatedAt(onboardingEntity.getUpdatedAt());
            list.add(o);
        }
        return list;
    }

    private static DataProtectionOfficer toDataProtectionOfficer(DataProtectionOfficerEntity dataProtectionOfficer) {
        DataProtectionOfficer data = new DataProtectionOfficer();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    private static PaymentServiceProvider toPaymentServiceProvider(PaymentServiceProviderEntity paymentServiceProvider) {
        PaymentServiceProvider provider = new PaymentServiceProvider();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }

    private static List<Attributes> toAttributes(List<AttributesEntity> attributes) {
        List<Attributes> list = new ArrayList<>();
        for(AttributesEntity attributesEntity : attributes){
            Attributes att = new Attributes();
            att.setDescription(attributesEntity.getDescription());
            att.setOrigin(attributesEntity.getOrigin());
            att.setCode(attributesEntity.getCode());
            list.add(att);
        }
        return list;
    }

    private static List<AttributesEntity> toAttributesEntity(List<Attributes> attributes) {
        List<AttributesEntity> list = new ArrayList<>();
        for(Attributes attribute : attributes){
            AttributesEntity entity = new AttributesEntity();
            entity.setDescription(attribute.getDescription());
            entity.setOrigin(attribute.getOrigin());
            entity.setCode(attribute.getCode());
            list.add(entity);
        }
        return list;
    }

    private static List<GeographicTaxonomies> toGeographicTaxonomies(List<GeoTaxonomyEntity> geographicTaxonomies) {
        List<GeographicTaxonomies> list = new ArrayList<>();
        for(GeoTaxonomyEntity entity : geographicTaxonomies){
            GeographicTaxonomies geo = new GeographicTaxonomies();
            geo.setDesc(entity.getDesc());
            geo.setCode(entity.getCode());
            list.add(geo);
        }
        return list;
    }

    public static InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(institution.getId());
        } else {
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setCreatedAt(institution.getCreatedAt());
        entity.setExternalId(institution.getExternalId());
        entity.setDescription(institution.getDescription());
        entity.setOrigin(institution.getOrigin());
        entity.setOriginId(institution.getOriginId());
        entity.setInstitutionType(institution.getInstitutionType());
        entity.setDigitalAddress(institution.getDigitalAddress());
        entity.setAddress(institution.getAddress());
        entity.setZipCode(institution.getZipCode());
        entity.setTaxCode(institution.getTaxCode());
        entity.setRea(institution.getRea());
        entity.setShareCapital(institution.getShareCapital());
        entity.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        entity.setSupportEmail(institution.getSupportEmail());
        entity.setSupportPhone(institution.getSupportPhone());
        entity.setImported(institution.isImported());

        if (institution.getOnboarding() != null) {
            entity.setOnboarding(toOnboardingEntity(institution.getOnboarding()));
        }
        if (institution.getGeographicTaxonomies() != null) {
            entity.setGeographicTaxonomies(toGeoTaxonomyEntity(institution.getGeographicTaxonomies()));
        }
        if (institution.getDataProtectionOfficer() != null) {
            entity.setDataProtectionOfficer(toDataProtectionOfficerEntity(institution.getDataProtectionOfficer()));
        }
        if (institution.getPaymentServiceProvider() != null) {
            entity.setPaymentServiceProvider(toPaymentServiceProviderEntity(institution.getPaymentServiceProvider()));
        }
        if( institution.getAttributes() != null){
            entity.setAttributes(toAttributesEntity(institution.getAttributes()));
        }
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    private static List<OnboardingEntity> toOnboardingEntity(List<Onboarding> onboardingList) {
        List<OnboardingEntity> list = new ArrayList<>();
        for (Onboarding onboarding : onboardingList) {
            OnboardingEntity o = new OnboardingEntity();
            o.setProductId(onboarding.getProductId());
            o.setTokenId(onboarding.getTokenId());
            o.setStatus(onboarding.getStatus());
            o.setContract(onboarding.getContract());
            o.setPricingPlan(onboarding.getPricingPlan());
            if (onboarding.getBilling() != null) {
                o.setBilling(toBillingEntity(onboarding.getBilling()));
            }
            o.setCreatedAt(onboarding.getCreatedAt());
            o.setUpdatedAt(onboarding.getUpdatedAt());
            o.setClosedAt(onboarding.getClosedAt());
            list.add(o);
        }
        return list;
    }

    private static List<GeoTaxonomyEntity> toGeoTaxonomyEntity(List<GeographicTaxonomies> geographicTaxonomies) {
        List<GeoTaxonomyEntity> list = new ArrayList<>();
        for(GeographicTaxonomies geo : geographicTaxonomies){
            GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
            entity.setDesc(geo.getDesc());
            entity.setCode(geo.getCode());
            list.add(entity);
        }
        return list;
    }

    private static DataProtectionOfficerEntity toDataProtectionOfficerEntity(DataProtectionOfficer dataProtectionOfficer) {
        DataProtectionOfficerEntity data = new DataProtectionOfficerEntity();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    private static PaymentServiceProviderEntity toPaymentServiceProviderEntity(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderEntity provider = new PaymentServiceProviderEntity();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }

    private static Billing toBilling(BillingEntity billing) {
        Billing response = new Billing();
        response.setPublicServices(billing.isPublicServices());
        response.setVatNumber(billing.getVatNumber());
        response.setRecipientCode(billing.getRecipientCode());
        return response;
    }

    private static BillingEntity toBillingEntity(Billing billing) {
        BillingEntity response = new BillingEntity();
        response.setPublicServices(billing.isPublicServices());
        response.setVatNumber(billing.getVatNumber());
        response.setRecipientCode(billing.getRecipientCode());
        return response;
    }
}
