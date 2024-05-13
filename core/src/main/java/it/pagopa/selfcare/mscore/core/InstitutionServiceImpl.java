package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.config.CoreConfig;
import it.pagopa.selfcare.mscore.constant.*;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.mapper.TokenMapper;
import it.pagopa.selfcare.mscore.core.strategy.CreateInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.factory.CreateInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.QueueEvent;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {


    private static final String REQUIRED_INSTITUTION_MESSAGE = "An institution id is required";
    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final CoreConfig coreConfig;
    private final ContractEventNotificationService contractService;
    private final InstitutionMapper institutionMapper;
    private final CreateInstitutionStrategyFactory createInstitutionStrategyFactory;
    private final TokenMapper tokenMapper;

    public InstitutionServiceImpl(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                  InstitutionConnector institutionConnector,
                                  CoreConfig coreConfig,
                                  ContractEventNotificationService contractService,
                                  InstitutionMapper institutionMapper,
                                  CreateInstitutionStrategyFactory createInstitutionStrategyFactory,
                                  TokenMapper tokenMapper) {
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.coreConfig = coreConfig;
        this.contractService = contractService;
        this.institutionMapper = institutionMapper;
        this.createInstitutionStrategyFactory = createInstitutionStrategyFactory;
        this.tokenMapper = tokenMapper;
    }

    @Override
    public List<Onboarding> getOnboardingInstitutionByProductId(String institutionId, String productId) {
        return institutionConnector.findOnboardingByIdAndProductId(institutionId, productId);
    }

    @Override
    public List<Institution> getInstitutionsByProductId(String productId, Integer page, Integer size) {
        return institutionConnector.findInstitutionsByProductId(productId, page, size);
    }

    @Override
    public Institution retrieveInstitutionById(String id) {
        return institutionConnector.findById(id);
    }

    @Override
    public Institution retrieveInstitutionByExternalId(String institutionExternalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(institutionExternalId);
        if (opt.isEmpty()) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_FOUND.getMessage(), "UNDEFINED", institutionExternalId), CustomError.INSTITUTION_NOT_FOUND.getCode());
        }
        log.info("founded institution having externalId: {}", institutionExternalId);
        return opt.get();
    }

    @Override
    public void retrieveInstitutionsWithFilter(String externalId, String productId, List<RelationshipState> validRelationshipStates) {
        List<Institution> list = institutionConnector.findWithFilter(externalId, productId, validRelationshipStates);
        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException(String.format(CustomError.INSTITUTION_NOT_ONBOARDED.getMessage(), externalId, productId),
                    CustomError.INSTITUTION_NOT_ONBOARDED.getCode());
        }
    }

    @Override
    public List<Institution> getInstitutions(String taxCode, String subunitCode, String origin, String originId) {
        if (StringUtils.hasText(taxCode) && (StringUtils.hasText(origin) || StringUtils.hasText(originId))) {
            throw new InvalidRequestException(GenericError.GET_INSTITUTIONS_REQUEST_ERROR.getMessage(), GenericError.GET_INSTITUTIONS_REQUEST_ERROR.getCode());
        }

        if (StringUtils.hasText(taxCode)) {
            return institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
        } else {
            return institutionConnector.findByOriginAndOriginId(origin, originId);
        }

    }

    @Override
    public List<Institution> getInstitutions(String taxCode, String subunitCode) {
        return institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
    }

    @Override
    public Institution createInstitutionFromIpa(String taxCode, InstitutionPaSubunitType subunitType, String subunitCode, List<InstitutionGeographicTaxonomies> geographicTaxonomies, InstitutionType institutionType) {
        CreateInstitutionStrategy institutionStrategy = createInstitutionStrategyFactory.createInstitutionStrategyIpa();
        return institutionStrategy.createInstitution(CreateInstitutionStrategyInput.builder()
                .taxCode(taxCode)
                .subunitCode(subunitCode)
                .subunitType(subunitType)
                .geographicTaxonomies(geographicTaxonomies)
                .institutionType(institutionType)
                .build());
    }

    @Override
    public Institution createInstitutionFromPda(Institution institution, String injectionInstitutionType) {
        CreateInstitutionStrategy institutionStrategy = createInstitutionStrategyFactory.createInstitutionStrategyPda(injectionInstitutionType);
        return institutionStrategy.createInstitution(CreateInstitutionStrategyInput.builder()
                .taxCode(institution.getTaxCode())
                .description(institution.getDescription())
                .build());
    }

    @Override
    public Institution createInstitution(Institution institution) {
        return createInstitutionStrategyFactory.createInstitutionStrategy(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(institution.getTaxCode())
                        .subunitCode(institution.getSubunitCode())
                        .subunitType(Optional.ofNullable(institution.getSubunitType())
                                .map(InstitutionPaSubunitType::valueOf)
                                .orElse(null))
                        .build());
    }

    @Override
    public Institution createInstitutionFromAnac(Institution institution) {
        return createInstitutionStrategyFactory.createInstitutionStrategyAnac(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(institution.getTaxCode())
                        .subunitCode(institution.getSubunitCode())
                        .subunitType(Optional.ofNullable(institution.getSubunitType())
                                .map(InstitutionPaSubunitType::valueOf)
                                .orElse(null))
                        .build());
    }

    @Override
    public Institution createInstitutionFromIvass(Institution institution) {
        return createInstitutionStrategyFactory.createInstitutionStrategyIvass(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .ivassCode(institution.getOriginId())
                        .build());
    }

    @Override
    public Institution createInstitutionFromInfocamere(Institution institution) {
        CreateInstitutionStrategy institutionStrategy = createInstitutionStrategyFactory.createInstitutionStrategyInfocamere(institution);
        return institutionStrategy.createInstitution(CreateInstitutionStrategyInput.builder()
                .taxCode(institution.getTaxCode())
                .description(institution.getDescription())
                .build());
    }

    @Override
    public Institution createInstitutionByExternalId(String externalId) {
        checkIfAlreadyExists(externalId);

        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);

        log.debug("institution from proxy: {}", institutionProxyInfo);
        log.info("getInstitution {}", institutionProxyInfo.getId());
        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
        log.info("category from proxy: {}", categoryProxyInfo);

        Institution newInstitution = institutionMapper.fromInstitutionProxyInfo(institutionProxyInfo);

        newInstitution.setExternalId(externalId);
        newInstitution.setOrigin(Origin.IPA.getValue());
        newInstitution.setCreatedAt(OffsetDateTime.now());

        Attributes attributes = new Attributes();
        attributes.setOrigin(categoryProxyInfo.getOrigin());
        attributes.setCode(categoryProxyInfo.getCode());
        attributes.setDescription(categoryProxyInfo.getName());
        newInstitution.setAttributes(List.of(attributes));
        try {
            return institutionConnector.save(newInstitution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    @Override
    public Institution createPnPgInstitution(String taxId, String description) {
        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxId);
        newInstitution.setDescription(description);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());
        newInstitution.setOrigin(Origin.INFOCAMERE.getValue());
        newInstitution.setOriginId(taxId); //TODO: CHE CAMPO USARE
        return institutionConnector.saveOrRetrievePnPg(newInstitution);
    }

    @Override
    public Institution createPgInstitution(String taxId, String description, boolean existsInRegistry, SelfCareUser selfCareUser) {
        checkIfAlreadyExists(taxId);
        Institution newInstitution = new Institution();
        newInstitution.setExternalId(taxId);
        newInstitution.setDescription(description);
        newInstitution.setInstitutionType(InstitutionType.PG);
        newInstitution.setTaxCode(taxId);
        newInstitution.setCreatedAt(OffsetDateTime.now());
        newInstitution.setOriginId(taxId); //TODO: CHE CAMPO USARE

        //TODO: QUANDO SARA' DISPONIBILE IL SERVIZIO PUNTUALE PER CONOSCERE LA RAGIONE SOCIALE DATA LA PIVA SOSTITUIRE LA CHIAMATA
        if (existsInRegistry) {
            if (coreConfig.isInfoCamereEnable()) {
                List<InstitutionByLegal> institutionByLegal = partyRegistryProxyConnector.getInstitutionsByLegal(selfCareUser.getFiscalCode());
                institutionByLegal.stream()
                        .filter(i -> taxId.equalsIgnoreCase(i.getBusinessTaxId()))
                        .findFirst()
                        .ifPresentOrElse(institution -> newInstitution.setDescription(institution.getBusinessName()),
                                () -> {
                                    throw new InvalidRequestException(String.format(CustomError.INSTITUTION_LEGAL_NOT_FOUND.getMessage(), taxId), CustomError.INSTITUTION_LEGAL_NOT_FOUND.getCode());
                                });

                NationalRegistriesProfessionalAddress professionalAddress = partyRegistryProxyConnector.getLegalAddress(taxId);
                if (professionalAddress != null) {
                    newInstitution.setAddress(professionalAddress.getAddress());
                    newInstitution.setZipCode(professionalAddress.getZipCode());
                }
            }
            newInstitution.setOrigin(Origin.INFOCAMERE.getValue());
        } else {
            newInstitution.setOrigin(Origin.ADE.getValue());
        }
        return institutionConnector.save(newInstitution);
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(Institution institution, List<RelationshipState> states) {
        List<Onboarding> onboardingList;
        if (institution.getOnboarding() != null) {
            if (states != null && !states.isEmpty()) {
                onboardingList = institution.getOnboarding().stream()
                        .filter(onboarding -> states.contains(onboarding.getStatus()))
                        .collect(Collectors.toList());
            } else {
                onboardingList = institution.getOnboarding();
            }
            if (!onboardingList.isEmpty()) {
                return onboardingList;
            }
        }
        throw new ResourceNotFoundException(String.format(CustomError.PRODUCTS_NOT_FOUND_ERROR.getMessage(), institution.getId()), CustomError.PRODUCTS_NOT_FOUND_ERROR.getCode());
    }

    @Override
    public Institution retrieveInstitutionProduct(String externalId, String productId) {
        return institutionConnector.findByExternalIdAndProductId(externalId, productId);
    }

    @Override
    public List<GeographicTaxonomies> retrieveInstitutionGeoTaxonomies(Institution institution) {
        log.info("Retrieving geographic taxonomies for institution {}", institution.getId());
        if (institution.getGeographicTaxonomies() != null) {
            List<GeographicTaxonomies> geographicTaxonomies = institution.getGeographicTaxonomies().stream()
                    .map(institutionGeoTax -> retrieveGeoTaxonomies(institutionGeoTax.getCode())
                            .orElseThrow(() -> new MsCoreException(String.format(CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), institutionGeoTax.getCode()), CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getCode())))
                    .collect(Collectors.toList());
            if (!geographicTaxonomies.isEmpty()) {
                return geographicTaxonomies;
            }
        }
        throw new MsCoreException(String.format("GeographicTaonomies for institution %s not found", institution.getId()), "0000");
    }

    @Override
    public Optional<GeographicTaxonomies> retrieveGeoTaxonomies(String code) {
        try {
            return Optional.of(partyRegistryProxyConnector.getExtByCode(code));
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Institution updateInstitution(String institutionId, InstitutionUpdate institutionUpdate, String userId) {
        List<InstitutionGeographicTaxonomies> geographicTaxonomies = retrieveGeographicTaxonomies(institutionUpdate);
        return institutionConnector.findAndUpdate(institutionId, null, geographicTaxonomies, institutionUpdate);
    }

    @Override
    public void updateInstitutionDelegation(String institutionId, boolean delegation) {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setDelegation(delegation);
        institutionConnector.findAndUpdate(institutionId, null, null, institutionUpdate);
    }

    private List<InstitutionGeographicTaxonomies> retrieveGeographicTaxonomies(InstitutionUpdate institutionUpdate) {
        if (institutionUpdate.getGeographicTaxonomies() != null) {
            return institutionUpdate.getGeographicTaxonomies()
                    .stream()
                    .map(geoTaxonomy -> retrieveGeoTaxonomies(geoTaxonomy.getCode())
                            .orElseThrow(() -> new MsCoreException(String.format(CustomError.GEO_TAXONOMY_CODE_NOT_FOUND.getMessage(), geoTaxonomy.getCode()), geoTaxonomy.getCode())))
                    .map(geo -> new InstitutionGeographicTaxonomies(geo.getGeotaxId(), geo.getDescription()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ValidInstitution> retrieveInstitutionByExternalIds(List<ValidInstitution> validInstitutionList, String productId) {
        List<String> institutionsExternalId = institutionConnector.findByExternalIdsAndProductId(validInstitutionList, productId);
        validInstitutionList.removeIf(validInstitution -> institutionsExternalId.contains(validInstitution.getId()));
        return validInstitutionList;
    }

    @Override
    public List<Institution> findInstitutionsByGeoTaxonomies(String geoTaxonomies, SearchMode searchMode) {
        List<String> geo = Arrays.stream(geoTaxonomies.split(","))
                .filter(StringUtils::hasText).toList();
        validateGeoTaxonomies(geo, searchMode);
        return institutionConnector.findByGeotaxonomies(geo, searchMode);
    }

    private void validateGeoTaxonomies(List<String> geoTaxonomies, SearchMode searchMode) {
        if (geoTaxonomies.isEmpty() && searchMode != SearchMode.EXACT) {
            throw new InvalidRequestException("Empty geographic taxonomies filter is valid only when searchMode is exact", "0000");
        }
    }

    @Override
    public List<Institution> findInstitutionsByProductId(String productId) {
        List<Institution> institutions = institutionConnector.findByProductId(productId);
        if (institutions.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Institutions with productId %s not found", productId), "0000");
        }
        return institutions;
    }

    @Override
    public List<Institution> retrieveInstitutionByIds(List<String> ids) {
        return institutionConnector.findAllByIds(ids);
    }

    @Override
    public void updateCreatedAt(String institutionId, String productId, OffsetDateTime createdAt, OffsetDateTime activatedAt) {
        log.trace("updateCreatedAt start");
        log.debug("updateCreatedAt institutionId = {}, productId = {}, createdAt = {}, activatedAt = {}", institutionId, productId, createdAt, activatedAt);
        Assert.hasText(institutionId, "An institution ID is required.");
        Assert.hasText(productId, "A product ID is required.");
        Assert.notNull(createdAt, "A createdAt date is required.");

        Institution updatedInstitution = institutionConnector.updateOnboardedProductCreatedAt(institutionId, productId, createdAt);
        Onboarding onboarding = updatedInstitution.getOnboarding().stream()
                .filter(onboarding1 -> onboarding1.getProductId().equals(productId) && onboarding1.getStatus() == RelationshipState.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CustomError.CONTRACT_NOT_FOUND.getMessage(), institutionId, productId), CustomError.CONTRACT_NOT_FOUND.getCode()));
        Token updatedToken = tokenMapper.toToken(onboarding, institutionId, productId);

        contractService.sendDataLakeNotification(updatedInstitution, updatedToken, QueueEvent.UPDATE);

        log.trace("updateCreatedAt end");
    }

    public void checkIfAlreadyExists(String externalId) {
        log.info("START - check institution {} already exists", externalId);
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent()) {
            throw new ResourceConflictException(String.format(CustomError.CREATE_INSTITUTION_CONFLICT.getMessage(), externalId), CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
        }
    }

    @Override
    public List<Institution> getInstitutionBrokers(String productId, InstitutionType type) {
        return institutionConnector.findBrokers(productId, type);
    }
}
