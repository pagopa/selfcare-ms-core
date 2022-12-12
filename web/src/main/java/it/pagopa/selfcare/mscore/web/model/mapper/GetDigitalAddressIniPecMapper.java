package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.inipec.IniPecBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressIniPECOKDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GetDigitalAddressIniPecMapper {

    public static GetDigitalAddressIniPECOKDto toResource(IniPecBatchRequest iniPecBatchRequest) {
        if (iniPecBatchRequest == null) {
            return null;
        }
        GetDigitalAddressIniPECOKDto resource = new GetDigitalAddressIniPECOKDto();
        resource.setCorrelationId(iniPecBatchRequest.getCorrelationId());
        return resource;
    }

}
