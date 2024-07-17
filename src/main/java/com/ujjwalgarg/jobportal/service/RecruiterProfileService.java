package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.service.dto.recruiterprofileservice.RecruiterProfileUpdateDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface RecruiterProfileService {

  void updateProfile(RecruiterProfileUpdateDTO profile, MultipartFile profilePhoto)
      throws NotFoundException, IOException;

}
