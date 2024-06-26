package com.ujjwalgarg.jobportal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;

/**
 * CandidateProfileService
 */
@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    @Autowired
    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    public CandidateProfile createNew(CandidateProfile profile) {
        return this.candidateProfileRepository.save(profile);
    }

    public Optional<CandidateProfile> getProfileById(int id) {
        return this.candidateProfileRepository.findById(id);
    }

}
