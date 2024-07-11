package com.ujjwalgarg.jobportal.controller;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.auth.LoginRequest;
import com.ujjwalgarg.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController
 */
@RestController
@RequestMapping(
    path = "/api/auth",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<Response<String>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    String token = this.authService.loginUser(loginRequest.email(), loginRequest.password());

    var response = Response.success(token,
        String.format("Successfully logged in user with email %s", loginRequest.email()));
    return ResponseEntity.ok(response);
  }

}
