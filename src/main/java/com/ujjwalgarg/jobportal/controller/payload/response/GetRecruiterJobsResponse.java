package com.ujjwalgarg.jobportal.controller.payload.response;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.RemoteType;
import com.ujjwalgarg.jobportal.entity.Address;

/**
 * GetRecruiterJobsResponse
 */
public interface GetRecruiterJobsResponse {
    Integer getId();

    String getTitle();

    String getDescription();

    String getSalary();

    EmploymentType getEmploymentType();

    RemoteType getRemoteType();

    Integer getNoOfVacancy();

    LocalDate getCreatedAt();

    Boolean getHiringComplete();

    @Value("#{new com.ujjwalgarg.jobportal.entity.Address(target.addressId, target.addressCity, target.addressState, target.addressCountry)}")
    Address getAddress();

    int getNoOfApplicants();
}
