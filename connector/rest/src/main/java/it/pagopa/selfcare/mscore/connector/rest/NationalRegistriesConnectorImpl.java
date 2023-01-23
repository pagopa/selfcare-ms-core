package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.NationalRegistriesConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressFilter;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressRequest;
import it.pagopa.selfcare.mscore.connector.rest.model.nationalregistries.NationalRegistriesAddressResponse;
import it.pagopa.selfcare.mscore.model.NationalRegistriesProfessionalAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.CREATE_INSTITUTION_NOT_FOUND;

@Slf4j
@Service
public class NationalRegistriesConnectorImpl implements NationalRegistriesConnector {

    private final NationalRegistriesRestClient nationalRegistriesRestClient;

    public NationalRegistriesConnectorImpl(NationalRegistriesRestClient nationalRegistriesRestClient) {
        this.nationalRegistriesRestClient = nationalRegistriesRestClient;
    }

    @Override
    public NationalRegistriesProfessionalAddress getLegalAddress(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = createRequest(taxCode);
        NationalRegistriesAddressResponse response = nationalRegistriesRestClient.getLegalAddress(nationalRegistriesAddressRequest);
        if(response == null || response.getProfessionalAddress() == null){
            throw new ResourceNotFoundException(String.format(CREATE_INSTITUTION_NOT_FOUND.getMessage(), taxCode),CREATE_INSTITUTION_NOT_FOUND.getCode());
        }
        return toNationalRegistriesProfessionalAddress(response);

    }

    private NationalRegistriesProfessionalAddress toNationalRegistriesProfessionalAddress(NationalRegistriesAddressResponse response) {
        NationalRegistriesProfessionalAddress result = new NationalRegistriesProfessionalAddress();
        result.setAddress(response.getProfessionalAddress().getAddress());
        result.setZip(response.getProfessionalAddress().getZip());
        result.setDescription(response.getProfessionalAddress().getDescription());
        result.setMunicipality(response.getProfessionalAddress().getMunicipality());
        result.setProvince(response.getProfessionalAddress().getProvince());
        return result;
    }

    private NationalRegistriesAddressRequest createRequest(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = new NationalRegistriesAddressRequest();
        NationalRegistriesAddressFilter filter = new NationalRegistriesAddressFilter();
        filter.setTaxId(taxCode);
        nationalRegistriesAddressRequest.setFilter(filter);
        return nationalRegistriesAddressRequest;
    }
}
