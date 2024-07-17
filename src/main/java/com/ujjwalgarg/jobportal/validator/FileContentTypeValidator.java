package com.ujjwalgarg.jobportal.validator;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileContentTypeValidator implements
    ConstraintValidator<FileContentType, MultipartFile> {

  private String[] allowedTypes;

  @Override
  public void initialize(FileContentType constraintAnnotation) {
    this.allowedTypes = constraintAnnotation.allowedTypes();
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return true; // or false, depending on your requirements
    }

    String contentType = file.getContentType();
    for (String type : allowedTypes) {
      if (type.equalsIgnoreCase(contentType)) {
        return true;
      }
    }
    return false;
  }
}
