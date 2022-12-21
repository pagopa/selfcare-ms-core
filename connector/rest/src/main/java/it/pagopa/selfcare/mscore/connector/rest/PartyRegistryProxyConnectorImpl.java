package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.PartyRegistryProxyRestClient;
import it.pagopa.selfcare.mscore.connector.rest.model.ProxyCategoryResponse;
import it.pagopa.selfcare.mscore.connector.rest.model.ProxyInstitutionResponse;
import it.pagopa.selfcare.mscore.model.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PartyRegistryProxyConnectorImpl implements PartyRegistryProxyConnector {

    private final PartyRegistryProxyRestClient restClient;

    public PartyRegistryProxyConnectorImpl(PartyRegistryProxyRestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public InstitutionProxyInfo getInstitutionById(String id) {
        ProxyInstitutionResponse response = restClient.getInstitutionById(id);
        return convertInstitutionProxyInfo(response);
    }

    @Override
    public CategoryProxyInfo getCategory(String origin, String code) {
        ProxyCategoryResponse response = restClient.getCategory(origin, code);
        return convertCategoryProxyInfo(response);
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
