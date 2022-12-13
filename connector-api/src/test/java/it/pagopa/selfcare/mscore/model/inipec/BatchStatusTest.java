package it.pagopa.selfcare.mscore.model.inipec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BatchStatusTest {

    @Test
    void testFromValue() {
        assertThrows(IllegalArgumentException.class, () -> BatchStatus.fromValue("42"));
        assertEquals(BatchStatus.NO_BATCH_ID, BatchStatus.fromValue("NO_BATCH_ID"));
        assertEquals(BatchStatus.NOT_WORKED, BatchStatus.fromValue("NOT_WORKED"));
        assertEquals(BatchStatus.WORKING, BatchStatus.fromValue("WORKING"));
        assertEquals("WORKED", BatchStatus.WORKED.toString());
        assertEquals("ERROR", BatchStatus.ERROR.getValue());

    }
}

