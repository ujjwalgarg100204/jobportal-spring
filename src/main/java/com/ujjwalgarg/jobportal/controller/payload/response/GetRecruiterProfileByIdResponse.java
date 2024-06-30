package com.ujjwalgarg.jobportal.controller.payload.response;

import java.util.List;

import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;

import lombok.Builder;

/**
 * GetRecruiterProfileByIdResponse
 */
@Builder
public record GetRecruiterProfileByIdResponse(
        Integer id,
        String firstName,
        String lastName,
        String about,
        String profilePhotoUrl,
        Boolean hasProfilePhoto,
        Address address,
        ContactInformation contactInformation,
        List<Education> educations,
        List<Interest> interests,
        CompanyDTOResponse company) {

    public static GetRecruiterProfileByIdResponse fromRecruiterProfile(RecruiterProfile profile,
            String profilePhotoUrl) {
        return GetRecruiterProfileByIdResponse.builder()
                .id(profile.getId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .about(profile.getAbout())
                .hasProfilePhoto(profile.getHasProfilePhoto())
                .address(profile.getAddress())
                .profilePhotoUrl(profilePhotoUrl)
                .contactInformation(profile.getContactInformation())
                .educations(profile.getEducations())
                .interests(profile.getInterests())
                .company(CompanyDTOResponse.fromCompany(profile.getCompany()))
                .build();

    }
}
