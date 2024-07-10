package com.ujjwalgarg.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * AlreadyPresentException
 */
public class AlreadyPresentException extends ResponseStatusException {

  public AlreadyPresentException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
