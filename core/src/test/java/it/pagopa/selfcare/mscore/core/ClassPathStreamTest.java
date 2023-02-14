package it.pagopa.selfcare.mscore.core;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ClassPathStreamTest {

    /**
     * Method under test: {@link ClassPathStream#ClassPathStream(String)}
     */
    @Test
    void testConstructor() {
        assertNull((new ClassPathStream("Path")).getStream());
    }

}

