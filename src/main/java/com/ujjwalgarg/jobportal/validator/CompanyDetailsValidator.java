package com.ujjwalgarg.jobportal.validator;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyDetailsValidator implements
    ConstraintValidator<ValidCompanyDetails, NewRecruiterRequest> {

  private static boolean isNotNullAndNonEmpty(String s) {
    return s == null || s.isEmpty();
  }

  @Override
  public boolean isValid(NewRecruiterRequest request, ConstraintValidatorContext context) {
    boolean isValid = true;

    if (request.companyId() == null) {
      if (isNotNullAndNonEmpty(request.companyName())) {
        context.buildConstraintViolationWithTemplate(
                "Company name must be provided if company ID is not specified.")
            .addPropertyNode("companyName")
            .addConstraintViolation();
        isValid = false;
      }
      if (isNotNullAndNonEmpty(request.companyAddressState())) {
        context.buildConstraintViolationWithTemplate(
                "Company state must be provided if company ID is not specified.")
            .addPropertyNode("companyAddressState")
            .addConstraintViolation();
        isValid = false;
      }
      if (isNotNullAndNonEmpty(request.companyAddressCountry())) {
        context.buildConstraintViolationWithTemplate(
                "Company country must be provided if company ID is not specified.")
            .addPropertyNode("companyAddressCountry")
            .addConstraintViolation();
        isValid = false;
      }
    } else {
      if (request.companyName() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company name must be null if company ID is specified.")
            .addPropertyNode("companyName")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.companyAddressCity() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company city must be null if company ID is specified.")
            .addPropertyNode("companyAddressCity")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.companyAddressState() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company state must be null if company ID is specified.")
            .addPropertyNode("companyAddressState")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.companyAddressCountry() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company country must be null if company ID is specified.")
            .addPropertyNode("companyAddressCountry")
            .addConstraintViolation();
        isValid = false;
      }
    }

    if (!isValid) {
      context.disableDefaultConstraintViolation();
    }

    return isValid;
  }
}
