package it.pagopa.selfcare.mscore.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceForbiddenExceptionTest {

    @Test
    void testConstructor() {
        assertEquals("Code", (new ResourceForbiddenException("An error occurred", "Code")).getCode());
    }
}

