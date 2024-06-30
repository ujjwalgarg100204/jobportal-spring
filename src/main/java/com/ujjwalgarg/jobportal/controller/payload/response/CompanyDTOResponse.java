package com.ujjwalgarg.jobportal.controller.payload.response;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;

/**
 * CompanyDTOResponse
 */
public record CompanyDTOResponse(
        Integer id,
        String name,
        Boolean hasLogo,
        Address address) {

    public static CompanyDTOResponse fromCompany(Company company) {
        return new CompanyDTOResponse(
                company.getId(),
                company.getName(),
                company.getHasLogo(),
                company.getAddress());
    }
}
