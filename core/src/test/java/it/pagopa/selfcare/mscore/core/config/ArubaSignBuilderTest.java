package it.pagopa.selfcare.mscore.core.config;

import it.pagopa.selfcare.mscore.config.ArubaConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ArubaSignBuilder.class})
@ExtendWith(SpringExtension.class)
class ArubaSignBuilderTest {
    @MockBean
    private ArubaConfig arubaConfig;

    @Autowired
    private ArubaSignBuilder arubaSignBuilder;

    /**
     * Method under test: {@link ArubaSignBuilder#getArubaPkcs()}
     */
    @Test
    void testGetArubaPkcs() {

        when(arubaConfig.getServiceUrl()).thenReturn("url");
        when(arubaConfig.getOtpPwd()).thenReturn("test");
        when(arubaConfig.getTypeOtpAuth()).thenReturn("test");
        when(arubaConfig.getUser()).thenReturn("test");
        when(arubaConfig.getDelegatedUser()).thenReturn("test");
        when(arubaConfig.getDelegatedPassword()).thenReturn("test");
        when(arubaConfig.getDelegatedDomain()).thenReturn("test");
        arubaSignBuilder.getArubaPkcs();
    }
}

