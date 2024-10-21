package com.avenhon.kmdb.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    // Method for getting error during entity fields validation
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponce = new HashMap<>();
        errorResponce.put("errors", errors);
        return errorResponce;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleException(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handling
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleException(BadRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Handling @Valid annotation exception for getting error messages
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();

        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }
}
