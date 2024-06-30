package com.ujjwalgarg.jobportal.controller.payload.request;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateRecruiterProfileRequest
 */
public record UpdateRecruiterProfileRequest(
        @NotNull(message = "Profile Id is mandatory") Integer id,
        @NotBlank(message = "First Name is mandatory") String firstName,
        @NotBlank(message = "Last Name is mandatory") String lastName,
        @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000") String about,
        @Valid Address address,
        @Valid ContactInformation contactInformation,
        List<@Valid Education> educations,
        List<@Valid Interest> interests,
        @Valid @NotNull(message = "Company must be defined for a recruiter") Company company) {

    public RecruiterProfile toRecruiterProfile() {
        return RecruiterProfile.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .about(about)
                .address(address)
                .contactInformation(contactInformation)
                .educations(educations)
                .interests(interests)
                .company(company)
                .build();
    }
}
