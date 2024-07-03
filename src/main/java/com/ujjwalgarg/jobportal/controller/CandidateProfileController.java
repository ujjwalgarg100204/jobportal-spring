package com.ujjwalgarg.jobportal.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.controller.payload.request.UpdateCandidateProfileRequest;
import com.ujjwalgarg.jobportal.controller.payload.response.GetCandidateProfileByIdResponse;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.EntityNotFoundException;
import com.ujjwalgarg.jobportal.exception.FileTooBigException;
import com.ujjwalgarg.jobportal.exception.InvalidFileTypeException;
import com.ujjwalgarg.jobportal.security.services.AuthService;
import com.ujjwalgarg.jobportal.service.CandidateProfileService;
import com.ujjwalgarg.jobportal.service.S3FileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * CandidateProfileController
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/candidate/profile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidateProfileController {

    private final AuthService authService;
    private final CandidateProfileService cProfileService;
    private final S3FileService s3FileService;

    @GetMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<GetCandidateProfileByIdResponse>> getCandidateProfileById() {
        User user = this.authService.getAuthenticatedUser();
        CandidateProfile profile = this.cProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Candidate Profile with id:%d could not be found",
                        user.getId())));
        String profilePhotoUrl = null;
        if (profile.getHasProfilePhoto()) {
            profilePhotoUrl = this.s3FileService.getFilePresignedUrl(
                    CandidateProfileService.getProfileImageS3Path(user.getId()));
        }
        var response = Response.<GetCandidateProfileByIdResponse>builder()
                .success(true)
                .data(GetCandidateProfileByIdResponse.fromCandidateProfile(profile, profilePhotoUrl))
                .message("Candidate Profile fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<Void>> updateCandidateProfile(
            @Valid @RequestPart(value = "profile", required = true) UpdateCandidateProfileRequest reqProfile,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto) {
        User user = this.authService.getAuthenticatedUser();
        CandidateProfile profile = reqProfile.toCandidateProfile();

        profile.setHasProfilePhoto(false);
        profile.setHasResume(false);

        // save resume and profile photo
        if (resume != null && !resume.isEmpty()) {
            // perform file validation
            if (resume.getSize() > 5e6) {
                throw new FileTooBigException("Resume should be less than or equal to 5MB");
            } else if (!resume.getContentType().equals("application/pdf")) {
                throw new InvalidFileTypeException("Only pdf files are allowed as resume");
            }

            try {
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", resume.getContentType());
                boolean success = this.s3FileService.uploadFile(
                        CandidateProfileService.getResumeS3Path(user.getId()),
                        resume.getBytes(),
                        metadata);
                if (success) {
                    profile.setHasResume(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            this.s3FileService.deleteFile(CandidateProfileService.getResumeS3Path(user.getId()));
        }

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
                        CandidateProfileService.getProfileImageS3Path(user.getId()),
                        profilePhoto.getBytes(),
                        metadata);
                if (success) {
                    profile.setHasProfilePhoto(true);
                }
            } catch (IOException err) {
                err.printStackTrace();
            }
        } else {
            this.s3FileService.deleteFile(CandidateProfileService.getProfileImageS3Path(user.getId()));
        }

        // prepare profile object for save
        profile.setUser(user);
        profile.getSkills()
                .forEach(skill -> skill.setCandidateProfile(profile));
        profile.setId(user.getId());

        this.cProfileService.updateProfile(profile);

        var response = Response.<Void>builder()
                .success(true)
                .message("Candidate Profile updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/photo", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<String>> getProfilePhotoUrl() {
        User user = this.authService.getAuthenticatedUser();
        CandidateProfile profile = this.cProfileService
                .getProfileById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Candidate Profile with id:%d could not be found",
                        user.getId())));
        if (!profile.getHasProfilePhoto()) {
            throw new EntityNotFoundException("Profile Photo of the candidate does not exist");
        }

        String presignedUrl = this.s3FileService.getFilePresignedUrl(
                CandidateProfileService.getProfileImageS3Path(user.getId()));

        var response = Response.<String>builder()
                .success(true)
                .data(presignedUrl)
                .message("Profile Photo fetched successfully")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/jobApplications/{id}/@exists", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<Boolean>> checkIfCandidateAppliedForJob(@PathVariable("id") Integer jobId) {
        User user = this.authService.getAuthenticatedUser();
        boolean applied = this.cProfileService.checkIfCandidateAppliedForJob(user.getId(), jobId);

        var response = Response.success(applied, "Checked if candidate applied for job successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/bookmarkedJobs/{id}/@exists", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Response<Boolean>> checkIfCandidateHasBookmarkedJob(@PathVariable("id") Integer jobId) {
        User user = this.authService.getAuthenticatedUser();
        boolean bookmarked = this.cProfileService.checkIfCandidateBookmarkedJob(user.getId(), jobId);

        var response = Response.success(bookmarked, "Checked if candidate bookmarked job successfully");
        return ResponseEntity.ok(response);
    }

}
