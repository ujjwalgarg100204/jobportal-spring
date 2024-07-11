package com.ujjwalgarg.jobportal.controlleradvice;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import java.util.Arrays;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure(
        String.format("Method `%s` is not supported for path `%s`, supported methods are `%s`",
            ex.getMethod(),
            request.getContextPath(), Arrays.toString(ex.getSupportedMethods())));
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure(String.format(
        "Media of type `%s` is not supported for path `%s` with method `%s`, supported media types is/are `%s`",
        ex.getContentType(), request.getContextPath(), "", ex.getSupportedMediaTypes()));
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure("Media of type `%s` is not acceptable");
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    var response = Response.failure(
        String.format("Missing path variable '%s' for method parameter of type %s",
            ex.getVariableName(), ex.getParameter().getParameterType().getSimpleName()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure(
        String.format("Required parameter '%s' of type %s is missing",
            ex.getParameterName(), ex.getParameterType()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    var response = Response.failure("Validation failed");
    for (FieldError err : ex.getFieldErrors()) {
      response.addValidationError(err.getField(), err.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure("Validation failed");
    ex.getAllValidationResults()
        .stream()
        .flatMap(result -> result.getResolvableErrors().stream())
        .forEach(error -> response.addValidationError(Arrays.toString(error.getArguments()),
            error.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    var response = Response.failure(
        String.format("No resource found for %s %s",
            ex.getHttpMethod(), ex.getResourcePath()));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    var response = Response.failure(
        String.format("Maximum upload size of %d bytes exceeded",
            ex.getMaxUploadSize()));
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    Class<?> requiredType = ex.getRequiredType();
    var response = Response.failure(
        String.format("Conversion not supported for value '%s' of type %s",
            ex.getValue(), requiredType != null ? requiredType.getSimpleName() : "<unknown>"));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {
    Class<?> requiredType = ex.getRequiredType();
    var response = Response.failure(
        String.format("Type mismatch: value '%s' cannot be converted to %s",
            ex.getValue(), requiredType != null ? requiredType.getSimpleName() : "<unknown>"));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    var response = Response.failure("Method validation failed");
    ex.getAllValidationResults().stream()
        .flatMap(result -> result.getResolvableErrors().stream())
        .forEach(error -> response.addValidationError(Arrays.toString(error.getArguments()),
            error.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

    var response = Response.failure("An internal error occurred: " + ex.getMessage());
    return ResponseEntity.status(statusCode).body(response);
  }

}
