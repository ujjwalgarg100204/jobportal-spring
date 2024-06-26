package com.ujjwalgarg.jobportal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;

import jakarta.transaction.Transactional;

/**
 * RecruiterProfileService
 */
@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Transactional
    public RecruiterProfile createNew(RecruiterProfile profile) {
        return this.recruiterProfileRepository.save(profile);
    }

    public Optional<RecruiterProfile> getProfileById(int id) {
        return this.recruiterProfileRepository.findById(id);
    }

}
