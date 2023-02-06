package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ClassPathStreamFactoryTest {
    /**
     * Method under test: {@link ClassPathStreamFactory#getUrl(String)}
     */
    @Test
    void testGetUrl() {
        assertNull((new ClassPathStreamFactory()).getUrl("https://example.org/example").getStream());
        assertThrows(RuntimeException.class,
                () -> (new ClassPathStreamFactory()).getUrl("42https://example.org/example"));
    }
}

