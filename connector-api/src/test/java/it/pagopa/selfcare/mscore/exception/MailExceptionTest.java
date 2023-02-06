package it.pagopa.selfcare.mscore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class MailExceptionTest {
    /**
     * Method under test: {@link MailException#MailException(Throwable)}
     */
    @Test
    void testConstructor() {
        Throwable throwable = new Throwable();
        MailException actualMailException = new MailException(throwable);
        Throwable cause = actualMailException.getCause();
        assertSame(throwable, cause);
        Throwable[] suppressed = actualMailException.getSuppressed();
        assertEquals(0, suppressed.length);
        assertEquals("java.lang.Throwable", actualMailException.getLocalizedMessage());
        assertEquals("java.lang.Throwable", actualMailException.getMessage());
        assertNull(cause.getLocalizedMessage());
        assertNull(cause.getCause());
        assertNull(cause.getMessage());
        assertSame(suppressed, cause.getSuppressed());
    }
}

