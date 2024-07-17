package com.ujjwalgarg.jobportal.validator;

import com.ujjwalgarg.jobportal.annotation.FileSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

  private long max;
  private long min;
  private boolean allowEmpty;

  @Override
  public void initialize(FileSize constraintAnnotation) {
    this.max = constraintAnnotation.max();
    this.min = constraintAnnotation.min();
    this.allowEmpty = constraintAnnotation.allowEmpty();
  }

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null) {
      return true;
    }
    if (file.isEmpty()) {
      return allowEmpty;
    }
    return file.getSize() >= min && file.getSize() <= max;
  }
}
