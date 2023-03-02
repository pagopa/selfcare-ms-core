package it.pagopa.selfcare.mscore.core;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClassPathStreamTest {

    @Test
    void testConstructor() {
        assertNull((new ClassPathStream("Path")).getStream());
    }

}

