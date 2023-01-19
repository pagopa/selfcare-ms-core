package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.api.NationalRegistriesConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.NationalRegistriesRestClient;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressFilter;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressRequest;
import it.pagopa.selfcare.mscore.model.nationalregistries.NationalRegistriesAddressResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.pagopa.selfcare.mscore.constant.ErrorEnum.CREATE_INSTITUTION_NOT_FOUND;

@Slf4j
@Service
public class NationalRegistriesConnectorImpl implements NationalRegistriesConnector {

    private final NationalRegistriesRestClient nationalRegistriesRestClient;

    public NationalRegistriesConnectorImpl(NationalRegistriesRestClient nationalRegistriesRestClient) {
        this.nationalRegistriesRestClient = nationalRegistriesRestClient;
    }

    @Override
    public NationalRegistriesAddressResponse getLegalAddress(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = createRequest(taxCode);
        NationalRegistriesAddressResponse response = nationalRegistriesRestClient.getLegalAddress(nationalRegistriesAddressRequest);
        if(response == null || response.getProfessionalAddress() == null){
            throw new ResourceNotFoundException(String.format(CREATE_INSTITUTION_NOT_FOUND.getMessage(), taxCode),CREATE_INSTITUTION_NOT_FOUND.getCode());
        }
        return response;

    }

    private NationalRegistriesAddressRequest createRequest(String taxCode) {
        NationalRegistriesAddressRequest nationalRegistriesAddressRequest = new NationalRegistriesAddressRequest();
        NationalRegistriesAddressFilter filter = new NationalRegistriesAddressFilter();
        filter.setTaxId(taxCode);
        nationalRegistriesAddressRequest.setFilter(filter);
        return nationalRegistriesAddressRequest;
    }
}
