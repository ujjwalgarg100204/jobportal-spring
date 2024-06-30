package com.ujjwalgarg.jobportal.controller.payload.request;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * UpdateCompanyRequest
 */
public record UpdateCompanyRequest(
        @NotNull(message = "Id of company is mandatory") Integer id,
        @NotBlank(message = "Name of company is mandatory") String name,
        @Valid Address address) {

    public Company toCompany() {
        return Company.builder()
                .id(id)
                .name(name)
                .address(address)
                .build();
    }
}
