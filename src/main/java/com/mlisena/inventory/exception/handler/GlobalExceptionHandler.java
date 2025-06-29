package com.mlisena.inventory.exception.handler;

import com.mlisena.inventory.exception.inventory.InventoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private String getPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String instancePath, String typeUri, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setInstance(URI.create(instancePath));
        problemDetail.setTitle(title != null ? title : status.getReasonPhrase());
        problemDetail.setType(URI.create(typeUri != null ? typeUri : "about:blank"));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String path = getPath(request);
        log.error("Validations errors at {}: {}", path, ex.getMessage());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        ProblemDetail problemDetail = createProblemDetail(
             HttpStatus.BAD_REQUEST,
             "Validation failed for one or more fields",
             path,
             "https://example.com/validation-error",
             "Validation Error"
        );
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(InventoryNotFoundException.class)
    public ProblemDetail handleInventoryNotFoundException(InventoryNotFoundException ex, WebRequest request) {
        String path = getPath(request);
        log.error("Inventory not found at {}: {}", path, ex.getMessage());
        return createProblemDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            path,
            "https://example.com/inventory-not-found",
            "Inventory Not Found"
        );
    }
}
