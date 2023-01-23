package it.pagopa.selfcare.mscore.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Problem;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

class CustomExceptionHandlerTest {

    /**
     * Method under test: {@link CustomExceptionHandler#handleResourceNotFoundException(HttpServletRequest, ResourceNotFoundException)}
     */
    @Test
    void testHandleResourceNotFoundException() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<Problem> actualHandleResourceNotFoundExceptionResult = customExceptionHandler
                .handleResourceNotFoundException(request, new ResourceNotFoundException("An error occurred", "Code"));
        assertTrue(actualHandleResourceNotFoundExceptionResult.hasBody());
        assertEquals(1, actualHandleResourceNotFoundExceptionResult.getHeaders().size());
        assertEquals(HttpStatus.NOT_FOUND, actualHandleResourceNotFoundExceptionResult.getStatusCode());
        Problem body = actualHandleResourceNotFoundExceptionResult.getBody();
        Assertions.assertNotNull(body);
        if (body.getStatus() != null && body.getErrors() != null) {
            assertEquals(404, body.getStatus().intValue());
            assertEquals(1, body.getErrors().size());
        }
        assertEquals("An error occurred", body.getDetail());
        assertEquals("NOT_FOUND", body.getTitle());
        assertEquals("http://localhost", body.getType());
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleResourceConflictException(HttpServletRequest, ResourceConflictException)}
     */
    @Test
    void testHandleResourceConflictException() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<Problem> actualHandleResourceConflictExceptionResult = customExceptionHandler
                .handleResourceConflictException(request, new ResourceConflictException("An error occurred", "Code"));
        assertTrue(actualHandleResourceConflictExceptionResult.hasBody());
        assertEquals(1, actualHandleResourceConflictExceptionResult.getHeaders().size());
        assertEquals(HttpStatus.CONFLICT, actualHandleResourceConflictExceptionResult.getStatusCode());
        Problem body = actualHandleResourceConflictExceptionResult.getBody();
        Assertions.assertNotNull(body);
        if (body.getStatus() != null && body.getErrors() != null) {
            assertEquals(409, body.getStatus().intValue());
            assertEquals(1, body.getErrors().size());
        }
        assertEquals("An error occurred", body.getDetail());
        assertEquals("CONFLICT", body.getTitle());
        assertEquals("http://localhost", body.getType());
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleInvalidRequestException(HttpServletRequest, InvalidRequestException)}
     */
    @Test
    void testHandleInvalidRequestException() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ResponseEntity<Problem> actualHandleInvalidRequestExceptionResult = customExceptionHandler
                .handleInvalidRequestException(request, new InvalidRequestException("An error occurred", "Code"));
        assertTrue(actualHandleInvalidRequestExceptionResult.hasBody());
        assertEquals(1, actualHandleInvalidRequestExceptionResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleInvalidRequestExceptionResult.getStatusCode());
        Problem body = actualHandleInvalidRequestExceptionResult.getBody();
        Assertions.assertNotNull(body);
        assertEquals(400, body.getStatus().intValue());
        assertEquals(1, body.getErrors().size());
        assertEquals("An error occurred", body.getDetail());
        assertEquals("BAD_REQUEST", body.getTitle());
        assertEquals("http://localhost", body.getType());
    }

    /**
     * Method under test:
     */
    @Test
    void testHandleException() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RuntimeException ex = new RuntimeException();
        ResponseEntity<Problem> actualHandleExceptionResult = customExceptionHandler.handleException(request, ex);
        assertTrue(actualHandleExceptionResult.hasBody());
        assertTrue(actualHandleExceptionResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleExceptionResult.getStatusCode());
        Problem body = actualHandleExceptionResult.getBody();
        Assertions.assertNotNull(body);
        if (body.getStatus() != null && body.getErrors() != null) {
            assertEquals(400, body.getStatus().intValue());
            assertEquals(1, body.getErrors().size());
            assertEquals("Generic Error", body.getDetail());
            assertEquals("BAD_REQUEST", body.getTitle());
            assertEquals("http://localhost", body.getType());
        }

    }
}

