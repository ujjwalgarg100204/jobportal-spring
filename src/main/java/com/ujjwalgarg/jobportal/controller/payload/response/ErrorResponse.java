package com.ujjwalgarg.jobportal.controller.payload.response;

import java.util.List;

/**
 * ErrorResponse
 */
public record ErrorResponse(
        boolean success,
        List<String> validationErrors,
        String message) {

    public ErrorResponse(List<String> validationErrors, String message) {
        this(false, validationErrors, message);
    }

}
