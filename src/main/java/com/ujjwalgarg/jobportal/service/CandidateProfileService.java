package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.controller.payload.candidateprofile.CandidateProfileGetRequestDto;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.service.dto.candidateprofileservice.CandidateProfileUpdateDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface CandidateProfileService {

  void updateProfile(CandidateProfileUpdateDTO updateDTO, MultipartFile profilePhoto,
      MultipartFile resume) throws IOException;

  CandidateProfileGetRequestDto getCandidateProfileById(Integer id) throws NotFoundException;
}
