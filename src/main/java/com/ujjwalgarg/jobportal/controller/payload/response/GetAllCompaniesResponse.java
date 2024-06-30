package com.ujjwalgarg.jobportal.controller.payload.response;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.Company;

import lombok.Builder;

/**
 * GetAllCompaniesResponse
 */
@Builder
public record GetAllCompaniesResponse(
        Integer id,
        String name,
        Boolean hasLogo,
        Address address,
        String logoUrl) {

    public static GetAllCompaniesResponse fromCompany(Company company, String logoUrl) {
        return GetAllCompaniesResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .hasLogo(company.getHasLogo())
                .address(company.getAddress())
                .logoUrl(logoUrl)
                .build();

    }

}
