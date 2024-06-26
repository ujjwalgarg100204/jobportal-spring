package com.ujjwalgarg.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ujjwalgarg.jobportal.controller.payload.response.ErrorResponse;
import com.ujjwalgarg.jobportal.exception.EntityAlreadyExistsException;

import ch.qos.logback.core.util.StringUtil;

/**
 * GlobalExceptionHandler
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> validationErrors = new ArrayList<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(err -> {
                    String fieldName = ((FieldError) err).getField();
                    String errorMsg = err.getDefaultMessage();
                    validationErrors.add(String.format(
                            "%s: %s",
                            StringUtil.capitalizeFirstLetter(fieldName),
                            StringUtil.capitalizeFirstLetter(errorMsg)));
                });

        var response = new ErrorResponse(
                validationErrors,
                "Couldn't proceed with request due to validation errors");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex) {
        var response = new ErrorResponse(null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
