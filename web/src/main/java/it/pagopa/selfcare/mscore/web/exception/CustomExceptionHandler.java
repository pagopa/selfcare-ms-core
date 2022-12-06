package it.pagopa.selfcare.mscore.web.exception;

import it.pagopa.selfcare.mscore.core.model.Problem;
import it.pagopa.selfcare.mscore.core.model.ProblemError;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
        Problem problem = createProblem(request.getRequestURL().toString(), "NOT_FOUND", ex.getMessage(), 400);
        return new ResponseEntity<>(problem, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleException(HttpServletRequest request, Exception ex) {
        log.error("ResourceNotFoundException Occured --> URL:{}, MESSAGE:{}" + request.getRequestURL(),ex.getMessage(),ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createProblem(request.getRequestURL().toString(), "BAD_REQUEST", ex.getMessage(), 404));
    }

    private Problem createProblem(String url, String title, String message, Integer status) {
        Problem problem = new Problem();
        problem.setType(url);
        problem.setTitle(title);
        problem.setStatus(status);
        problem.setDetail(message);
        problem.setErrors(createProblemError());
        return problem;
    }

    private List<ProblemError> createProblemError() {
        //List<ProblemError> list = new ArrayList<>();
        return new ArrayList<>();
    }
}
