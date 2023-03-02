package it.pagopa.selfcare.mscore.core.config;

import it.pagopa.selfcare.mscore.config.ArubaConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ArubaSignBuilderTest {

    @Mock
    private ArubaConfig arubaConfig;

    @InjectMocks
    private ArubaSignBuilder arubaSignBuilder;

    @Test
    void testGetArubaPkcs() {
        when(arubaConfig.getServiceUrl()).thenReturn("serviceUrl");
        when(arubaConfig.getOtpPwd()).thenReturn("serviceUrl");
        when(arubaConfig.getTypeOtpAuth()).thenReturn("serviceUrl");
        when(arubaConfig.getUser()).thenReturn("serviceUrl");
        when(arubaConfig.getDelegatedUser()).thenReturn("serviceUrl");
        when(arubaConfig.getDelegatedPassword()).thenReturn("serviceUrl");
        when(arubaConfig.getDelegatedDomain()).thenReturn("serviceUrl");

        arubaSignBuilder.getArubaPkcs();
    }
}

