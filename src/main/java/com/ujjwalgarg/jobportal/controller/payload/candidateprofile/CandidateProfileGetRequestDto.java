package com.ujjwalgarg.jobportal.controller.payload.candidateprofile;


import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.WorkAuthorization;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import java.util.List;
import lombok.Builder;

@Builder
public record CandidateProfileGetRequestDto(
    Integer id,
    String firstName,
    String lastName,
    String shortAbout,
    String about,
    String profilePhotoUrl,
    String resumeUrl,
    String portfolioWebsite,
    WorkAuthorization workAuthorization,
    EmploymentType preferredEmploymentType,
    Address address,
    ContactInformation contactInformation,
    List<Education> educations,
    List<Interest> interests,
    List<SkillGetRequestDto> skills
) {

}
