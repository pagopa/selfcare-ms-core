package it.pagopa.selfcare.mscore.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class FileDownloadExceptionTest {
    /**
     * Method under test: {@link FileDownloadException#FileDownloadException(Throwable)}
     */
    @Test
    void testConstructor() {
        Throwable throwable = new Throwable();
        FileDownloadException actualFileDownloadException = new FileDownloadException(throwable);
        Throwable cause = actualFileDownloadException.getCause();
        assertSame(throwable, cause);
        Throwable[] suppressed = actualFileDownloadException.getSuppressed();
        assertEquals(0, suppressed.length);
        assertEquals("java.lang.Throwable", actualFileDownloadException.getLocalizedMessage());
        assertEquals("java.lang.Throwable", actualFileDownloadException.getMessage());
        assertNull(cause.getLocalizedMessage());
        assertNull(cause.getCause());
        assertNull(cause.getMessage());
        assertSame(suppressed, cause.getSuppressed());
    }
}

