package com.ujjwalgarg.jobportal.controller.payload.response;

import java.time.LocalDate;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.RemoteType;
import com.ujjwalgarg.jobportal.entity.Address;

import lombok.Data;

/**
 * GetJobByIdResponse
 */
@Data
public class GetJobByIdResponse {

    private Integer id;
    private String title;
    private String description;
    private String salary;
    private EmploymentType employmentType;
    private RemoteType remoteType;
    private Integer noOfVacancy;
    private LocalDate createdAt;
    private Boolean hiringComplete;
    private GetRecruiterProfileByIdResponse recruiterProfile;
    private Address address;
    private CompanyDTOResponse company;
    private Integer noOfAppliedCandidates;

}
