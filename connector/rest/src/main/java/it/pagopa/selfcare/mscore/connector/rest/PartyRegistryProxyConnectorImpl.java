package it.pagopa.selfcare.mscore.connector.rest;

import feign.FeignException;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.registryproxy.*;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.InstitutionByLegal;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.NationalRegistriesProfessionalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomError.CREATE_INSTITUTION_NOT_FOUND;

@Slf4j
@Service
public class PartyRegistryProxyConnectorImpl implements PartyRegistryProxyConnector {

    private final PartyRegistryProxyRestClient restClient;

    public PartyRegistryProxyConnectorImpl(PartyRegistryProxyRestClient restClient) {
        this.restClient = restClient;
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
}
