package com.ujjwalgarg.jobportal.annotation;

import static java.lang.annotation.ElementType.TYPE;

import com.ujjwalgarg.jobportal.validator.CompanyDetailsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CompanyDetailsValidator.class)
public @interface ValidCompanyDetails {

  String message() default "Invalid company details provided. Either provide company id or company information. Not both";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
