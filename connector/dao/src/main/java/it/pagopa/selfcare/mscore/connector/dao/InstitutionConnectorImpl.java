package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.BillingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.OnboardingEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.OnboardingPremiumEntity;
import it.pagopa.selfcare.mscore.model.Billing;
import it.pagopa.selfcare.mscore.model.Institution;
import it.pagopa.selfcare.mscore.model.Onboarding;
import it.pagopa.selfcare.mscore.model.Premium;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstitutionConnectorImpl implements InstitutionConnector {

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
    public List<Institution> findAll() {
        return repository.findAll().stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public List<Institution> findAll(Institution institution) {
        Example<InstitutionEntity> example = Example.of(convertToInstitutionEntity(institution), UntypedExampleMatcher.matching());
        return repository.findAll(example).stream()
                .map(this::convertToInstitution)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(new ObjectId(id));
    }

    @Override
    public Optional<Institution> findById(String id) {
        return repository.findById(new ObjectId(id)).map(this::convertToInstitution);
    }

    @Override
    public Optional<Institution> findByExternalId(String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);
        List<Institution> result = findAll(institution);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    private Institution convertToInstitution(InstitutionEntity entity) {
        Institution institution = new Institution();
        institution.setId(entity.getId().toString());
        institution.setExternalId(entity.getExternalId());
        institution.setDescription(entity.getDescription());
        institution.setInstitutionType(entity.getInstitutionType());
        institution.setIpaCode(entity.getIpaCode());
        institution.setDigitalAddress(entity.getDigitalAddress());
        institution.setAddress(entity.getAddress());
        institution.setZipCode(entity.getZipCode());
        institution.setTaxCode(entity.getTaxCode());
        if (entity.getBilling() != null) {
            institution.setBilling(convertToBilling(entity.getBilling()));
        }
        if (entity.getOnboarding() != null) {
            institution.setOnboarding(convertToOnboarding(entity.getOnboarding()));
        }
        return institution;
    }

    private Billing convertToBilling(BillingEntity entity) {
        Billing billing = new Billing();
        billing.setVatNumber(entity.getVatNumber());
        billing.setRecipientCode(entity.getRecipientCode());
        billing.setPublicServer(entity.getPublicServices());
        return billing;
    }

    private List<Onboarding> convertToOnboarding(List<OnboardingEntity> entities) {
        return entities.stream().map(this::convertToOnboarding).collect(Collectors.toList());
    }

    private Onboarding convertToOnboarding(OnboardingEntity entity) {
        Onboarding onboarding = new Onboarding();
        onboarding.setProductId(entity.getProductId());
        onboarding.setStatus(entity.getStatus());
        onboarding.setContract(entity.getContract());
        onboarding.setPricingPlan(entity.getPricingPlan());
        if (entity.getPremium() != null) {
            onboarding.setPremium(convertToPremium(entity.getPremium()));
        }
        return onboarding;
    }

    private Premium convertToPremium(OnboardingPremiumEntity entity) {
        Premium premium = new Premium();
        premium.setContract(entity.getContract());
        premium.setStatus(entity.getStatus());
        return premium;
    }

    private InstitutionEntity convertToInstitutionEntity(Institution institution) {
        InstitutionEntity entity = new InstitutionEntity();
        if (institution.getId() != null) {
            entity.setId(new ObjectId(institution.getId()));
        }
        entity.setExternalId(institution.getExternalId());
        entity.setDescription(institution.getDescription());
        entity.setIpaCode(institution.getIpaCode());
        entity.setInstitutionType(institution.getInstitutionType());
        entity.setDigitalAddress(institution.getDigitalAddress());
        entity.setAddress(institution.getAddress());
        entity.setZipCode(institution.getZipCode());
        entity.setTaxCode(institution.getTaxCode());
        if (institution.getBilling() != null) {
            entity.setBilling(convertToBillingEntity(institution.getBilling()));
        }
        if (institution.getOnboarding() != null) {
            entity.setOnboarding(convertToOnboardingEntity(institution.getOnboarding()));
        }
        return entity;
    }

    private BillingEntity convertToBillingEntity(Billing billing) {
        BillingEntity entity = new BillingEntity();
        entity.setVatNumber(billing.getVatNumber());
        entity.setRecipientCode(billing.getRecipientCode());
        entity.setPublicServices(billing.getPublicServer());
        return entity;
    }

    private List<OnboardingEntity> convertToOnboardingEntity(List<Onboarding> onboarding) {
        return onboarding.stream().map(this::convertToOnboardingEntity).collect(Collectors.toList());
    }

    private OnboardingEntity convertToOnboardingEntity(Onboarding onboarding) {
        OnboardingEntity entity = new OnboardingEntity();
        entity.setProductId(onboarding.getProductId());
        entity.setStatus(onboarding.getStatus());
        entity.setContract(onboarding.getContract());
        entity.setPricingPlan(onboarding.getPricingPlan());
        if (onboarding.getPremium() != null) {
            entity.setPremium(convertToOnboardingPremiumEntity(onboarding.getPremium()));
        }
        return entity;
    }

    private OnboardingPremiumEntity convertToOnboardingPremiumEntity(Premium premium) {
        OnboardingPremiumEntity entity = new OnboardingPremiumEntity();
        entity.setStatus(premium.getStatus());
        entity.setContract(premium.getContract());
        return entity;
    }
}
