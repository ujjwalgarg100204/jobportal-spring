package com.ujjwalgarg.jobportal.controller.payload.request;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.WorkAuthorization;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.Skill;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateCandidateProfileRequest
 */
public record UpdateCandidateProfileRequest(
        @NotBlank(message = "First Name is mandatory") String firstName,
        @NotBlank(message = "Last Name is mandatory") String lastName,
        @NotBlank(message = "Short About is mandatory") String shortAbout,
        @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000") String about,
        @URL String portfolioWebsite,
        WorkAuthorization workAuthorization,
        EmploymentType preferredEmploymentType,
        @Valid Address address,
        @Valid @NotNull(message = "Contact Information is mandatory") ContactInformation contactInformation,
        List<@Valid Education> educations,
        List<@Valid Interest> interests,
        List<@Valid Skill> skills) {

    public CandidateProfile toCandidateProfile() {
        return CandidateProfile.builder()
                .firstName(firstName)
                .lastName(lastName)
                .shortAbout(shortAbout)
                .about(about)
                .portfolioWebsite(portfolioWebsite)
                .workAuthorization(workAuthorization)
                .preferredEmploymentType(preferredEmploymentType)
                .address(address)
                .contactInformation(contactInformation)
                .educations(educations)
                .interests(interests)
                .skills(skills)
                .build();
    }
}
