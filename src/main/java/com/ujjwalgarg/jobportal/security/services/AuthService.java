package com.ujjwalgarg.jobportal.security.services;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * AuthService
 */
@Service
public class AuthService {

  private final UserServiceImpl userService;

  @Autowired
  public AuthService(UserServiceImpl userService) {
    this.userService = userService;
  }

  public User getAuthenticatedUser() {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return this.userService
        .getUserByEmail(userDetails.getUsername());
  }
}
