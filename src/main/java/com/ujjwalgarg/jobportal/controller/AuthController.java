package com.ujjwalgarg.jobportal.controller;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.auth.LoginRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.auth.NewRecruiterRequest;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.mapper.CandidateProfileMapper;
import com.ujjwalgarg.jobportal.mapper.RecruiterProfileMapper;
import com.ujjwalgarg.jobportal.mapper.UserMapper;
import com.ujjwalgarg.jobportal.service.AuthService;
import com.ujjwalgarg.jobportal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
  private final UserService userService;
  private final UserMapper userMapper;
  private final RecruiterProfileMapper recruiterProfileMapper;
  private final CandidateProfileMapper candidateProfileMapper;

  @PostMapping("/login")
  public ResponseEntity<Response<String>> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
    String token = this.authService.loginUser(loginRequest.email(), loginRequest.password());

    var response = Response.success(token,
        String.format("Successfully logged in user with email %s", loginRequest.email()));
    return ResponseEntity.ok(response);
  }

  @Transactional
  @PostMapping("/candidate")
  public ResponseEntity<Response<Void>> createNewCandidate(
      @Valid @RequestBody NewCandidateRequest candidateRequest) {
    User user = this.userMapper.fromNewCandidateRequest(candidateRequest);
    CandidateProfile cProfile = this.candidateProfileMapper.fromNewCandidateRequest(
        candidateRequest);

    this.userService.createNewCandidate(user, cProfile);
    var response = Response.<Void>success(null, "Successfully created new candidate");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Transactional
  @PostMapping("/recruiter")
  public ResponseEntity<Response<Void>> createNewRecruiter(
      @Valid @RequestBody NewRecruiterRequest recruiterRequest) {
    User user = this.userMapper.fromNewRecruiterRequest(recruiterRequest);
    var rProfile = this.recruiterProfileMapper.fromNewRecruiterRequest(recruiterRequest);

    this.userService.createNewRecruiter(user, rProfile);
    var response = Response.<Void>success(null, "Successfully created new recruiter");
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
