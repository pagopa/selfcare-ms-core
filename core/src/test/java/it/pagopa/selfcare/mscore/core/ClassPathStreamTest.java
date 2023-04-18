package it.pagopa.selfcare.mscore.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class ClassPathStreamTest {

    @Test
    void testConstructor() {
        assertNull((new ClassPathStream("Path")).getStream());
    }


}

