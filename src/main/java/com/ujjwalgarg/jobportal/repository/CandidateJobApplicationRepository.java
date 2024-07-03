package com.ujjwalgarg.jobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ujjwalgarg.jobportal.entity.CandidateJobApplication;

public interface CandidateJobApplicationRepository extends JpaRepository<CandidateJobApplication, Integer> {

    boolean existsByCandidateProfile_IdAndJob_Id(int candidateId, int jobId);

    int countByJob_Id(int jobId);

}
