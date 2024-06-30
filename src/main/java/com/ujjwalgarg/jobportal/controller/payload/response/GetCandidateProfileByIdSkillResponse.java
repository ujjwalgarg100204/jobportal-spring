package com.ujjwalgarg.jobportal.controller.payload.response;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import com.ujjwalgarg.jobportal.entity.Skill;

import lombok.Builder;

/**
 * GetCandidateProfileByIdSkillResponse
 */
@Builder
public record GetCandidateProfileByIdSkillResponse(
        Integer id,
        String name,
        String yearsOfExperience,
        ExperienceLevel experienceLevel) {

    public static GetCandidateProfileByIdSkillResponse fromSkill(Skill skill) {
        return GetCandidateProfileByIdSkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .yearsOfExperience(skill.getYearsOfExperience())
                .experienceLevel(skill.getExperienceLevel())
                .build();
    }

}
