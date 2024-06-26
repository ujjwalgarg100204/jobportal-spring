package com.ujjwalgarg.jobportal.controller.payload.request;

import com.ujjwalgarg.jobportal.entity.Company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RegisterNewRecruiterRequest
 */
public record RegisterNewRecruiterRequest(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "First Name is mandatory") String firstName,
        @NotBlank(message = "Last Name is mandatory") String lastName,
        @NotNull(message = "Company must be defined for a recruiter") @Valid Company company) {
}
