package com.ujjwalgarg.jobportal.controller;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import com.ujjwalgarg.jobportal.annotation.FileSize;
import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.service.CandidateProfileService;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/candidate/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_CANDIDATE')")
public class CandidateProfileController {

  private final CandidateProfileService profileService;

  @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response<Void>> updateCandidateProfile(
      @RequestPart(value = "updateDetails") @Valid CandidateProfileUpdateDTO updateDTO,
      @RequestPart(value = "resume", required = false) @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = "application/pdf") MultipartFile resume,
      @RequestPart(value = "profilePhoto", required = false) @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = {
          "image/png", "image/jpg", "image/jpeg"}) MultipartFile profilePhoto) throws IOException {

    profileService.updateProfile(updateDTO, profilePhoto, resume);
    var res = Response.<Void>success(null, "Profile updated successfully");
    return ResponseEntity.ok(res);
  }
}
