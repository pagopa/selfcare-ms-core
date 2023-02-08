package it.pagopa.selfcare.mscore.web.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Problem;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class CustomExceptionHandlerTest {

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
                "Parameter Type");

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.CONTINUE,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals("Required request parameter 'Parameter Name' for method parameter type Parameter Type is not present",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter3() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException(
                "InvalidRequestException Occured --> MESSAGE:{}", "InvalidRequestException Occured --> MESSAGE:{}", true);

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.CONTINUE,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals(
                "Required request parameter 'InvalidRequestException Occured --> MESSAGE:{}' for method parameter type"
                        + " InvalidRequestException Occured --> MESSAGE:{} is present but converted to null",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter4() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
                "Parameter Type");

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.SWITCHING_PROTOCOLS,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals(
                "Required request parameter 'Parameter Name' for method parameter type Parameter Type is not present",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter5() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
                "Parameter Type");

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.PROCESSING,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals(
                "Required request parameter 'Parameter Name' for method parameter type Parameter Type is not present",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter6() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
                "Parameter Type");

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.CHECKPOINT,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals(
                "Required request parameter 'Parameter Name' for method parameter type Parameter Type is not present",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMissingServletRequestParameter(MissingServletRequestParameterException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMissingServletRequestParameter7() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("Parameter Name",
                "Parameter Type");

        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMissingServletRequestParameterResult = customExceptionHandler
                .handleMissingServletRequestParameter(ex, httpHeaders, HttpStatus.OK,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMissingServletRequestParameterResult.hasBody());
        assertEquals(1, actualHandleMissingServletRequestParameterResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMissingServletRequestParameterResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getErrors().size());
        assertEquals(
                "Required request parameter 'Parameter Name' for method parameter type Parameter Type is not present",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getDetail());
        assertEquals("MISSING PARAMETER",
                ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMissingServletRequestParameterResult.getBody()).getTitle());
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }


    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid3() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.CONTINUE,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid5() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.SWITCHING_PROTOCOLS,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid6() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.PROCESSING,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid7() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.CHECKPOINT,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid8() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.OK,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid9() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.CREATED,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid10() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.ACCEPTED,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid11() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders,
                        HttpStatus.NON_AUTHORITATIVE_INFORMATION, new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid12() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.NO_CONTENT,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid13() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.RESET_CONTENT,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

    /**
     * Method under test: {@link CustomExceptionHandler#handleMethodArgumentNotValid(MethodArgumentNotValidException, HttpHeaders, HttpStatus, WebRequest)}
     */
    @Test
    void testHandleMethodArgumentNotValid14() {
        CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(new BindException("Target", "Object Name"));
        when(methodArgumentNotValidException.getMessage()).thenReturn("An error occurred");
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<Object> actualHandleMethodArgumentNotValidResult = customExceptionHandler
                .handleMethodArgumentNotValid(methodArgumentNotValidException, httpHeaders, HttpStatus.PARTIAL_CONTENT,
                        new ServletWebRequest(new MockHttpServletRequest()));
        assertTrue(actualHandleMethodArgumentNotValidResult.hasBody());
        assertEquals(1, actualHandleMethodArgumentNotValidResult.getHeaders().size());
        assertEquals(HttpStatus.BAD_REQUEST, actualHandleMethodArgumentNotValidResult.getStatusCode());
        assertEquals(400, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getStatus().intValue());
        assertEquals(1, ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getErrors().size());
        assertEquals("[]", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getDetail());
        assertEquals("INVALID ARGUMENT", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getType());
        assertEquals("BAD_REQUEST", ((Problem) actualHandleMethodArgumentNotValidResult.getBody()).getTitle());
        verify(methodArgumentNotValidException).getBindingResult();
        List<String> getResult = httpHeaders.get(HttpHeaders.CONTENT_TYPE);
        assert getResult != null;
        assertEquals(1, getResult.size());
        assertEquals("application/json", getResult.get(0));
    }

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

