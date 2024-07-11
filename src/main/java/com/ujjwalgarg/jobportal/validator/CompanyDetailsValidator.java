package com.ujjwalgarg.jobportal.validator;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import com.ujjwalgarg.jobportal.controller.payload.auth.RegisterNewRecruiterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyDetailsValidator implements
    ConstraintValidator<ValidCompanyDetails, RegisterNewRecruiterRequest> {

  private static boolean isNotNullAndNonEmpty(String s) {
    return s != null && !s.isEmpty();
  }

  @Override
  public boolean isValid(RegisterNewRecruiterRequest request, ConstraintValidatorContext context) {
    // request is considered valid only, if either company id is provided or company information
    // is provided, not both
    if (request.companyId() == null) {
      return isNotNullAndNonEmpty(request.companyName()) &&
          isNotNullAndNonEmpty(request.companyAddressState()) &&
          isNotNullAndNonEmpty(request.companyAddressCountry());
    } else {
      return request.companyName() == null && request.companyAddressCity() == null &&
          request.companyAddressState() == null && request.companyAddressCountry() == null;
    }
  }
}
