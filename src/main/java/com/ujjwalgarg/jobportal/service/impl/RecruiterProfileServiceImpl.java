package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import com.ujjwalgarg.jobportal.annotation.FileSize;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.mapper.RecruiterProfileMapper;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;
import com.ujjwalgarg.jobportal.service.CompanyService;
import com.ujjwalgarg.jobportal.service.FileUploadService;
import com.ujjwalgarg.jobportal.service.RecruiterProfileService;
import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j(topic = "RECRUITER_PROFILE_SERVICE")
@PreAuthorize("hasRole('ROLE_RECRUITER')")
@RequiredArgsConstructor
public class RecruiterProfileServiceImpl implements RecruiterProfileService {

  private final RecruiterProfileRepository recruiterProfileRepository;
  private final RecruiterProfileMapper profileMapper;
  private final UserService userService;
  private final FileUploadService fileUploadService;
  private final CompanyService companyService;

  private static String getProfilePhotoPath(int id) {
    return String.format("recruiter-profile-photo/%d", id);
  }

  @Override
  public void updateProfile(@Valid RecruiterProfileUpdateDTO updateDTO,
      @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = {
          "image/png", "image/jpg", "image/jpeg"}) MultipartFile profilePhoto)
      throws NotFoundException, IOException {
    RecruiterProfile profile = recruiterProfileRepository.findById(updateDTO.getId())
        .orElseThrow(() -> {
          log.error("Recruiter Profile not found for id: {}", updateDTO.getId());
          return new NotFoundException("Recruiter Profile not found");
        });
    profileMapper.updateRecruiterProfile(updateDTO, profile);
    profile.setUser(userService.getUserById(profile.getId()));

    // set company
    Integer companyId = profile.getCompany().getId();
    if (companyId != null) {
      profile.setCompany(companyService.getCompanyById(companyId));
    }

    // delete or upload profile photo
    if (profilePhoto != null) {
      // save profile photo in storage
      String profilePhotoPath = getProfilePhotoPath(profile.getId());
      boolean success = fileUploadService.uploadFile(profilePhotoPath, profilePhoto.getBytes());
      if (success) {
        profile.setHasProfilePhoto(true);
      }
    } else {
      fileUploadService.deleteFile(getProfilePhotoPath(profile.getId()));
      profile.setHasProfilePhoto(false);
    }

    recruiterProfileRepository.save(profile);
  }
}
