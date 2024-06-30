package com.ujjwalgarg.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.request.LoginRequest;
import com.ujjwalgarg.jobportal.controller.payload.request.RegisterNewCandidateRequest;
import com.ujjwalgarg.jobportal.controller.payload.request.RegisterNewRecruiterRequest;
import com.ujjwalgarg.jobportal.controller.payload.request.VerifySessionRequest;
import com.ujjwalgarg.jobportal.controller.payload.response.JwtResponse;
import com.ujjwalgarg.jobportal.controller.payload.response.LoginResponse;
import com.ujjwalgarg.jobportal.controller.payload.response.UserJwtResponse;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityAlreadyExistsException;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.security.jwt.JwtUtils;
import com.ujjwalgarg.jobportal.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

/**
 * AuthController
 */
@RestController
@RequestMapping(value = "/api/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService,
            PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        var response = Response.<LoginResponse>builder()
                .success(true)
                .data(new LoginResponse(jwt))
                .message("Succesfully logged in user with email %s".formatted(loginRequest.email()))
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/register/recruiter")
    public ResponseEntity<Response<Void>> registerNewRecruiter(
            @Valid @RequestBody RegisterNewRecruiterRequest recruiter) {
        if (this.userService.checkIfExistsByEmail(recruiter.email())) {
            throw new EntityAlreadyExistsException(
                    "User with email:%s already exists, maybe try logging in instead".formatted(recruiter.email()));
        }
        var rRole = Role.builder()
                .id(2)
                .name(ERole.ROLE_RECRUITER)
                .build();
        var rUser = User.builder()
                .email(recruiter.email())
                .password(encoder.encode(recruiter.password()))
                .role(rRole)
                .build();
        var rProfile = RecruiterProfile.builder()
                .firstName(recruiter.firstName())
                .lastName(recruiter.lastName())
                .company(recruiter.company())
                .hasProfilePhoto(false)
                .build();

        // set company logo to false
        recruiter.company().setHasLogo(false);

        // save User first
        this.userService.createNew(rUser, rProfile, null);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Successfully created new recruiter with email:%s"
                        .formatted(recruiter.email()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/register/candidate")
    public ResponseEntity<Response<Void>> registerNewCandidate(
            @Valid @RequestBody RegisterNewCandidateRequest candidate) {
        if (this.userService.checkIfExistsByEmail(candidate.email())) {
            throw new EntityAlreadyExistsException("User with email:%s already exists".formatted(candidate.email()));
        }
        var cRole = Role.builder()
                .id(1)
                .name(ERole.ROLE_CANDIDATE)
                .build();
        var cUser = User.builder()
                .email(candidate.email())
                .password(encoder.encode(candidate.password()))
                .role(cRole)
                .build();
        var cProfile = CandidateProfile.builder()
                .firstName(candidate.firstName())
                .lastName(candidate.lastName())
                .shortAbout(candidate.shortAbout())
                .contactInformation(candidate.contactInformation())
                .hasProfilePhoto(false)
                .hasResume(false)
                .build();

        // save User first
        this.userService.createNew(cUser, null, cProfile);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Successfully created new candidate with email:%s".formatted(candidate.email()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/me")
    public ResponseEntity<Response<JwtResponse>> verifySession(@Valid @RequestBody VerifySessionRequest session) {
        String email = this.jwtUtils.getEmailFromJwtToken(session.token());
        User user = this.userService.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No user with email: %s found", email)));

        var userJwtResponse = UserJwtResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
        var jwtResponse = new JwtResponse(session.token(), userJwtResponse);
        var baseResponse = Response.<JwtResponse>builder()
                .success(true)
                .data(jwtResponse)
                .message("jwt successfully decrypted and user returned")
                .build();

        return ResponseEntity.ok(baseResponse);
    }

}
