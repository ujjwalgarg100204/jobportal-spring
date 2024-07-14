package com.ujjwalgarg.jobportal.validator;

import com.ujjwalgarg.jobportal.annotation.ValidCompanyDetails;
import com.ujjwalgarg.jobportal.validator.validatable.CompanyDetailsValidatable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CompanyDetailsValidator implements
    ConstraintValidator<ValidCompanyDetails, CompanyDetailsValidatable> {

  private static boolean isNotNullAndNonEmpty(String s) {
    return s == null || s.isEmpty();
  }

  @Override
  public boolean isValid(CompanyDetailsValidatable request, ConstraintValidatorContext context) {
    boolean isValid = true;

    if (request.getCompanyId() == null) {
      if (isNotNullAndNonEmpty(request.getCompanyName())) {
        context.buildConstraintViolationWithTemplate(
                "Company name must be provided if company ID is not specified.")
            .addPropertyNode("companyName")
            .addConstraintViolation();
        isValid = false;
      }
      if (isNotNullAndNonEmpty(request.getCompanyAddressState())) {
        context.buildConstraintViolationWithTemplate(
                "Company state must be provided if company ID is not specified.")
            .addPropertyNode("companyAddressState")
            .addConstraintViolation();
        isValid = false;
      }
      if (isNotNullAndNonEmpty(request.getCompanyAddressCountry())) {
        context.buildConstraintViolationWithTemplate(
                "Company country must be provided if company ID is not specified.")
            .addPropertyNode("companyAddressCountry")
            .addConstraintViolation();
        isValid = false;
      }
    } else {
      if (request.getCompanyName() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company name must be null if company ID is specified.")
            .addPropertyNode("companyName")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.getCompanyAddressCity() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company city must be null if company ID is specified.")
            .addPropertyNode("companyAddressCity")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.getCompanyAddressState() != null) {
        context.buildConstraintViolationWithTemplate(
                "Company state must be null if company ID is specified.")
            .addPropertyNode("companyAddressState")
            .addConstraintViolation();
        isValid = false;
      }
      if (request.getCompanyAddressCountry() != null) {
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
