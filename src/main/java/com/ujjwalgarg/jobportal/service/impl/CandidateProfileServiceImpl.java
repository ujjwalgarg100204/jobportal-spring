package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.annotation.FileContentType;
import com.ujjwalgarg.jobportal.annotation.FileSize;
import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.CandidateProfileGetRequestDto;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.mapper.CandidateProfileMapper;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;
import com.ujjwalgarg.jobportal.service.CandidateProfileService;
import com.ujjwalgarg.jobportal.service.FileUploadService;
import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Slf4j(topic = "CANDIDATE_PROFILE_SERVICE")
@PreAuthorize("hasRole('ROLE_CANDIDATE')")
@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

  private final CandidateProfileMapper profileMapper;
  private final CandidateProfileRepository candidateProfileRepository;
  private final FileUploadService fileUploadService;
  private final UserService userService;

  private static String getProfilePhotoPath(int id) {
    return String.format("candidate-profile-photo/%d", id);
  }

  private static String getResumePath(int id) {
    return String.format("candidate-resume/%d", id);
  }

  @Override
  public void updateProfile(
      @RequestPart(value = "updateDetails") @Valid CandidateProfileUpdateDTO updateDTO,
      @RequestPart(value = "resume", required = false) @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = "application/pdf") MultipartFile resume,
      @RequestPart(value = "profilePhoto", required = false) @Valid @FileSize(min = 10, max = (long) 5e6, message = "File size must be between 100 bytes and 5 MB.", allowEmpty = false) @FileContentType(allowedTypes = {
          "image/png", "image/jpg", "image/jpeg"}) MultipartFile profilePhoto) throws IOException {
    CandidateProfile profile = getProfileById(updateDTO.getId());
    profileMapper.updateCandidateProfileFromDTO(updateDTO, profile);
    profile.setUser(userService.getUserById(profile.getId()));

    // delete or upload profile photo
    if (profilePhoto != null) {
      // save the profile photo in storage
      String profilePhotoPath = getProfilePhotoPath(profile.getId());
      boolean success = fileUploadService.uploadFile(profilePhotoPath, profilePhoto.getBytes());
      if (success) {
        profile.setHasProfilePhoto(true);
      }
    } else {
      fileUploadService.deleteFile(getProfilePhotoPath(profile.getId()));
      profile.setHasProfilePhoto(false);
    }

    // delete or upload resume
    if (resume != null) {
      // save resume in storage
      String resumePath = getResumePath(profile.getId());
      boolean success = fileUploadService.uploadFile(resumePath, resume.getBytes());
      if (success) {
        profile.setHasResume(true);
      } else {
        fileUploadService.deleteFile(resumePath);
        profile.setHasResume(false);
      }
    }

    candidateProfileRepository.save(profile);
  }

  @Override
  public CandidateProfileGetRequestDto getCandidateProfileById(Integer id)
      throws NotFoundException {
    CandidateProfile profile = getProfileById(id);
    String resumeUrl = null;
    if (Boolean.TRUE.equals(profile.getHasResume())) {
      resumeUrl = fileUploadService.getFileUrl(getResumePath(profile.getId()));
    }
    String profilePhotoUrl = null;
    if (Boolean.TRUE.equals(profile.getHasProfilePhoto())) {
      profilePhotoUrl = fileUploadService.getFileUrl(getProfilePhotoPath(profile.getId()));
    }
    return profileMapper.toCandidateProfileGetRequest(profile, profilePhotoUrl, resumeUrl);
  }

  private CandidateProfile getProfileById(Integer id) throws NotFoundException {
    return candidateProfileRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Candidate Profile not found for id: {}", id);
          return new NotFoundException("Candidate Profile not found");
        });
  }
}
