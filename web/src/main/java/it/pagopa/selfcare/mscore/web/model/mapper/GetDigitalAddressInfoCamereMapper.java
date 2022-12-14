package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.infocamere.InfoCamereBatchRequest;
import it.pagopa.selfcare.mscore.web.model.GetDigitalAddressInfoCamereOKDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GetDigitalAddressInfoCamereMapper {

    public static GetDigitalAddressInfoCamereOKDto toResource(InfoCamereBatchRequest infoCamereBatchRequest) {
        if (infoCamereBatchRequest == null) {
            return null;
        }
        GetDigitalAddressInfoCamereOKDto resource = new GetDigitalAddressInfoCamereOKDto();
        resource.setCorrelationId(infoCamereBatchRequest.getCorrelationId());
        return resource;
    }

}
