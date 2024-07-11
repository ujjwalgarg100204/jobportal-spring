package com.ujjwalgarg.jobportal.controller.payload.auth;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import jakarta.validation.constraints.NotBlank;

@ValidCompanyDetails
public record RegisterNewRecruiterRequest(
    // user fields
    @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Password is required") String password,

    // profile fields
    @NotBlank(message = "First Name is mandatory") String firstName,
    @NotBlank(message = "Last Name is mandatory") String lastName,

    // company fields
    Integer companyId,
    String companyName,
    String companyAddressCity,
    String companyAddressState,
    String companyAddressCountry
) {


}
