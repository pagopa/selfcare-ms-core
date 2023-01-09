package it.pagopa.selfcare.mscore.web.exception;

import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.Problem;
import it.pagopa.selfcare.mscore.model.ProblemError;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.web.util.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleResourceNotFoundException(HttpServletRequest request, ResourceNotFoundException ex) {
        log.error("ResourceNotFoundException Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), "NOT_FOUND", ex.getMessage(), 400, ex.getCode());
        return new ResponseEntity<>(problem, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Problem> handleResourceConflictException(HttpServletRequest request, ResourceConflictException ex) {
        log.error("ResourceNotFoundException Occured --> URL:{}, MESSAGE:{}",request.getRequestURL(),ex.getMessage(),ex);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Problem problem = createProblem(request.getRequestURL().toString(), "CONFLICT", ex.getMessage(), 409, ex.getCode());
        return new ResponseEntity<>(problem, headers, HttpStatus.CONFLICT);
    }

    @Around(value = "@annotation(exceptionMessage)")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(HttpServletRequest request, Exception ex, ExceptionMessage exceptionMessage) {
        log.error("{} Occured --> URL:{}, MESSAGE:{}",ex.getCause(), request.getRequestURL(),exceptionMessage.message().getMessage(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createProblem(request.getRequestURL().toString(), "BAD_REQUEST", exceptionMessage.message().getMessage(), 404, exceptionMessage.message().getCode()));
    }

    private Problem createProblem(String url, String title, String message, Integer status, String code) {
        Problem problem = new Problem();
        problem.setType(url);
        problem.setTitle(title);
        problem.setStatus(status);
        problem.setDetail(message);
        problem.setErrors(createProblemError(message,code));
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
}
