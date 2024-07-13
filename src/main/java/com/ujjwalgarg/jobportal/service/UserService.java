package com.ujjwalgarg.jobportal.service;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;

public interface UserService {

  User createNewCandidate(User user, CandidateProfile cProfile) throws AlreadyPresentException;

  User createNewRecruiter(User user, RecruiterProfile rProfile) throws AlreadyPresentException;

  User getUserByEmail(String email) throws NotFoundException;
}
