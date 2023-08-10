package it.pagopa.selfcare.mscore.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClassPathStreamTest {

    @Test
    void testConstructor() {
        assertNull((new ClassPathStream("Path")).getStream());
    }

    @Test
    void testGetReader() {
        assertThrows(NullPointerException.class, () ->(new ClassPathStream("Path")).getReader());
    }


}

