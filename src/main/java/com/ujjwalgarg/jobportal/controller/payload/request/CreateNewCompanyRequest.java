package com.ujjwalgarg.jobportal.controller.payload.request;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * CreateNewCompanyRequest
 */
public record CreateNewCompanyRequest(
        @NotBlank(message = "Name of company is mandatory") String name,
        @Valid Address address) {

    public Company toCompany() {
        address.setId(null);
        return Company.builder()
                .name(name)
                .hasLogo(false)
                .address(address)
                .build();
    }
}
