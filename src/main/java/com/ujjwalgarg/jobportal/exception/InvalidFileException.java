package com.ujjwalgarg.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * InvalidFileException
 */
public class InvalidFileException extends ResponseStatusException {

  public InvalidFileException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
