package com.ujjwalgarg.jobportal.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;

import lombok.RequiredArgsConstructor;

/**
 * CandidateProfileService
 */
@RequiredArgsConstructor
@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    @Transactional
    public CandidateProfile createNew(CandidateProfile profile) {
        return this.candidateProfileRepository.save(profile);
    }

    public Optional<CandidateProfile> getProfileById(int id) {
        return this.candidateProfileRepository.findById(id);
    }

    @Transactional
    public CandidateProfile updateProfile(CandidateProfile profile) {
        return this.candidateProfileRepository.save(profile);
    }

    public static String getProfileImageS3Path(int id) {
        return String.format("candidate-profile-images/%d", id);
    }

    public static String getResumeS3Path(int id) {
        return String.format("candidate-resumes/%d", id);
    }

}
