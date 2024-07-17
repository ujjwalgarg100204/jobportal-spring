package com.ujjwalgarg.jobportal.annotation;

import com.ujjwalgarg.jobportal.validator.FileSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileSizeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileSize {

  String message() default "Invalid file size";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  long max(); // maximum file size in bytes

  long min(); // minimum file size in bytes

  boolean allowEmpty() default true;

}
