package com.ujjwalgarg.jobportal.controller.payload.response;

import java.util.List;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.WorkAuthorization;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;

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
        WorkAuthorization workAuthorization,
        EmploymentType preferredEmploymentType,
        String profilePhotoUrl,
        Address address,
        ContactInformation contactInformation,
        List<Education> educations,
        List<Interest> interests,
        List<GetCandidateProfileByIdSkillResponse> skills) {

    public static GetCandidateProfileByIdResponse fromCandidateProfile(CandidateProfile profile,
            String profilePhotoUrl) {
        return GetCandidateProfileByIdResponse.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .shortAbout(profile.getShortAbout())
                .about(profile.getAbout())
                .hasProfilePhoto(profile.getHasProfilePhoto())
                .hasResume(profile.getHasResume())
                .workAuthorization(profile.getWorkAuthorization())
                .profilePhotoUrl(profilePhotoUrl)
                .portfolioWebsite(profile.getPortfolioWebsite())
                .preferredEmploymentType(profile.getPreferredEmploymentType())
                .address(profile.getAddress())
                .contactInformation(profile.getContactInformation())
                .educations(profile.getEducations())
                .interests(profile.getInterests())
                .skills(profile.getSkills()
                        .stream()
                        .map(GetCandidateProfileByIdSkillResponse::fromSkill)
                        .toList())
                .build();
    }
}
