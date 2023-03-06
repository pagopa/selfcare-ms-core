package it.pagopa.selfcare.mscore.web.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaginationUtilsTest {
    /**
     * Method under test: {@link PaginationUtils#paginate(List, Integer, Integer)}
     */
    @Test
    void testPaginate() {
        assertTrue(PaginationUtils.paginate(new ArrayList<>(), 3, 10).isEmpty());
        assertThrows(IllegalArgumentException.class, () -> PaginationUtils.paginate(new ArrayList<>(), -1, 10));
        assertThrows(IllegalArgumentException.class, () -> PaginationUtils.paginate(new ArrayList<>(), 3, -1));
    }


    /**
     * Method under test: {@link PaginationUtils#paginate(List, Integer, Integer)}
     */
    @Test
    void testPaginate3() {
        ArrayList<Object> objectList = new ArrayList<>();
        objectList.add("42");
        assertTrue(PaginationUtils.paginate(objectList, 0, 10).isEmpty());
    }
}

