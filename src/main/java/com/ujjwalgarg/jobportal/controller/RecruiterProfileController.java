package com.ujjwalgarg.jobportal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.request.UpdateRecruiterProfileRequest;
import com.ujjwalgarg.jobportal.controller.payload.response.GetRecruiterJobsResponse;
import com.ujjwalgarg.jobportal.controller.payload.response.GetRecruiterProfileByIdResponse;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.exception.FileTooBigException;
import com.ujjwalgarg.jobportal.exception.InvalidFileTypeException;
import com.ujjwalgarg.jobportal.exception.ResourceNotFoundException;
import com.ujjwalgarg.jobportal.security.services.AuthService;
import com.ujjwalgarg.jobportal.service.JobService;
import com.ujjwalgarg.jobportal.service.RecruiterProfileService;
import com.ujjwalgarg.jobportal.service.S3FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * RecruiterProfileController
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/recruiter/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class RecruiterProfileController {

    private final AuthService authService;
    private final S3FileService s3FileService;
    private final RecruiterProfileService rProfileService;
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Response<GetRecruiterProfileByIdResponse>> getCurrentRecruiterProfile() {
        User user = this.authService.getAuthenticatedUser();
        RecruiterProfile profile = this.rProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Recruiter Profile with id:%d could not be found",
                        user.getId())));
        String profilePhotoUrl = null;
        if (profile.getHasProfilePhoto()) {
            profilePhotoUrl = this.s3FileService.getFilePresignedUrl(
                    RecruiterProfileService.getProfileImageS3Path(user.getId()));
        }

        var response = Response.<GetRecruiterProfileByIdResponse>builder()
                .success(true)
                .data(GetRecruiterProfileByIdResponse.fromRecruiterProfile(profile, profilePhotoUrl))
                .message("Recruiter Profile fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> updateCurrentRecruiterProfile(
            @Valid @RequestPart(value = "profile", required = true) UpdateRecruiterProfileRequest reqProfile,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        User user = this.authService.getAuthenticatedUser();
        RecruiterProfile profile = reqProfile.toRecruiterProfile();

        // save profile photo
        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            // perform file validation
            if (profilePhoto.getSize() > 5e6) {
                throw new FileTooBigException("Profile Photo should be less than or equal to 5MB");
            } else if (!profilePhoto.getContentType().startsWith("image/")) {
                throw new InvalidFileTypeException("Only image files are allowed as profile photo");
            }

            try {
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", profilePhoto.getContentType());
                boolean success = this.s3FileService.uploadFile(
                        RecruiterProfileService.getProfileImageS3Path(user.getId()),
                        profilePhoto.getBytes(),
                        metadata);
                if (success) {
                    profile.setHasProfilePhoto(true);
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        }

        // prepare profile object for save
        profile.setUser(user);
        profile.setId(user.getId());

        this.rProfileService.updateProfile(profile);

        var response = Response.<Void>builder()
                .success(true)
                .message("Candidate Profile updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    @GetMapping(value = "/photo", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getProfilePhotoUrl() {
        User user = this.authService.getAuthenticatedUser();
        RecruiterProfile profile = this.rProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Candidate Profile with id:%d could not be found",
                        user.getId())));
        if (!profile.getHasProfilePhoto()) {
            throw new ResourceNotFoundException("Profile Photo of the candidate does not exist");
        }

        String presignedUrl = this.s3FileService.getFilePresignedUrl(
                RecruiterProfileService.getProfileImageS3Path(user.getId()));

        return ResponseEntity.ok(presignedUrl);
    }

    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    @Transactional(readOnly = true)
    @GetMapping(value = "/company", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<Company>> getCurrentRecruiterCompany() {
        User user = this.authService.getAuthenticatedUser();
        RecruiterProfile profile = this.rProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Candidate Profile with id:%d could not be found",
                        user.getId())));
        Company company = profile.getCompany();

        var response = Response.<Company>builder()
                .success(true)
                .data(company)
                .message("Company fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_RECRUITER')")
    @Transactional(readOnly = true)
    @GetMapping(value = "/job", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<List<GetRecruiterJobsResponse>>> findAllJobsOfRecruiter() {
        User user = this.authService.getAuthenticatedUser();
        List<GetRecruiterJobsResponse> jobs = this.jobService.getJobsOfRecruiterWithApplicantCount(user.getId());

        var response = Response.<List<GetRecruiterJobsResponse>>builder()
                .success(true)
                .data(jobs)
                .message("Jobs fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
