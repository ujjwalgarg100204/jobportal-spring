package com.ujjwalgarg.jobportal.controller.payload.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record NewCandidateRequest(
    @NotBlank(message = "Email is required") @Email String email,
    @NotBlank(message = "Password is required") String password,
    @NotBlank(message = "First Name is mandatory") String firstName,
    @NotBlank(message = "Last Name is mandatory") String lastName,
    @NotBlank(message = "Short About is mandatory") String shortAbout,
    @URL(message = "Invalid URL provided") String twitterHandle,
    @URL(message = "Invalid URL provided") String linkedinHandle,
    @URL(message = "Invalid URL provided") String githubHandle,
    String phone
) {

}
