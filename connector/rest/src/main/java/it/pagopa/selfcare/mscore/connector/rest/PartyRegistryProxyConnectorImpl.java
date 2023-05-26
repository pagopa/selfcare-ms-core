package it.pagopa.selfcare.mscore.connector.rest;

import feign.FeignException;
import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.mscore.connector.rest.mapper.AooMapper;
import it.pagopa.selfcare.mscore.connector.rest.mapper.UoMapper;
import it.pagopa.selfcare.mscore.connector.rest.model.geotaxonomy.GeographicTaxonomiesResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.*;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.AreaOrganizzativaOmogenea;
import it.pagopa.selfcare.mscore.model.UnitaOrganizzativa;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomError.CREATE_INSTITUTION_NOT_FOUND;

@Slf4j
@Service
public class PartyRegistryProxyConnectorImpl implements PartyRegistryProxyConnector {

    private final PartyRegistryProxyRestClient restClient;
    private final AooMapper aooMapper;
    private final UoMapper uoMapper;

    public PartyRegistryProxyConnectorImpl(PartyRegistryProxyRestClient restClient, AooMapper aooMapper, UoMapper uoMapper) {
        this.restClient = restClient;
        this.aooMapper = aooMapper;
        this.uoMapper = uoMapper;
    }

    @Override
    public InstitutionProxyInfo getInstitutionById(String id) {
        try {
            ProxyInstitutionResponse response = restClient.getInstitutionById(id);
            if (response == null) {
                throw new ResourceNotFoundException(String.format(CREATE_INSTITUTION_NOT_FOUND.getMessage(), id), CREATE_INSTITUTION_NOT_FOUND.getCode());
            }
            return convertInstitutionProxyInfo(response);
        } catch (FeignException e) {
            throw new MsCoreException(e.getMessage(), String.valueOf(e.status()));
        }
    }

    @Override
    public CategoryProxyInfo getCategory(String origin, String code) {
        try {
            ProxyCategoryResponse response = restClient.getCategory(origin, code);
            return convertCategoryProxyInfo(response);
        } catch (FeignException e) {
            throw new MsCoreException(e.getMessage(), String.valueOf(e.status()));
        }
    }

    @Override
    public List<InstitutionByLegal> getInstitutionsByLegal(String taxId) {
        try {
            InstitutionsByLegalResponse response = restClient.getInstitutionsByLegal(toInstitutionsByLegalRequest(taxId));
            return toInstitutionsByLegalResponse(response);
        } catch (FeignException e) {
            throw new MsCoreException(e.getMessage(), String.valueOf(e.status()));
        }
    }

    @Override
    public NationalRegistriesProfessionalAddress getLegalAddress(String taxId) {
        try {
            return restClient.getLegalAddress(taxId);
        } catch (FeignException e) {
            log.error("LegalAddress not found for taxId {}", taxId);
            throw new MsCoreException(e.getMessage(), String.valueOf(e.status()));
        }
    }

    private List<InstitutionByLegal> toInstitutionsByLegalResponse(InstitutionsByLegalResponse response) {
        List<InstitutionByLegal> list = new ArrayList<>();
        if (response.getBusinesses() != null && !response.getBusinesses().isEmpty()) {
            response.getBusinesses().forEach(institutions -> {
                InstitutionByLegal institutionByLegal = new InstitutionByLegal();
                institutionByLegal.setBusinessName(institutions.getBusinessName());
                institutionByLegal.setBusinessTaxId(institutions.getBusinessTaxId());
                list.add(institutionByLegal);
            });
        }
        return list;
    }

    private InstitutionsByLegalRequest toInstitutionsByLegalRequest(String taxId) {
        InstitutionsByLegalRequest institutions = new InstitutionsByLegalRequest();
        LegalFilter legalFilter = new LegalFilter();
        legalFilter.setLegalTaxId(taxId);
        institutions.setFilter(legalFilter);
        return institutions;
    }

    private InstitutionProxyInfo convertInstitutionProxyInfo(ProxyInstitutionResponse response) {
        InstitutionProxyInfo info = new InstitutionProxyInfo();
        info.setId(response.getId());
        info.setOriginId(response.getOriginId());
        info.setO(response.getO());
        info.setOu(response.getOu());
        info.setAoo(response.getAoo());
        info.setTaxCode(response.getTaxCode());
        info.setCategory(response.getCategory());
        info.setDescription(response.getDescription());
        info.setDigitalAddress(response.getDigitalAddress());
        info.setAddress(response.getAddress());
        info.setZipCode(response.getZipCode());
        info.setOrigin(response.getOrigin());
        info.setIstatCode(response.getIstatCode());
        return info;
    }

    private CategoryProxyInfo convertCategoryProxyInfo(ProxyCategoryResponse response) {
        CategoryProxyInfo info = new CategoryProxyInfo();
        info.setCode(response.getCode());
        info.setName(response.getName());
        info.setKind(response.getKind());
        info.setOrigin(response.getOrigin());
        return info;
    }

    @Override
    public GeographicTaxonomies getExtByCode(String code) {
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByCode code = {}", code);
        Assert.hasText(code, "Code is required");
        GeographicTaxonomiesResponse result = restClient.getExtByCode(code);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getExtByCode result = {}", result);
        return toGeoTaxonomies(result);
    }

    @Override
    public AreaOrganizzativaOmogenea getAooById(String aooId) {
        log.debug("getAooById id = {}", aooId);
        Assert.hasText(aooId, "Code is required");
        AooResponse result = restClient.getAooById(aooId);
        log.debug("getAooById id = {}", aooId);
        return aooMapper.toEntity(result);
    }

    @Override
    public UnitaOrganizzativa getUoById(String uoId) {
        log.debug("getUoById id = {}", uoId);
        Assert.hasText(uoId, "Code is required");
        UoResponse result = restClient.getUoById(uoId);
        log.debug("getUoById id = {}", uoId);
        return uoMapper.toEntity(result);
    }

    private GeographicTaxonomies toGeoTaxonomies(GeographicTaxonomiesResponse result) {
        GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
        geographicTaxonomies.setDescription(result.getDescription());
        geographicTaxonomies.setGeotaxId(result.getGeotaxId());
        geographicTaxonomies.setEnable(result.isEnable());
        geographicTaxonomies.setRegionId(result.getRegionId());
        geographicTaxonomies.setProvinceId(result.getProvinceId());
        geographicTaxonomies.setProvinceAbbreviation(result.getProvinceAbbreviation());
        geographicTaxonomies.setCountry(result.getCountry());
        geographicTaxonomies.setCountryAbbreviation(result.getCountryAbbreviation());
        geographicTaxonomies.setIstatCode(result.getIstatCode());
        return geographicTaxonomies;
    }
}
