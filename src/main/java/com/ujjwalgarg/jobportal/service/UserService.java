package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;

public interface UserService {

  User createNewCandidate(User user, CandidateProfile cProfile);

  User createNewRecruiter(User user, RecruiterProfile rProfile);

  User getUserByEmail(String email);
}
