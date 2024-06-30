package com.ujjwalgarg.jobportal.controller.payload.request;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.RemoteType;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Job;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * CreateNewJobRequest
 */
public record CreateNewJobRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Description is required") String description,
        String salary,
        @NotNull(message = "Employment type is required") EmploymentType employmentType,
        @NotNull(message = "Remote type is required") RemoteType remoteType,
        @Min(value = 1, message = "No of vacancy must be greater than 0") @NotNull(message = "No of vacancy is required") Integer noOfVacancy,
        @Valid Address address) {

    public Job toJob() {
        return Job.builder()
                .title(title)
                .description(description)
                .salary(salary)
                .employmentType(employmentType)
                .remoteType(remoteType)
                .noOfVacancy(noOfVacancy)
                .hiringComplete(false)
                .address(address)
                .build();
    }

}
