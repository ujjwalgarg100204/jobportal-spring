package com.ujjwalgarg.jobportal.controller.payload.auth;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@ValidCompanyDetails
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewRecruiterRequest implements CompanyDetailsValidatable {

  // user fields
  @Email
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  // profile fields
  @NotBlank(message = "First Name is mandatory")
  private String firstName;

  @NotBlank(message = "Last Name is mandatory")
  private String lastName;

  // company fields
  private Integer companyId;
  private String companyName;
  private String companyAddressCity;
  private String companyAddressState;
  private String companyAddressCountry;
}
