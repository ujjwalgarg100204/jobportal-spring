package com.ujjwalgarg.jobportal.controller.payload.response;

import java.util.List;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.Skill;

import lombok.Builder;

/**
 * GetCandidateProfileByIdResponse
 */
@Builder
public record GetCandidateProfileByIdResponse(
        Integer id,
        String firstName,
        String lastName,
        String shortAbout,
        String about,
        Boolean hasProfilePhoto,
        Boolean hasResume,
        String portfolioWebsite,
        EmploymentType preferredEmploymentType,
        Address address,
        ContactInformation contactInformation,
        List<Education> educations,
        List<Interest> interests,
        List<Skill> skills) {

    public static GetCandidateProfileByIdResponse fromCandidateProfile(CandidateProfile profile) {
        return GetCandidateProfileByIdResponse.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .shortAbout(profile.getShortAbout())
                .about(profile.getAbout())
                .hasProfilePhoto(profile.getHasProfilePhoto())
                .hasResume(profile.getHasResume())
                .portfolioWebsite(profile.getPortfolioWebsite())
                .preferredEmploymentType(profile.getPreferredEmploymentType())
                .address(profile.getAddress())
                .contactInformation(profile.getContactInformation())
                .educations(profile.getEducations())
                .interests(profile.getInterests())
                .skills(profile.getSkills())
                .build();
    }
}
