package it.pagopa.selfcare.mscore.connector.rest.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("x-api-key", "xZQloQ0F2y8N7MR9QEhez9M3WO15Ivi9a0yos3HZ");
    }
}
