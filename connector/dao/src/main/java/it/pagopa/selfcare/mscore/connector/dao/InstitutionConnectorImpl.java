package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.GET_INSTITUTION_BILLING_ERROR;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.INSTITUTION_NOT_FOUND;

@Slf4j
@Component
public class InstitutionConnectorImpl implements InstitutionConnector {

    private static final String CURRENT_ONBOARDING = "current.";
    private static final String CURRENT_ONBOARDING_REFER = "$[current]";
    private final InstitutionRepository repository;

    @Autowired
    public InstitutionConnectorImpl(InstitutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Institution save(Institution institution) {
        final InstitutionEntity entity = convertToInstitutionEntity(institution);
        return convertToInstitution(repository.save(entity));
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public Institution findById(String id) {
        return repository.findById(id)
                .map(institution -> {
                    log.info("Founded institution {}", institution.getExternalId());
                    return convertToInstitution(institution);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(INSTITUTION_NOT_FOUND.getMessage(), id, null), INSTITUTION_NOT_FOUND.getCode()));
    }

    @Override
    public void findAndUpdateStatus(String institutionId, String productId, RelationshipState status) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        UpdateDefinition updateDefinition = new Update()
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.status.name()), status)
                .set(constructQuery(CURRENT_ONBOARDING_REFER, Onboarding.Fields.updatedAt.name()), OffsetDateTime.now())
                .filterArray(Criteria.where(CURRENT_ONBOARDING + Onboarding.Fields.productId.name()).is(productId));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, updateDefinition, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public Institution findAndUpdate(String institutionId, Onboarding onboarding, List<GeographicTaxonomies> geographicTaxonomiesList) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.addToSet(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        if (geographicTaxonomiesList != null && !geographicTaxonomiesList.isEmpty()) {
            List<GeoTaxonomyEntity> list = geographicTaxonomiesList.stream().map(geographicTaxonomies -> {
                GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
                entity.setCode(geographicTaxonomies.getCode());
                entity.setDesc(geographicTaxonomies.getDesc());
                return entity;
            }).collect(Collectors.toList());
            list.forEach(geoTaxonomyEntity -> update.addToSet(InstitutionEntity.Fields.geographicTaxonomies.name(), geoTaxonomyEntity));
        }

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        return convertToInstitution(repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class));
    }

    @Override
    public Institution findInstitutionProduct(String externalId, String productId) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)
                .and(constructQuery(Onboarding.Fields.productId.name())).is(productId));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(GET_INSTITUTION_BILLING_ERROR.getMessage(), externalId, productId),
                        GET_INSTITUTION_BILLING_ERROR.getCode()));
    }

    @Override
    public void findAndRemoveOnboarding(String institutionId, Onboarding onboarding) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.id.name()).is(institutionId));
        Update update = new Update();
        update.set(InstitutionEntity.Fields.updatedAt.name(), OffsetDateTime.now());
        if (onboarding != null) {
            update.pull(InstitutionEntity.Fields.onboarding.name(), onboarding);
        }
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(false);
        repository.findAndModify(query, update, findAndModifyOptions, InstitutionEntity.class);
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        return repository.find(Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId)),
                        InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .findFirst();
    }

    @Override
    public List<Institution> findWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        Query query = Query.query(Criteria.where(InstitutionEntity.Fields.externalId.name()).is(externalId))
                .addCriteria(Criteria.where(InstitutionEntity.Fields.onboarding.name())
                        .elemMatch(Criteria.where(Onboarding.Fields.productId.name()).is(productId)
                                .and(Onboarding.Fields.status.name()).in(validRelationshipStates)));

        return repository.find(query, InstitutionEntity.class).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    private String constructQuery(String... variables) {
        StringBuilder builder = new StringBuilder();
        builder.append(InstitutionEntity.Fields.onboarding.name());
        Arrays.stream(variables).forEach(s -> builder.append(".").append(s));
        return builder.toString();
    }

    private Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        if (entity != null) {
            institution.setId(entity.getId());
            institution.setExternalId(entity.getExternalId());
            institution.setDescription(entity.getDescription());
            institution.setInstitutionType(entity.getInstitutionType());
            institution.setIpaCode(entity.getIpaCode());
            institution.setDigitalAddress(entity.getDigitalAddress());
            institution.setAddress(entity.getAddress());
            institution.setZipCode(entity.getZipCode());
            institution.setTaxCode(entity.getTaxCode());
            institution.setOnboarding(toOnboarding(entity.getOnboarding()));
            institution.setDataProtectionOfficer(toDataProtectionOfficer(entity.getDataProtectionOfficer()));
            institution.setPaymentServiceProvider(toPaymentServiceProvider(entity.getPaymentServiceProvider()));
            institution.setAttributes(toAttributes(entity.getAttributes()));
            institution.setGeographicTaxonomies(toGeographicTaxonomies(entity.getGeographicTaxonomies()));
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

    private List<Onboarding> toOnboarding(List<OnboardingEntity> onboarding) {
        List<Onboarding> list = new ArrayList<>();
        for (OnboardingEntity onboardingEntity : onboarding) {
            Onboarding o = new Onboarding();
            o.setProductId(onboardingEntity.getProductId());
            o.setStatus(onboardingEntity.getStatus());
            o.setContract(onboardingEntity.getContract());
            o.setPricingPlan(onboardingEntity.getPricingPlan());
            o.setPremium(onboardingEntity.getPremium());
            o.setBilling(onboardingEntity.getBilling());
            o.setCreatedAt(onboardingEntity.getCreatedAt());
            o.setUpdatedAt(onboardingEntity.getUpdatedAt());
            list.add(o);
        }
        return list;
    }

    private DataProtectionOfficer toDataProtectionOfficer(DataProtectionOfficerEntity dataProtectionOfficer) {
        DataProtectionOfficer data = new DataProtectionOfficer();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    private PaymentServiceProvider toPaymentServiceProvider(PaymentServiceProviderEntity paymentServiceProvider) {
        PaymentServiceProvider provider = new PaymentServiceProvider();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }

    private List<Attributes> toAttributes(List<AttributesEntity> attributes) {
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

    private List<GeographicTaxonomies> toGeographicTaxonomies(List<GeoTaxonomyEntity> geographicTaxonomies) {
        List<GeographicTaxonomies> list = new ArrayList<>();
        for(GeoTaxonomyEntity entity : geographicTaxonomies){
            GeographicTaxonomies geo = new GeographicTaxonomies();
            geo.setDesc(entity.getDesc());
            geo.setCode(entity.getCode());
            list.add(geo);
        }
        return list;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(institution.getId());
        } else {
            entity.setId(UUID.randomUUID().toString());
        }
        entity.setCreatedAt(institution.getCreatedAt());
        entity.setExternalId(institution.getExternalId());
        entity.setDescription(institution.getDescription());
        entity.setIpaCode(institution.getIpaCode());
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

        if(institution.getOnboarding()!=null){
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
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    private List<OnboardingEntity> toOnboardingEntity(List<Onboarding> onboardingList) {
        List<OnboardingEntity> list = new ArrayList<>();
        for (Onboarding onboarding : onboardingList) {
            OnboardingEntity o = new OnboardingEntity();
            o.setProductId(onboarding.getProductId());
            o.setStatus(onboarding.getStatus());
            o.setContract(onboarding.getContract());
            o.setPricingPlan(onboarding.getPricingPlan());
            o.setPremium(onboarding.getPremium());
            o.setBilling(onboarding.getBilling());
            o.setCreatedAt(onboarding.getCreatedAt());
            o.setUpdatedAt(onboarding.getUpdatedAt());
            list.add(o);
        }
        return list;
    }

    private List<GeoTaxonomyEntity> toGeoTaxonomyEntity(List<GeographicTaxonomies> geographicTaxonomies) {
        List<GeoTaxonomyEntity> list = new ArrayList<>();
        for(GeographicTaxonomies geo : geographicTaxonomies){
            GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
            entity.setDesc(geo.getDesc());
            entity.setCode(geo.getCode());
            list.add(entity);
        }
        return list;
    }

    private DataProtectionOfficerEntity toDataProtectionOfficerEntity(DataProtectionOfficer dataProtectionOfficer) {
        DataProtectionOfficerEntity data = new DataProtectionOfficerEntity();
        data.setPec(dataProtectionOfficer.getPec());
        data.setEmail(dataProtectionOfficer.getEmail());
        data.setAddress(dataProtectionOfficer.getAddress());
        return data;
    }

    private PaymentServiceProviderEntity toPaymentServiceProviderEntity(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderEntity provider = new PaymentServiceProviderEntity();
        provider.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        provider.setAbiCode(paymentServiceProvider.getAbiCode());
        provider.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        provider.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
        provider.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        return provider;
    }
}
