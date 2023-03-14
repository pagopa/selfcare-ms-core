package it.pagopa.selfcare.mscore.web.exception;

import it.pagopa.selfcare.mscore.constant.GenericError;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.error.Problem;
import it.pagopa.selfcare.mscore.model.error.ProblemError;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull MissingServletRequestParameterException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        GenericError genericError = GenericError.GENERIC_ERROR;
        log.error("InvalidRequestException Occured --> MESSAGE:{}",ex.getMessage(),ex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem("MISSING PARAMETER", BAD_REQUEST, genericError.getMessage(), ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "0000");
        return new ResponseEntity<>(problem, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        GenericError genericError = GenericError.GENERIC_ERROR;
        log.error("InvalidRequestException Occured --> MESSAGE:{}",ex.getMessage(),ex);
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        Problem problem = createProblem("INVALID ARGUMENT", BAD_REQUEST, genericError.getMessage(), errors.toString(), HttpStatus.BAD_REQUEST.value(), "0000");
        return new ResponseEntity<>(problem, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException ex) {
        GenericError genericError = retrieveGenericError(request);
        log.error("ResourceNotFoundException Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), "NOT_FOUND", genericError.getMessage(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), ex.getCode());
        return new ResponseEntity<>(problem, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Problem> handleResourceConflictException(HttpServletRequest request, ResourceConflictException ex) {
        GenericError genericError = retrieveGenericError(request);
        log.error("ResourceConflictException Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), "CONFLICT", genericError.getMessage(), ex.getMessage(), HttpStatus.CONFLICT.value(), ex.getCode());
        return new ResponseEntity<>(problem, headers, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Problem> handleInvalidRequestException(HttpServletRequest request, InvalidRequestException ex) {
        GenericError genericError = retrieveGenericError(request);
        log.error("InvalidRequestException Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), BAD_REQUEST, genericError.getMessage(), ex.getMessage(), HttpStatus.BAD_REQUEST.value(), ex.getCode());
        return new ResponseEntity<>(problem, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MsCoreException.class)
    public ResponseEntity<Problem> handleMsCoreException(HttpServletRequest request, MsCoreException ex) {
        GenericError genericError = retrieveGenericError(request);
        log.error("Exception Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), "FATAL_ERROR", genericError.getMessage(),  ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getCode());
        return new ResponseEntity<>(problem, headers, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Problem> handleException(HttpServletRequest request, RuntimeException ex) {
        GenericError genericError = retrieveGenericError(request);
        log.error("{} Occured --> URL:{}, MESSAGE:{}",ex.getCause(), request.getRequestURL(), genericError.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createProblem(request.getRequestURL().toString(), BAD_REQUEST, genericError.getMessage(), genericError.getMessage(), HttpStatus.BAD_REQUEST.value(), genericError.getCode()));
    }

    private Problem createProblem(String url, String title, String customMessage, String errorMessage, Integer status, String code) {
        Problem problem = new Problem();
        problem.setType(url);
        problem.setTitle(title);
        problem.setStatus(status);
        problem.setDetail(customMessage);
        problem.setErrors(createProblemError(errorMessage,code));
        return problem;
    }

    private List<ProblemError> createProblemError(String message, String code) {
        List<ProblemError> list = new ArrayList<>();
        list.add(ProblemError.builder()
                .code(code)
                .detail(message)
                .build());
        return list;
    }

    private GenericError retrieveGenericError(HttpServletRequest request){
        GenericError genericError = (GenericError) request.getAttribute("errorEnum");
        if(genericError == null){
            genericError = GenericError.GENERIC_ERROR;
        }
        return genericError;
    }
}
