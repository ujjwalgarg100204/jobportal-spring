package com.ujjwalgarg.jobportal.controller.payload.common;

import com.ujjwalgarg.jobportal.constant.ExperienceLevel;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SkillDetailsDto {

  private Integer id;

  @NotBlank(message = "Name of skill is mandatory")
  private String name;

  @NotBlank(message = "Years of Experience of skill is mandatory")
  private String yearsOfExperience;

  @NotNull(message = "Experience level of skill is mandatory")
  private ExperienceLevel experienceLevel;
}
