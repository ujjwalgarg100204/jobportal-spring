package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthService {

  User getAuthenticatedUser() throws NotFoundException;

  String loginUser(String email, String password) throws BadCredentialsException;
}
