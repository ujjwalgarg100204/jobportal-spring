package com.ujjwalgarg.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.response.GetCandidateProfileByIdResponse;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.security.services.AuthService;
import com.ujjwalgarg.jobportal.service.CandidateProfileService;

/**
 * CandidateProfileController
 */
@RestController
@RequestMapping(value = "/api/candidate/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidateProfileController {

    private final AuthService authService;
    private final CandidateProfileService cProfileService;

    @Autowired
    public CandidateProfileController(AuthService authService, CandidateProfileService cProfileService) {
        this.authService = authService;
        this.cProfileService = cProfileService;
    }

    @GetMapping
    public ResponseEntity<Response<GetCandidateProfileByIdResponse>> getCandidateProfileById() {
        User user = this.authService.getAuthenticatedUser();
        CandidateProfile profile = this.cProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Candidate Profile with id:%d could not be found",
                        user.getId())));
        var response = Response.<GetCandidateProfileByIdResponse>builder()
                .success(true)
                .data(GetCandidateProfileByIdResponse.fromCandidateProfile(profile))
                .message("Candidate Profile fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
