package com.ujjwalgarg.jobportal.service;

import org.springframework.stereotype.Service;
import com.ujjwalgarg.jobportal.repository.CandidateJobApplicationRepository;

import lombok.RequiredArgsConstructor;

/**
 * CandidateJobApplicationService
 */
@RequiredArgsConstructor
@Service
public class CandidateJobApplicationService {

    private final CandidateJobApplicationRepository cJobApplicationRepository;

    public boolean checkIfCandidateAppliedForJob(int candidateId, int jobId) {
        return this.cJobApplicationRepository.existsByCandidateProfile_IdAndJob_Id(candidateId, jobId);
    }

    public int getJobApplicationCountsByJobId(int jobId) {
        return this.cJobApplicationRepository.countByJob_Id(jobId);
    }

}
