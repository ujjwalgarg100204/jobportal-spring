package com.ujjwalgarg.jobportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.request.CreateNewJobRequest;
import com.ujjwalgarg.jobportal.controller.payload.request.UpdateJobRequest;
import com.ujjwalgarg.jobportal.entity.Job;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.security.services.AuthService;
import com.ujjwalgarg.jobportal.service.JobService;
import com.ujjwalgarg.jobportal.service.RecruiterProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * JobController
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/job", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class JobController {

    private final AuthService authService;
    private final JobService jobService;
    private final RecruiterProfileService rProfileService;

    @Transactional
    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    @PostMapping
    public ResponseEntity<Response<Void>> createNewJob(@Valid @RequestBody CreateNewJobRequest jobRequest) {
        User user = authService.getAuthenticatedUser();
        RecruiterProfile rProfile = rProfileService.getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter profile not found"));
        Job job = jobRequest.toJob();

        job.setRecruiterProfile(rProfile);
        job.setCompany(rProfile.getCompany());

        jobService.createNew(job);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Job created successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    @PatchMapping
    public ResponseEntity<Response<Void>> updateJob(@Valid @RequestBody UpdateJobRequest jobRequest) {
        User user = authService.getAuthenticatedUser();
        RecruiterProfile rProfile = rProfileService.getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter profile not found"));
        Job job = jobRequest.toJob();

        job.setRecruiterProfile(rProfile);
        job.setCompany(rProfile.getCompany());

        jobService.updateJob(job);

        var response = Response.<Void>builder()
                .success(true)
                .data(null)
                .message("Job updated successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
