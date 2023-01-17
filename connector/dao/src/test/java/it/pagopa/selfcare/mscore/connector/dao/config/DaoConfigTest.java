package it.pagopa.selfcare.mscore.connector.dao.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {DaoConfig.class})
@ExtendWith(SpringExtension.class)
class DaoConfigTest {
    @Autowired
    private DaoConfig daoConfig;

    /**
     * Method under test: {@link DaoConfig#customConversions()}
     */
    @Test
    void testCustomConversions() {
        // TODO: Complete this test.
        //   Diffblue AI was unable to find a test

        daoConfig.customConversions();
    }
}

