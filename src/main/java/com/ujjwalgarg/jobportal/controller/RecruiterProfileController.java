package com.ujjwalgarg.jobportal.controller;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import com.ujjwalgarg.jobportal.annotation.FileSize;
import com.ujjwalgarg.jobportal.controller.payload.Response;
import com.ujjwalgarg.jobportal.service.RecruiterProfileService;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
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
@RequestMapping(path = "/api/recruiter/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_RECRUITER')")
public class RecruiterProfileController {

  private final RecruiterProfileService profileService;


  @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Response<Void>> updateRecruiterProfile(
      @RequestPart(value = "updateDetails") @Valid RecruiterProfileUpdateDTO updateDTO,
      @RequestPart(value = "profilePhoto", required = false) @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = {
          "image/png", "image/jpg", "image/jpeg"}) MultipartFile profilePhoto) throws IOException {

    profileService.updateProfile(updateDTO, profilePhoto);
    var res = Response.<Void>success(null, "Profile updated successfully");
    return ResponseEntity.ok(res);
  }
}
