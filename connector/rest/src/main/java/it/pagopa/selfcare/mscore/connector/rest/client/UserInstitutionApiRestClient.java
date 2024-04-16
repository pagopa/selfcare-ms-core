package it.pagopa.selfcare.mscore.connector.rest.client;

import it.pagopa.selfcare.user.generated.openapi.v1.api.InstitutionControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${rest-client.user-ms.institution.serviceCode}", url = "${rest-client.user-ms.base-url}")
public interface UserInstitutionApiRestClient extends InstitutionControllerApi {
}
