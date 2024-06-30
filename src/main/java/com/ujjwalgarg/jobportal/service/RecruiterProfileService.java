package com.ujjwalgarg.jobportal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * RecruiterProfileService
 */
@RequiredArgsConstructor
@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;

    @Transactional
    public RecruiterProfile createNew(RecruiterProfile profile) {
        return this.recruiterProfileRepository.save(profile);
    }

    public Optional<RecruiterProfile> getProfileById(int id) {
        return this.recruiterProfileRepository.findById(id);
    }

    @Transactional
    public RecruiterProfile updateProfile(RecruiterProfile profile) {
        return this.recruiterProfileRepository.save(profile);
    }

    public static String getProfileImageS3Path(int id) {
        return String.format("recruiter-profile-images/%d", id);
    }

}
