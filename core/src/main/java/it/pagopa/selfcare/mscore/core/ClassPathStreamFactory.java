package it.pagopa.selfcare.mscore.core;

import com.openhtmltopdf.extend.FSStream;
import com.openhtmltopdf.extend.FSStreamFactory;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

import static it.pagopa.selfcare.mscore.constant.GenericError.GENERIC_ERROR;

@Slf4j
public class ClassPathStreamFactory implements FSStreamFactory {
    @Override
    public FSStream getUrl(String url) {
        URI fullUri;
        try {
            fullUri = new URI(url);
            return new ClassPathStream(fullUri.getPath());
        } catch (URISyntaxException e) {
            log.error("URISintaxException in ClassPathStreamFactory: ",e);
            throw new InvalidRequestException(GENERIC_ERROR.getMessage(), GENERIC_ERROR.getCode());
        }
    }
}
