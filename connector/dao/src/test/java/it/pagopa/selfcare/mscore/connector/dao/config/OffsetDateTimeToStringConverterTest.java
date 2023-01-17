package it.pagopa.selfcare.mscore.connector.dao.config;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class OffsetDateTimeToStringConverterTest {
    /**
     * Method under test: {@link OffsetDateTimeToStringConverter#convert(OffsetDateTime)}
     */
    @Test
    void testConvert() {
        String offsetDateTime = new OffsetDateTimeToStringConverter().convert(OffsetDateTime.now());
        Assertions.assertDoesNotThrow(() -> offsetDateTime);
    }
}

