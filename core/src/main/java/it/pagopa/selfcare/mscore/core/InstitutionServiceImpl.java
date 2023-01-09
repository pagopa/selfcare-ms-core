package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.GeoTaxonomiesConnector;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.core.exception.InstitutionConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.InstitutionInfoCamere;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.*;

@Slf4j
@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;
    private final GeoTaxonomiesConnector geoTaxonomiesConnector;

    public InstitutionServiceImpl(InstitutionConnector institutionConnector,
                                  PartyRegistryProxyConnector partyRegistryProxyConnector, GeoTaxonomiesConnector geoTaxonomiesConnector) {
        this.institutionConnector = institutionConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.geoTaxonomiesConnector = geoTaxonomiesConnector;
    }

    @Override
    public Institution createInstitutionByExternalId(InstitutionType institutionType, String externalId) {
        log.info("Creating institution having external id {}", externalId);

        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent())
            throw new ResourceConflictException(CREATE_INSTITUTION_CONFLICT.getMessage(), CREATE_INSTITUTION_CONFLICT.getCode());

        Institution newInstitution = new Institution();

        if (InstitutionType.PA.equals(institutionType)) {
            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(externalId);

            log.debug("institution from proxy: {}", institutionProxyInfo);
            log.info("getInstitution {}", institutionProxyInfo.getId());

            CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());
            log.info("category from proxy: {}", categoryProxyInfo);

            newInstitution.setExternalId(externalId);
            newInstitution.setOriginId(institutionProxyInfo.getOriginId());
            newInstitution.setDescription(institutionProxyInfo.getDescription());
            newInstitution.setDigitalAddress(institutionProxyInfo.getDigitalAddress());
            newInstitution.setTaxCode(institutionProxyInfo.getTaxCode());
            newInstitution.setAddress(institutionProxyInfo.getAddress());
            newInstitution.setZipCode(institutionProxyInfo.getZipCode());

        } else if (InstitutionType.PG.equals(institutionType)) {
            InstitutionInfoCamere response = partyRegistryProxyConnector.getInstitutionFromInfoCamereById(externalId);
           /* newInstitution.setExternalId(externalId);
            newInstitution.setOriginId(response.getOriginId());
            newInstitution.setDescription(response.getDescription());
            newInstitution.setDigitalAddress(response.getDigitalAddress());
            newInstitution.setTaxCode(response.getTaxCode());
            newInstitution.setAddress(response.getAddress());
            newInstitution.setZipCode(response.getZipCode());*/
        }

        Institution institution = saveInstitution(newInstitution, externalId);
        log.info("institution created {}", institution.getExternalId());
        return institution;
    }

    @Override
    public Institution createInstitutionRaw(Institution institution, String externalId) {
        Optional<Institution> opt = institutionConnector.findByExternalId(externalId);
        if (opt.isPresent())
            throw new ResourceConflictException(CREATE_INSTITUTION_CONFLICT.getMessage(), CREATE_INSTITUTION_CONFLICT.getCode());

        institution.setOriginId(Optional.ofNullable(institution.getInstitutionType().toString())
                .orElse("SELC_" + externalId));
        institution.setOrigin("SELC");

        Institution saved = saveInstitution(institution, externalId);
        log.info("institution created {}", saved.getExternalId());
        return saved;
    }

    @Override
    public List<Onboarding> retrieveInstitutionProducts(String id, List<String> states) {
        Optional<Institution> optionalInstitution = institutionConnector.findById(id);
        if(optionalInstitution.isPresent())
            return optionalInstitution.get().getOnboarding().stream()
                    .filter(onboarding -> states.contains(onboarding.getStatus().name()))
                    .collect(Collectors.toList());
        else
            throw new ResourceNotFoundException(PRODUCTS_NOT_FOUND_ERROR.getMessage(),PRODUCTS_NOT_FOUND_ERROR.getCode());
    }

    @Override
    public Institution saveInstitution(Institution institution, String externalId) {
        Optional<Institution> alreadyPresent = institutionConnector.findByExternalId(externalId);
        if (alreadyPresent.isPresent()) {
            log.warn("institution with externalId {} already exist with id {}", externalId, alreadyPresent.get().getId());
            throw new InstitutionConflictException();
        }
        return institutionConnector.save(institution);
    }

    @Override
    public List<GeographicTaxonomies> getGeoTaxonomies(String id) {
        Optional<Institution> opt = institutionConnector.findById(id);
        List<GeographicTaxonomies> response = new ArrayList<>();
        if (opt.isPresent()) {
            List<GeographicTaxonomies> geographicTaxonomiesList = opt.get().getGeographicTaxonomies();
            for (GeographicTaxonomies g : geographicTaxonomiesList) {
                response.add(geoTaxonomiesConnector.getExtByCode(g.getCode()));
            }
        }
        return response;
    }
}
