package com.ujjwalgarg.jobportal.controller.payload.candidateprofile;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import lombok.Builder;

@Builder
public record SkillGetRequestDto(
    Integer id,
    String name,
    String yearsOfExperience,
    ExperienceLevel experienceLevel
) {

}
