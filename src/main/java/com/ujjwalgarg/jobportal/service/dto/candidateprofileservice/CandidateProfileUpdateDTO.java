package com.ujjwalgarg.jobportal.service.dto.candidateprofileservice;

import com.ujjwalgarg.jobportal.constant.EmploymentType;
import com.ujjwalgarg.jobportal.constant.WorkAuthorization;
import com.ujjwalgarg.jobportal.controller.payload.common.SkillDetailsDto;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Builder
@Getter
@Setter
public class CandidateProfileUpdateDTO {

  @NotNull
  private Integer id;

  @NotBlank(message = "First Name is mandatory")
  private String firstName;

  @NotBlank(message = "Last Name is mandatory")
  private String lastName;

  @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000")
  private String about;

  @URL
  private String portfolioWebsite;

  private WorkAuthorization workAuthorization;

  private EmploymentType preferredEmploymentType;

  @Valid
  private Address address;

  @Valid
  private ContactInformation contactInformation;

  private List<@Valid Education> educations;

  private List<@Valid Interest> interests;

  private List<@Valid SkillDetailsDto> skills;
}
