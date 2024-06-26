package com.ujjwalgarg.jobportal.controller.payload.request;

import jakarta.validation.constraints.NotBlank;

/**
 * VerifySessionRequest
 */
public record VerifySessionRequest(@NotBlank String token) {
}
