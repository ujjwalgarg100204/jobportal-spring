package com.ujjwalgarg.jobportal.controller.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Response<T> {

  private final boolean success;
  private final String message;

  @JsonInclude(Include.NON_NULL)
  private T data;

  @JsonInclude(Include.NON_NULL)
  private List<ValidationError> validationErrors;

  public static <T> Response<T> success(T data, String message) {
    var response = new Response<T>(true, message);
    response.setData(data);
    return response;
  }

  public static <T> Response<T> failure(String message) {
    return new Response<>(false, message);
  }

  public void addValidationError(String field, String message) {
    if (this.validationErrors == null) {
      this.validationErrors = new ArrayList<>();
    }

    var validationError = new ValidationError(field, message);
    this.validationErrors.add(validationError);
  }

  private record ValidationError(String field, String message) {

  }
}
