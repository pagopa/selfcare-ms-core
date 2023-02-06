package it.pagopa.selfcare.mscore.core;

import com.openhtmltopdf.extend.FSStream;
import com.openhtmltopdf.extend.FSStreamFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class ClassPathStreamFactory implements FSStreamFactory {
    @Override
    public FSStream getUrl(String url) {
        URI fullUri = null;
        try {
            fullUri = new URI(url);
            return new ClassPathStream(fullUri.getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
