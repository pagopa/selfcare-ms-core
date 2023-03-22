package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        if (entity != null) {
            institution.setId(entity.getId());
            institution.setExternalId(entity.getExternalId());
            institution.setOrigin(entity.getOrigin());
            institution.setOriginId(entity.getOriginId());
            institution.setDescription(entity.getDescription());
            institution.setInstitutionType(entity.getInstitutionType());
            institution.setDigitalAddress(entity.getDigitalAddress());
            institution.setAddress(entity.getAddress());
            institution.setZipCode(entity.getZipCode());
            institution.setTaxCode(entity.getTaxCode());
            if(institution.getBilling() != null){
                institution.setBilling(toBilling(entity.getBilling()));
            }
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
                institution.setGeographicTaxonomies(toInstitutionGeographicTaxonomies(entity.getGeographicTaxonomies()));
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
        }else{
            entity.setGeographicTaxonomies(Collections.emptyList());
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

    public static List<Onboarding> toOnboarding(List<OnboardingEntity> onboarding) {
        List<Onboarding> list = new ArrayList<>();
        if (onboarding != null) {
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
        }
        return list;
    }

    public static DataProtectionOfficer toDataProtectionOfficer(DataProtectionOfficerEntity dataProtectionOfficer) {
        DataProtectionOfficer data = new DataProtectionOfficer();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
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

    public static List<InstitutionGeographicTaxonomies> toInstitutionGeographicTaxonomies(List<GeoTaxonomyEntity> geographicTaxonomies) {
        List<InstitutionGeographicTaxonomies> list = new ArrayList<>();
        if (geographicTaxonomies != null) {
            for (GeoTaxonomyEntity entity : geographicTaxonomies) {
                InstitutionGeographicTaxonomies geo = new InstitutionGeographicTaxonomies();
                geo.setDesc(entity.getDesc());
                geo.setCode(entity.getCode());
                list.add(geo);
            }
        }
        return list;
    }

    private static Billing toBilling(BillingEntity billing) {
        Billing response = new Billing();
        response.setPublicServices(billing.isPublicServices());
        response.setVatNumber(billing.getVatNumber());
        response.setRecipientCode(billing.getRecipientCode());
        return response;
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

    public static PaymentServiceProviderEntity toPaymentServiceProviderEntity(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderEntity provider = new PaymentServiceProviderEntity();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }

    private static BillingEntity toBillingEntity(Billing billing) {
        BillingEntity response = new BillingEntity();
        response.setPublicServices(billing.isPublicServices());
        response.setVatNumber(billing.getVatNumber());
        response.setRecipientCode(billing.getRecipientCode());
        return response;
    }

    public static void addGeographicTaxonomies(InstitutionUpdate institutionUpdate, Update update) {
        if (institutionUpdate.getGeographicTaxonomies() != null && !institutionUpdate.getGeographicTaxonomies().isEmpty()) {
            List<GeoTaxonomyEntity> list = institutionUpdate.getGeographicTaxonomies().stream().map(geographicTaxonomies -> {
                GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
                entity.setCode(geographicTaxonomies.getCode());
                entity.setDesc(geographicTaxonomies.getDesc());
                return entity;
            }).collect(Collectors.toList());
            list.forEach(geoTaxonomyEntity -> update.addToSet(InstitutionEntity.Fields.geographicTaxonomies.name(), geoTaxonomyEntity));
        }
    }
    public static Map<String, Object> getNotNullField(InstitutionUpdate institutionUpdate) {
        Map<String, Object> response = new HashMap<>();
        response.put(InstitutionUpdate.Fields.institutionType.name(), institutionUpdate.getInstitutionType().name());
        response.put(InstitutionUpdate.Fields.description.name(), institutionUpdate.getDescription());
        response.put(InstitutionUpdate.Fields.digitalAddress.name(), institutionUpdate.getDigitalAddress());
        response.put(InstitutionUpdate.Fields.address.name(), institutionUpdate.getAddress());
        response.put(InstitutionUpdate.Fields.taxCode.name(), institutionUpdate.getTaxCode());
        response.put(InstitutionUpdate.Fields.zipCode.name(), institutionUpdate.getZipCode());
        response.put(InstitutionUpdate.Fields.rea.name(), institutionUpdate.getRea());
        response.put(InstitutionUpdate.Fields.shareCapital.name(), institutionUpdate.getShareCapital());
        response.put(InstitutionUpdate.Fields.businessRegisterPlace.name(), institutionUpdate.getBusinessRegisterPlace());
        response.put(InstitutionUpdate.Fields.supportEmail.name(), institutionUpdate.getSupportEmail());
        response.put(InstitutionUpdate.Fields.supportPhone.name(), institutionUpdate.getSupportPhone());
        response.put(InstitutionUpdate.Fields.imported.name(), institutionUpdate.isImported());

        if(institutionUpdate.getPaymentServiceProvider() != null) {
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.abiCode.name()),
                    institutionUpdate.getPaymentServiceProvider().getAbiCode());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.businessRegisterNumber.name()),
                    institutionUpdate.getPaymentServiceProvider().getBusinessRegisterNumber());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.legalRegisterNumber.name()),
                    institutionUpdate.getPaymentServiceProvider().getLegalRegisterNumber());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.legalRegisterName.name()),
                    institutionUpdate.getPaymentServiceProvider().getLegalRegisterName());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.vatNumberGroup.name()),
                    institutionUpdate.getPaymentServiceProvider().isVatNumberGroup());
        }

        if(institutionUpdate.getDataProtectionOfficer() != null){
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.pec.name()),
                    institutionUpdate.getDataProtectionOfficer().getPec());
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.address.name()),
                    institutionUpdate.getDataProtectionOfficer().getAddress());
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.email.name()),
                    institutionUpdate.getDataProtectionOfficer().getEmail());
        }
        response.put(InstitutionUpdate.Fields.dataProtectionOfficer.name(), institutionUpdate.getDataProtectionOfficer());

        response.values().removeIf(Objects::isNull);
        return response;
    }

    private static String constructProtectionOfficerInnerField(String name) {
        return InstitutionUpdate.Fields.dataProtectionOfficer.name() + "." + name;
    }

    private static String constructPaymentInnerField(String name) {
        return InstitutionUpdate.Fields.businessRegisterPlace.name() + "." + name;
    }
}
