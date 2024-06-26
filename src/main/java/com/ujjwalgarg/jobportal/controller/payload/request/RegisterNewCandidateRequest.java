package com.ujjwalgarg.jobportal.controller.payload.request;

import com.ujjwalgarg.jobportal.entity.ContactInformation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * RegisterNewCandidateRequest
 */
public record RegisterNewCandidateRequest(
        @NotBlank(message = "Email is required") String email,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "First Name is mandatory") String firstName,
        @NotBlank(message = "Last Name is mandatory") String lastName,
        @NotBlank(message = "Short About is mandatory") String shortAbout,
        @NotNull(message = "Contact Information is mandatory") @Valid ContactInformation contactInformation) {
}
