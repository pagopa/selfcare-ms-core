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
        ClassPathStreamFactory classPathStreamFactory = new ClassPathStreamFactory();
        assertThrows(RuntimeException.class,
                () -> classPathStreamFactory.getUrl("42https://example.org/example"));
    }
}

