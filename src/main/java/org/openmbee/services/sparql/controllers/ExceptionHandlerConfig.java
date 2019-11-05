package org.openmbee.services.sparql.controllers;

import java.util.HashMap;
import java.util.Map;
import org.apache.jena.query.QueryParseException;
import org.openmbee.services.sparql.exceptions.PermissionException;
import org.openmbee.services.sparql.exceptions.SparqlException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {SparqlException.class})
    protected ResponseEntity<Object> handleSparqlException(SparqlException ex, WebRequest request) {
        if (ex.getResult().isUnsupported()) {
            return handleExceptionInternal(ex, "{\"messages\": [\"Sparql Operation Unsupported\"]}", new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
        }
        Map<String, Object> messages = new HashMap<>();
        messages.put("messages", ex.getResult().getErrors());
        return handleExceptionInternal(ex, messages, new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {QueryParseException.class})
    protected ResponseEntity<Object> handleQueryParseException(QueryParseException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(),
            HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {PermissionException.class})
    protected ResponseEntity<Object> handlePermissioinException(PermissionException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Permission Error", new HttpHeaders(),
            HttpStatus.FORBIDDEN, request);
    }
}
