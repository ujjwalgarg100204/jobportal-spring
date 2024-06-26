package com.ujjwalgarg.jobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.response.GetRecruiterProfileByIdResponse;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.security.services.AuthService;
import com.ujjwalgarg.jobportal.service.RecruiterProfileService;

/**
 * RecruiterProfileController
 */
@RestController
@RequestMapping(value = "/api/recruiter/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RecruiterProfileController {

    private final AuthService authService;
    private final RecruiterProfileService rProfileService;

    @Autowired
    public RecruiterProfileController(AuthService authService, RecruiterProfileService rProfileService) {
        this.authService = authService;
        this.rProfileService = rProfileService;
    }

    @GetMapping
    public ResponseEntity<Response<GetRecruiterProfileByIdResponse>> getRecruiterProfileById() {
        User user = this.authService.getAuthenticatedUser();
        RecruiterProfile profile = this.rProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Recruiter Profile with id:%d could not be found",
                        user.getId())));
        var response = Response.<GetRecruiterProfileByIdResponse>builder()
                .success(true)
                .data(GetRecruiterProfileByIdResponse.fromRecruiterProfile(profile))
                .message("Recruiter Profile fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }

}
