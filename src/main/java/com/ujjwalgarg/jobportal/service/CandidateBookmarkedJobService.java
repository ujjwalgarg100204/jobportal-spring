package com.ujjwalgarg.jobportal.service;

import org.springframework.stereotype.Service;

import com.ujjwalgarg.jobportal.repository.CandidateBookmarkedJobRepository;

import lombok.RequiredArgsConstructor;

/**
 * CandidateBookmarkedJobService
 */
@RequiredArgsConstructor
@Service
public class CandidateBookmarkedJobService {

    private final CandidateBookmarkedJobRepository cBookmarkedJobRepository;

    public boolean checkIfCandidateBookmarkedJob(Integer id, Integer jobId) {
        return this.cBookmarkedJobRepository.existsByCandidateProfile_IdAndJob_Id(id, jobId);
    }

}
