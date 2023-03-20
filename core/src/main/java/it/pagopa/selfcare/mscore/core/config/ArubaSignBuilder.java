package it.pagopa.selfcare.mscore.core.config;

import it.pagopa.selfcare.commons.connector.soap.aruba.sign.config.ArubaSignConfig;
import it.pagopa.selfcare.commons.connector.soap.aruba.sign.generated.client.Auth;
import it.pagopa.selfcare.commons.connector.soap.aruba.sign.service.ArubaPkcs7HashSignServiceImpl;
import it.pagopa.selfcare.commons.connector.soap.aruba.sign.service.ArubaSignServiceImpl;
import it.pagopa.selfcare.commons.connector.soap.utils.SoapLoggingHandler;
import it.pagopa.selfcare.commons.utils.crypto.service.Pkcs7HashSignService;
import it.pagopa.selfcare.mscore.config.ArubaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ArubaSignBuilder {

    private final ArubaConfig arubaConfig;

    public ArubaSignBuilder(ArubaConfig arubaConfig) {
        this.arubaConfig = arubaConfig;
    }

    @Bean
    public Pkcs7HashSignService getArubaPkcs(){
        return new ArubaPkcs7HashSignServiceImpl(new ArubaSignServiceImpl(buildArubaConfig(), new SoapLoggingHandler()));
    }
    private ArubaSignConfig buildArubaConfig() {
        var config = new ArubaSignConfig();
        config.setBaseUrl(arubaConfig.getServiceUrl());
        config.setConnectTimeoutMs(0);
        config.setRequestTimeoutMs(0);

        var arubaAuth = new Auth();
        arubaAuth.setOtpPwd(arubaConfig.getOtpPwd());
        arubaAuth.setTypeHSM("COSIGN");
        arubaAuth.setTypeOtpAuth(arubaConfig.getTypeOtpAuth());
        arubaAuth.setUser(arubaConfig.getUser());
        arubaAuth.setDelegatedUser(arubaConfig.getDelegatedUser());
        arubaAuth.setDelegatedPassword(arubaConfig.getDelegatedPassword());
        arubaAuth.setDelegatedDomain(arubaConfig.getDelegatedDomain());
        config.setAuth(arubaAuth);

        return config;
    }
}
