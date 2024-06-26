package com.ujjwalgarg.jobportal.controller.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest
 */
public record LoginRequest(
        @NotBlank(message = "Email is mandatory") @Email String email,
        @NotBlank(message = "Password is mandatory") String password) {
}
