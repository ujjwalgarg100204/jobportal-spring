package com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import com.ujjwalgarg.jobportal.entity.Address;
import com.ujjwalgarg.jobportal.entity.ContactInformation;
import com.ujjwalgarg.jobportal.entity.Education;
import com.ujjwalgarg.jobportal.entity.Interest;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@Setter
@ToString
@ValidCompanyDetails
public class RecruiterProfileUpdateDTO implements CompanyDetailsValidatable {

  @NotNull
  private Integer id;

  @NotBlank(message = "First Name is mandatory")
  private String firstName;

  @NotBlank(message = "Last Name is mandatory")
  private String lastName;

  @Length(max = 10_000, message = "Maximum number of characters allowed is 10,000")
  private String about;

  @Valid
  private Address address;

  @Valid
  private ContactInformation contactInformation;

  private List<@Valid Education> educations;

  private List<@Valid Interest> interests;

  // company fields
  private Integer companyId;
  private String companyName;
  private String companyAddressCity;
  private String companyAddressState;
  private String companyAddressCountry;
}
