package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.service.AuthService;
import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.util.JwtUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService
 */
@Slf4j(topic = "AUTH_SERVICE")
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;


  @PreAuthorize("isAuthenticated()")
  @Transactional(readOnly = true)
  public User getAuthenticatedUser() throws NotFoundException {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String email = userDetails.getUsername();

    return this.userService.getUserByEmail(email);
  }

  @Override
  public String loginUser(@Valid @Email String email, @Valid @NotNull String password)
      throws BadCredentialsException {
    var authToken = new UsernamePasswordAuthenticationToken(email, password);
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    return jwtUtils.generateJwtToken(userDetails.getUsername());
  }
}
