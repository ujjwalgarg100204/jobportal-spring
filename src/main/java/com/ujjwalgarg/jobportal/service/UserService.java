package com.ujjwalgarg.jobportal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * UserService
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CandidateProfileService candidateProfileService;
    private final RecruiterProfileService recruiterProfileService;

    @Transactional
    public User createNew(User user, RecruiterProfile recruiterProfile, CandidateProfile candidateProfile) {
        User savedUser = this.userRepository.save(user);

        // create an associated profile for the user
        if (savedUser.getRole().getName() == ERole.ROLE_RECRUITER) {
            if (recruiterProfile == null) {
                throw new IllegalArgumentException("Recruiter Profile can't be null when creating recruiter user");
            }
            recruiterProfile.setUser(savedUser);
            this.recruiterProfileService.createNew(recruiterProfile);
        } else {
            if (candidateProfile == null) {
                throw new IllegalArgumentException("Candidate Profile can't be null when creating candidate user");
            }
            candidateProfile.setUser(savedUser);
            this.candidateProfileService.createNew(candidateProfile);
        }

        return savedUser;
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean checkIfExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

}
